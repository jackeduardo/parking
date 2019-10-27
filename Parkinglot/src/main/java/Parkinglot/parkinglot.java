package Parkinglot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class parkinglot {
    private static int capacity =30;
    private static int remaining_capacity =30;
    private boolean if_admit ;

    public static int getCapacity() {
        return capacity;
    }

    public static int getRemaining_Capacity() {
        return remaining_capacity;
    }

    public boolean capacity_check() {
        return remaining_capacity >= 1;
    }

    public boolean get_if_admit(){
        if_admit=capacity_check();
        return  if_admit;
    }


    public void entrance_gate(cars car, ticket ticket) {
        ticket.setIn_time();
        car.setTicket(ticket);//give the ticket to the car
        remaining_capacity -= 1;

    }

    public void exit_gate(cars car, String intime, String outime) throws ParseException {
        int fee = payment_process(intime, outime);
        System.out.println("Car " + car.getLicense_plate() + " has paid the ticket for $" + fee + ".");
        remaining_capacity += 1;
    }

    public int payment_process(String intime, String outime) throws ParseException {
        int price = 5;//$5 per second
        return price * time_cal(intime, outime);
    }

    public int time_cal(String intime, String outime) throws ParseException {
        long d = 1000 * 24 * 60 * 60;
        long h = 1000 * 60 * 60;
        long m = 1000 * 60;
        long s = 1000;
        SimpleDateFormat DateFormat = new SimpleDateFormat("HH:mm:ss");
        Date in = DateFormat.parse(intime);
        Date out = DateFormat.parse(outime);
        long diff = out.getTime() - in.getTime();
        long sec = diff % d % h % m / s;
        return (int) sec;
    }



}
