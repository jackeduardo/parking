package Parkinglot;

public class parkinglot {
    private int capacity=50;
    private boolean if_admit=true;

    public void entrance_gate(cars car,ticket ticket){
        ticket.setIn_time();
        car.setTicket(ticket);//give the ticket to the car
        this.capacity-=1;
    }
    public void exit_gate(cars car,ticket ticket){
        ticket.setOut_time();
        String in_time=ticket.getIn_time();
        String out_time=ticket.getOut_time();//to be continued
        System.out.println("Ticket is paid.");
        System.out.println("Car "+car.getLicense_plate()+" exits.");
        this.capacity+=1;
    }
    public void payment_process(int Residence_time){
        int price=10;//$10 per second
        int fee=price* Residence_time;
        System.out.println("Parking fee is $"+fee+".");

    }

    public void Space_constrain(int capacity){
        if(capacity<1){
            System.out.println("No Space remained. Cars cannot be admitted.");
            this.if_admit=false;
        }
        else{
            this.if_admit=true;
        }
    }



}
