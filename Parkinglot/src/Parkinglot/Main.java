package Parkinglot;

import java.io.*;
import java.text.ParseException;
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
        //t.schedule(Parkinglot_timer, 1000, 1000);//Show the current time per sec
        BlockingQueue<String> plate_queue = new LinkedBlockingDeque();
        //BlockingQueue<Integer> residence_time_queue = new LinkedBlockingDeque();
        BlockingQueue<String> out_time_queue = new LinkedBlockingDeque();
        //Both threads for entrance and exit
        Thread Entrance = new Thread() {
            @Override
            public void run() {

                for (int i = 0; i < 100; i++) {// Each car will cost 1 sec at the entrance
                    try {
                        if (i < cars.length) {
                            ticket ticket = new ticket();
                            Parkinglot.entrance_gate(cars[i], ticket);
                            cars[i].getTicket().setIn_time();
                            String in_time = cars[i].getTicket().getIn_time();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            Date int_time_date = simpleDateFormat.parse(in_time);
                            String sec = DateToString(int_time_date, true, false, false);
                            String min = DateToString(int_time_date, false, true, false);
                            String hour = DateToString(int_time_date, false, false, true);
                            cars[i].generate_residence_time();
                            int[] in_time_int = {Integer.parseInt(hour), Integer.parseInt(min), Integer.parseInt(sec)};
                            String out_time_string = time_calculation(in_time_int, cars[i].getResidence_time());

                            out_time_queue.put(out_time_string);

                            plate_queue.put(cars[i].getLicense_plate());
                            //residence_time_queue.put(cars[i].getResidence_time());

                            //System.out.println(cars[i].getTicket().getIn_time());
                            Thread.sleep(1000);
                        }
                        else{
                            out_time_queue.put("No more car is at entrance.");

                            plate_queue.put("Null");
                        }
                    } catch (InterruptedException | ParseException e) {
                        e.printStackTrace();
                    }

                }

            }
        };
        BlockingQueue<String> exit_waiting_queue = new LinkedBlockingDeque();
        BlockingQueue<String> left_time = new LinkedBlockingDeque();
        //Both threads for entrance and exit
        Thread Exit = new Thread() {
            public void run() {
                //System.out.println("this thread2 "+cars[2].getResidence_time());
                List<String> plate_list = new ArrayList<String>();
                //List<Integer> residence_time_list = new ArrayList<Integer>();
                List<String> out_time_String = new ArrayList<String>();
                Map<String, String> actually_leaving_time_with_cars = new HashMap<String, String>();// Original out time+ queue time
                try {
                    for (int i = 0; i < 100; i++) {
                        SimpleDateFormat current_time = new SimpleDateFormat("HH:mm:ss");
                        String current_time_format = current_time.format(new Date());
                        System.out.println("this is current time: " + current_time_format);
                        out_time_String.add(out_time_queue.take());
                        System.out.println("this is out_time_String: " + out_time_String);
                        System.out.println("this is actually_leaving_time_with_cars: " + actually_leaving_time_with_cars);
                        plate_list.add(plate_queue.take());
                        System.out.println(out_time_String.size());
                        //residence_time_list.add(residence_time_queue.take());
                        int count = 0;
                        for (int j = 0; j < out_time_String.size(); j++) {
                            if (current_time_format.equals(out_time_String.get(j))) {
                                //exit_waiting_queue.put(plate_list.get(j));
                                String current_plate = plate_list.get(j);
                                System.out.println("Plate:" + current_plate + " is waiting to leave.");
                                String[] sArray = out_time_String.get(j).split(":");
                                int[] s_Array = new int[3];
                                for (int k = 0; k < sArray.length; k++) {
                                    s_Array[k] = Integer.parseInt(sArray[k]);
                                }
                                actually_leaving_time_with_cars.put(current_plate, time_calculation(s_Array, count));
                                count++;
                                for (Map.Entry<String, String> entry : actually_leaving_time_with_cars.entrySet()) {
                                    if (current_time_format.equals(entry.getValue())) {
                                        exit_waiting_queue.put(current_plate);
                                        left_time.put(entry.getValue());
                                    }
                                }
                                Thread.sleep(1000 * count);// each car costs 1 sec to get out
                                ;
                            }
                        }
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
//        Thread time_Process = new Thread() {
//            public void run() {
//                //System.out.println("this thread2 "+cars[2].getResidence_time());
//                try {
//                    Thread.sleep(1000);
//                    FileOutputStream fs = new FileOutputStream(new File("C:\\Users\\xuhaiyang\\Desktop\\cosc4353\\cosc4353 hw3\\parking\\Parkinglot\\carout"));
//                    PrintStream p = new PrintStream(fs);
//
//                    for (int i = 0; i < 100; i++) {
//                        String left_Car = exit_waiting_queue.take();
//                        String lefttime = left_time.take();
//                        System.out.println("Car " + left_Car + " has left.");
//                        p.println(left_Car + " left at " + lefttime);
//
//                    }
//                    p.close();
//
//                } catch (InterruptedException | FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
        Thread Exit_Process = new Thread() {
            public void run() {
                //System.out.println("this thread2 "+cars[2].getResidence_time());
                try {
                    Thread.sleep(1000);
                    FileOutputStream fs = new FileOutputStream(new File("C:\\Users\\xuhaiyang\\Desktop\\cosc4353\\cosc4353 hw3\\parking\\Parkinglot\\carout"));
                    PrintStream p = new PrintStream(fs);

                    for (int i = 0; i < 100; i++) {
                        String left_Car = exit_waiting_queue.take();
                        String lefttime = left_time.take();
                        System.out.println("Car " + left_Car + " has left.");
                        p.println(left_Car + " left at " + lefttime);

                    }
                    p.close();

                } catch (InterruptedException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        Entrance.start();
        Exit.start();
        //time_Process.start();
        Exit_Process.start();


    }

    public static cars[] read_input() throws Exception {
        File file = new File("C:\\Users\\xuhaiyang\\Desktop\\cosc4353\\cosc4353 hw3\\parking\\Parkinglot\\Cars'Stream");
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
        System.out.println("current time is :" + current_time_format);
    }
}
