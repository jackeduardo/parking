package Parkinglot;

import java.util.Random;

public class cars {
    private String license_plate;
    private int Residence_time;// The residence time is random for each car.
    private boolean car_state = false;// If the car enters or exits the parking lot.
    private ticket ticket;

    public void generate_residence_time() {
        Random random = new Random();
        this.Residence_time = random.nextInt((5-1)+1)+1;
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
