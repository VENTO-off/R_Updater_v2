package relevant_craft.vento.r_launcher.r_updater.manager.updater;

import relevant_craft.vento.r_launcher.r_updater.VENTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdaterManager {

    private boolean CHECK_UPDATE;
    private String UPDATER_VERSION;
    private String MD5;
    private String LAUNCHER_PATH;
    private String LAUNCHER_VERSION;

    public UpdaterManager() throws Exception {
        String response;
        try {
            URL url = new URL(VENTO.WEB + "updater.php?r_updater");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            response = bufferedreader.readLine();
            bufferedreader.close();
        } catch (IOException e) {
            throw new Exception();
        }

        String[] data = response.split("<::>");

        this.CHECK_UPDATE = Boolean.valueOf(data[0]);
        this.UPDATER_VERSION = data[1];
        this.MD5 = data[2];
        this.LAUNCHER_PATH = data[3];
        this.LAUNCHER_VERSION = data[4];
    }

    public boolean doCheckUpdate() {
        return CHECK_UPDATE;
    }

    public String getUpdaterVersion() {
        return UPDATER_VERSION;
    }

    public String getMD5() {
        return MD5;
    }

    public String getLauncherPath() {
        return LAUNCHER_PATH;
    }

    public String getLauncherVersion() {
        return LAUNCHER_VERSION;
    }
}
