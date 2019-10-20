package Parkinglot;

import java.io.*;
import java.text.ParseException;
import java.time.Year;
import java.util.*;

import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static void main(String[] args) throws Exception {

        cars[] cars = read_input();
        parkinglot Parkinglot = new parkinglot();
        System.out.println("Parking lot is initialized.");
        Timer t = new Timer();
        Parkinglot_Timer Parkinglot_timer = new Parkinglot_Timer();
        t.schedule(Parkinglot_timer, 1000, 1000);//Show the current time per sec
        BlockingQueue<String> plate_queue = new LinkedBlockingDeque();
        BlockingQueue<String> out_time_queue = new LinkedBlockingDeque();
        BlockingQueue<cars> car_stream = new LinkedBlockingDeque();
        //2 threads for entrance and exit

        Thread Entrance = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int countforwaiting = 0;
                for (int i = 0; i < 100; i++) {// Each car will cost 1 sec at the entrance
                    try {
                        int index = i - countforwaiting;
                        if (index < cars.length || Parkinglot.get_if_admit()) {
                            ticket ticket = new ticket();
                            if (Parkinglot.get_if_admit() && index < cars.length) {// check the capacity for the parking lot whether it admits more cars to come in.
                                Parkinglot.entrance_gate(cars[index], ticket);
                                cars[index].getTicket().setIn_time();
                                String in_time = cars[index].getTicket().getIn_time();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                Date int_time_date = simpleDateFormat.parse(in_time);
                                String sec = DateToString(int_time_date, true, false, false);
                                String min = DateToString(int_time_date, false, true, false);
                                String hour = DateToString(int_time_date, false, false, true);
                                cars[index].generate_residence_time();
                                int[] in_time_int = {Integer.parseInt(hour), Integer.parseInt(min), Integer.parseInt(sec)};
                                String out_time_string = time_calculation(in_time_int, cars[index].getResidence_time());
                                car_stream.put(cars[index]);
                                out_time_queue.put(out_time_string);
                                plate_queue.put(cars[index].getLicense_plate());
                                System.out.println("Car " + cars[index].getLicense_plate() + " has entered." + " ");
                                System.out.println("Parking lots' capacity: " + parkinglot.getRemaining_Capacity());
                                Thread.sleep(1000);
                            } else {
                                if (!Parkinglot.get_if_admit()) {
                                    System.out.println("Parking lot is full. Cars cannot be admitted.");
                                }
                                if (Parkinglot.getCapacity() == parkinglot.getRemaining_Capacity()) {
                                    System.out.println("Parking lot is empty.");
                                }
                                countforwaiting++;
                                Thread.sleep(1000);
                            }
                        }
                    } catch (InterruptedException | ParseException e) {
                        e.printStackTrace();
                    }

                }

            }
        };
        BlockingQueue<Parkinglot.cars> exit_waiting_queue = new LinkedBlockingDeque();
        BlockingQueue<String> left_time = new LinkedBlockingDeque();
        BlockingQueue<String> actually_leaving_time = new LinkedBlockingDeque();
        BlockingQueue<cars> car_stream_receive = new LinkedBlockingDeque();
        BlockingQueue<String> current_time = new LinkedBlockingDeque();
        //Both threads for entrance and exit
        Thread Exit = new Thread() {
            public void run() {
                Map<cars, String> actually_leaving_time_with_cars = new HashMap<cars, String>();// Original out time+ queue time
                try {
                    for (int i = 0; i < 100; i++) {
                        //int count=0;
                        String Current_time = current_time.take();
                        System.out.println("Current time is : " + Current_time);
                        boolean check = true;
                        if (car_stream_receive.peek() != null && actually_leaving_time.peek() != null) {
                            if (actually_leaving_time_with_cars.size() < cars.length) {
                                actually_leaving_time_with_cars.put(car_stream_receive.take(), actually_leaving_time.take());
                            } else {
                                Thread.sleep(1000);
                                check = false;
                            }
                        }
                        for (Map.Entry<cars, String> entry : actually_leaving_time_with_cars.entrySet()) {
                            if (Current_time.equals(entry.getValue())) {
                                exit_waiting_queue.put(entry.getKey());
                                left_time.put(entry.getValue());

                            }
                        }
                        if (check) {
                            Thread.sleep(1000);// each car costs 1 sec to get out
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread time_Process = new Thread() {
            public void run() {
                List<String> out_time_String = new ArrayList<String>();
                List<String> plate_list = new ArrayList<String>();
                List<cars> cars_list = new ArrayList<cars>();
                try {
                    int count = 0;
                    for (int i = 0; i < 100; i++) {

                        SimpleDateFormat current_time = new SimpleDateFormat("HH:mm:ss");
                        String current_time_format = current_time.format(new Date());
                        if (out_time_queue.peek() != null && plate_queue.peek() != null && car_stream.peek() != null) {
                            out_time_String.add(out_time_queue.take());
                            plate_list.add(plate_queue.take());
                            cars_list.add(car_stream.take());
                        }
                        for (int j = 0; j < out_time_String.size(); j++) {
                            if (current_time_format.equals(out_time_String.get(j))) {
                                //exit_waiting_queue.put(plate_list.get(j));
                                String current_plate = plate_list.get(j);
                                //System.out.println("Plate:" + current_plate + " is waiting to leave.");
                                //Current_plate.put(current_plate);
                                car_stream_receive.put(cars_list.get(j));
                                String[] sArray = out_time_String.get(j).split(":");
                                int[] s_Array = new int[3];
                                for (int k = 0; k < sArray.length; k++) {
                                    s_Array[k] = Integer.parseInt(sArray[k]);
                                }
                                count++;
                                actually_leaving_time.put(time_calculation(s_Array, count));
                            }
                        }
                        Thread.sleep(1000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread Exit_Process = new Thread() {
            public void run() {
                //System.out.println("this thread2 "+cars[2].getResidence_time());
                try {

                    FileOutputStream fs = new FileOutputStream(new File("C:\\Users\\xuhaiyang\\Desktop\\cosc4353\\cosc4353 hw3\\parking\\Parkinglot\\car_output\\carout"));
                    PrintStream p = new PrintStream(fs);

                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(2000);
                        Parkinglot.cars left_Car = exit_waiting_queue.take();
                        String lefttime = left_time.take();
                        String entertime = left_Car.getTicket().getIn_time();
                        System.out.print("Car " + left_Car.getLicense_plate() + " has left." + " " + "\n" + "\r");
                        p.println("Car " + left_Car.getLicense_plate() + " left at " + lefttime);
                        left_Car.getTicket().setOut_time(lefttime);
                        Parkinglot.exit_gate(left_Car, entertime, lefttime);
                        System.out.println("Parking lots' capacity: " + Parkinglot.getRemaining_Capacity());
                    }
                    p.close();

                } catch (InterruptedException | FileNotFoundException | ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread time_updater = new Thread() {
            public void run() {
                SimpleDateFormat Current_time = new SimpleDateFormat("HH:mm:ss");

                try {
                    for (int i = 0; i < 100; i++) {
                        String current_time_format = Current_time.format(new Date());
                        current_time.put(current_time_format);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Entrance.start();
        Exit.start();
        time_Process.start();
        Exit_Process.start();
        time_updater.start();


    }

    public static cars[] read_input() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Select the input (enter 1 - 5)");
        String carin_number = input.nextLine();
        File file = new File("C:\\Users\\xuhaiyang\\Desktop\\cosc4353\\cosc4353 hw3\\parking\\Parkinglot\\car_input\\carin" + carin_number);
        BufferedReader br_linescount = new BufferedReader(new FileReader(file));
        BufferedReader car_info_reader = new BufferedReader(new FileReader(file));
        int lines = 0;
        while (br_linescount.readLine() != null) lines++;
        cars[] Cars = new cars[lines];
        String car_info;
        int count = 0;
        while ((car_info = car_info_reader.readLine()) != null) {
            Cars[count] = new cars();
            Cars[count].setLicense_plate(car_info);
            count++;
        }
        br_linescount.close();
        car_info_reader.close();
        return Cars;
    }

    public static String DateToString(java.util.Date Date, boolean sec, boolean min, boolean hour) {
        if (sec) {
            SimpleDateFormat formatter = new SimpleDateFormat("ss");
            return formatter.format(Date);
        }
        if (min) {
            SimpleDateFormat formatter = new SimpleDateFormat("mm");
            return formatter.format(Date);
        }
        if (hour) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH");
            return formatter.format(Date);
        }
        return "again";

    }

    public static String time_calculation(int[] in_time, int residence_time) {
        int sec_pos = in_time[2] + residence_time;
        int carry_sec = sec_pos / 60;
        int min_pos = in_time[1];
        int hour_pos = in_time[0];
        if (sec_pos >= 60) {
            sec_pos -= 60 * carry_sec;
            min_pos += carry_sec;
            int carry_min = min_pos / 60;
            if (min_pos >= 60) {
                min_pos -= 60 * carry_min;
                hour_pos += carry_min;
            }
        }
        String sec;
        String min;
        String hour;
        if (sec_pos >= 10) {
            sec = sec_pos + "";
        } else {
            sec = "0" + sec_pos + "";
        }
        if (min_pos >= 10) {
            min = min_pos + "";
        } else {
            min = "0" + min_pos + "";
        }
        if (hour_pos >= 10) {
            hour = hour_pos + "";
        } else {
            hour = "0" + hour_pos + "";
        }

        String time = hour + ":" + min + ":" + sec;
        return time;
    }

}

class Parkinglot_Timer extends TimerTask {
    @Override
    public void run() {
        SimpleDateFormat current_time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String current_time_format = current_time.format(new Date());
        //System.out.println("Current time is :" + current_time_format);
    }
}
