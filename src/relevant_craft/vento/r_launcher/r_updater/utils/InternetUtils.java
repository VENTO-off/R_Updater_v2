package relevant_craft.vento.r_launcher.r_updater.utils;

import java.net.InetSocketAddress;
import java.net.Socket;

public class InternetUtils {

    public static boolean checkRealInternetConnection() {
        return checkInternet("8.8.8.8", 53);
    }

    public static boolean checkFirewallInternetConnection() {
        return checkInternet("www.r-launcher.su", 80);
    }

    private static boolean checkInternet(String host, int port) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 5000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
