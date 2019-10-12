package Parkinglot;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ticket {
    private String in_time;
    private String out_time;

    public void setIn_time() {
        Date currentTime = new Date();
        this.in_time = DateToStr(currentTime);
    }

    public String getIn_time() {
        return in_time;
    }
    public void setOut_time() {
        Date currentTime = new Date();
        this.out_time = DateToStr(currentTime);
    }
    public String getOut_time() {
        return out_time;
    }

    public static String DateToStr(java.util.Date Date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return formatter.format(Date);
    }

}
