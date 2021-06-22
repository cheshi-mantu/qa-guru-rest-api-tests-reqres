package helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetDate {

    public static String getTodaysDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime todayDate = LocalDateTime.now();
        return dtf.format(todayDate);
    }
}
