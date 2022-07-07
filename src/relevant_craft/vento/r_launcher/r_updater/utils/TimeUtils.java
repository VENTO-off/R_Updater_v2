package relevant_craft.vento.r_launcher.r_updater.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static String getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yy");
        return dateFormat.format(date);
    }
}
