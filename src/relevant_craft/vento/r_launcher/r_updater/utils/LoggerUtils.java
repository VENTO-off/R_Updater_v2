package relevant_craft.vento.r_launcher.r_updater.utils;

import relevant_craft.vento.r_launcher.r_updater.VENTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class LoggerUtils {

    private static File logs = new File(VENTO.LAUNCHER_DIR + File.separator + "r-updater_logs");

    public static void initLogger() {
        try {
            PrintStream out = new PrintStream(new FileOutputStream(getLoggerFile()), true);
            System.setOut(out);
            System.setErr(out);
        } catch (FileNotFoundException ignored) {}
    }

    private static String getLoggerFile() {
        if (!logs.exists()) { logs.mkdir(); }

        clearLogs();

        int counter = 1;
        File log;
        do {
            log = new File(logs + File.separator + TimeUtils.getCurrentDate() + " #" + counter + ".log");
            counter++;
        } while (log.exists());

        return log.getAbsolutePath();
    }

    private static void clearLogs() {
        if (!logs.exists()) {
            return;
        }

        for (File file : logs.listFiles()) {
            if (!file.getName().startsWith(TimeUtils.getCurrentDate())) {
                file.delete();
            }
        }
    }

    public static void log(String data) {
        System.out.println("[" + TimeUtils.getCurrentTime() + "] " + data);
    }
}
