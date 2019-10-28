package Parkinglot;

import java.util.Random;

public class Group {
    private parkinglot Parking_lot;
     private int original_price;
     private double discounts;
     private int policies;

    public int getOriginal_price() {
        return original_price;
    }

    public void setPolicies() {
        Random random = new Random();
        this.policies = random.nextInt((15-5)+1)+5;
    }

    public int getPolicies() {
        return policies;
    }

    public void setprice(){
        Random random = new Random();
        original_price=random.nextInt((20-5)+1)+5;
         Parking_lot.setPrice(discounts*original_price);
     }
    public double getprice(){
        return Parking_lot.getPrice();
    }

    public void setDiscounts(int parkinglot_num) {
        Random random = new Random();
        int discounts=random.nextInt(100);
        this.discounts = (double)discounts/100;
        System.out.println("The discount of parking lots "+(parkinglot_num+1)+" is " +(100-discounts)+"% off!");
    }

    public double getDiscounts() {
        return discounts;
    }

    public void setParkinglot() {
        parkinglot parkinglot = new parkinglot();
        Parking_lot = parkinglot;
    }
    public void send_price(cars car){//send the latest prices to interested cars
        car.setReceived_price(getprice());
    }

    public parkinglot getParkinglot() {
        return Parking_lot;
    }
}
