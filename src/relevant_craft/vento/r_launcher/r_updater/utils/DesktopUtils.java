package relevant_craft.vento.r_launcher.r_updater.utils;

import java.awt.*;
import java.net.URI;

public class DesktopUtils {

    public static void openUrl(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {}
        }
    }
}
