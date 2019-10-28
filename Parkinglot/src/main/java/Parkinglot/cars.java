package Parkinglot;

import java.util.Random;

public class cars {
    private String license_plate;
    private int Residence_time;// The residence time is random for each car.
    private boolean car_state = false;// If the car enters or exits the parking lot.
    private ticket ticket;
    private double received_price;
    private boolean if_interest;

    public void setReceived_price(double received_price) {
        Random random = new Random();
        if_interest=random.nextBoolean();
        if(if_interest){
        this.received_price = received_price;}
    }
    public boolean get_interest(){
        return if_interest;
    }
    public void generate_residence_time() {
        Random random = new Random();
        this.Residence_time = random.nextInt((10-5)+1)+5;
    }

    public int getResidence_time() {
        return Residence_time;
    }

    public void setLicense_plate(String plate) {
        this.license_plate = plate;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void update_state() {

    }

    public void setTicket(ticket ticket) {
        this.ticket = ticket;
    }

    public Parkinglot.ticket getTicket() {
        return ticket;
    }
}
