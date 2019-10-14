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
        t.schedule(Parkinglot_timer, 1000, 1000);//Show the current time per sec
        BlockingQueue<String> plate_queue = new LinkedBlockingDeque();
        BlockingQueue<Integer> residence_time_queue = new LinkedBlockingDeque();
        BlockingQueue<String> out_time_queue = new LinkedBlockingDeque();
        //Both threads for entrance and exit
        Thread Entrance = new Thread() {
            @Override
            public void run() {

                for (int i = 0; i < cars.length; i++) {// Each car will cost 0.5 sec at the entrance
                    try {
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
                        residence_time_queue.put(cars[i].getResidence_time());

                        System.out.println(cars[i].getTicket().getIn_time());
                        Thread.sleep(500);
                    } catch (InterruptedException | ParseException e) {
                        e.printStackTrace();
                    }

                }

            }
        };
        Thread Exit = new Thread() {
            public void run() {
                //System.out.println("this thread2 "+cars[2].getResidence_time());
                List<String> plate_list = new ArrayList<String>();
                List<Integer> residence_time_list = new ArrayList<Integer>();
                List<String> out_time_String = new ArrayList<String>();
                Queue<String> exit_waiting_queue=new LinkedList<String>();
                try {
                    for (int i = 0; i < cars.length; i++) {
                        SimpleDateFormat current_time = new SimpleDateFormat("HH:mm:ss");
                        String current_time_format = current_time.format(new Date());
                        //System.out.println(current_time_format);
                        out_time_String.add(out_time_queue.take());
                        plate_list.add(plate_queue.take());
                        residence_time_list.add(residence_time_queue.take());
                        for (int j=0;j<out_time_String.size();j++){
                            if(current_time_format.equals(out_time_String.get(j))){
                                exit_waiting_queue.add(plate_list.get(j));
                                System.out.println("Plate:"+plate_list.get(j)+"is out.");
                            }
                        }
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Entrance.start();
        Exit.start();


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
        String sec = sec_pos + "";
        String min = min_pos + "";
        String hour = hour_pos + "";

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
