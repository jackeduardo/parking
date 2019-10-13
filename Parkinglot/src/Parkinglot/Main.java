package Parkinglot;

import java.io.*;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static void main(String[] args) throws Exception {

        cars[] cars = read_input();
        ticket ticket = new ticket();
        parkinglot Parkinglot = new parkinglot();
        System.out.println("Parking lot is initialized.");
        Timer t = new Timer();
        Parkinglot_Timer Parkinglot_timer = new Parkinglot_Timer();
        t.schedule(Parkinglot_timer, 1000, 1000);//Show the current time per sec
        BlockingQueue<String> plate_queue = new LinkedBlockingDeque();
        BlockingQueue<Integer> residence_time_queue = new LinkedBlockingDeque();
        //Both threads for entrance and exit
        Thread Entrance = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < cars.length; i++) {// Each car will cost 0.5 sec at the entrance
                    try {
                        Parkinglot.entrance_gate(cars[i], ticket);
                        cars[i].generate_residence_time();
                        plate_queue.put(cars[i].getLicense_plate());
                        residence_time_queue.put(cars[i].getResidence_time());
                        System.out.println(cars[i].getTicket().getIn_time());
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        };
        Thread Exit = new Thread() {
            public void run() {
                //System.out.println("this thread2 "+cars[2].getResidence_time());
                try {
                    Thread.sleep(500);
                    for (int i = 0; i < cars.length; i++) {
                        plate_queue.take();
                        residence_time_queue.take();
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

}

class Parkinglot_Timer extends TimerTask {
    @Override
    public void run() {
        SimpleDateFormat current_time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String current_time_format = current_time.format(new Date());
        System.out.println("current time is :" + current_time_format);
    }
}
