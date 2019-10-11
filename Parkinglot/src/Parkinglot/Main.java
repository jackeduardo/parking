package Parkinglot;

import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        Timer t = new Timer();
        Parkinglot_Timer Parkinglot_timer = new Parkinglot_Timer();
        t.schedule(Parkinglot_timer, 1000, 1000);

    }

}

class Parkinglot_Timer extends TimerTask {
    @Override
    public void run() {
        SimpleDateFormat current_time = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        String current_time_format = current_time.format(new Date());
        System.out.println("current time is :" + current_time_format);
    }
}
