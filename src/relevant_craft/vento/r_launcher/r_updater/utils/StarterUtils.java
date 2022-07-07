package relevant_craft.vento.r_launcher.r_updater.utils;

import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.gui.notify.NotifyType;
import relevant_craft.vento.r_launcher.r_updater.gui.notify.NotifyWindow;
import relevant_craft.vento.r_launcher.r_updater.manager.java.JavaManager;

import java.util.ArrayList;

public class StarterUtils {

    private static Process process = null;

    public static void runLauncher(boolean online) {
        ArrayList<String> params = new ArrayList<>();
        if (JavaManager.isLauncherJavaValid() && JavaManager.getLauncherJavaDir() != null) {
            params.add("\"" + JavaManager.getLauncherJavaDir() + "\"");
            LoggerUtils.log("Using custom Java.");
        } else {
            params.add("\"" + OperatingSystem.getCurrentPlatform().getJavaDir() + "\"");
            LoggerUtils.log("Using system Java.");
        }

        params.add("-jar");
        params.add("\"" + VENTO.LAUNCHER_PATH.getAbsolutePath() + "\"");
        params.add("r_updater");
        params.add(VENTO.UPDATER_PATH.getAbsolutePath().replace(" ", "%20"));
        params.add(online ? "online" : "offline");

        ProcessBuilder pb = new ProcessBuilder(params);
        pb.redirectErrorStream(true);
        pb.directory(VENTO.LAUNCHER_DIR);
        try {
            process = pb.start();
//            Thread.sleep(3000);
//            try {
//                if (process.exitValue() != 0) {
//                    throw new Exception();
//                }
//            } catch (Exception ignored) {}
        } catch (Exception e) {
            LoggerUtils.log("Error starting launcher.");
            e.printStackTrace();
            try {
                NotifyWindow notify = new NotifyWindow(NotifyType.ALERT);
                notify.setTitle("Ошибка");
                notify.setMessage("Не удалось запустить лаунчер.\nПопробуйте переустановить Java.");
                notify.showInMainThread();

                VENTO.mainGui.close();
            } catch (Exception ignored) {}
        }
    }

    private static boolean isRunning() {
        try {
            process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static void kill() {
        if (process != null) {
            if (isRunning()) {
                process.destroy();
            }
        }
    }

}
