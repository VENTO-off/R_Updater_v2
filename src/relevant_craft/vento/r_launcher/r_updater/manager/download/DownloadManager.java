package relevant_craft.vento.r_launcher.r_updater.manager.download;

import javafx.application.Platform;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_ProgressBar;
import relevant_craft.vento.r_launcher.r_updater.utils.FileUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.FormatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManager {

    public static void downloadFile(String _url, String _file, final R_ProgressBar progressBar, long downloaded, long size, final long startTime) throws IOException {
        URL url = new URL(_url);
        File file = new File(_file + ".download");
        if (file.exists()) { file.delete(); }

        long lastSpeedUpdate = System.currentTimeMillis();

        InputStream input = url.openStream();
        URLConnection urlconnection = url.openConnection();
        urlconnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
        urlconnection.setDefaultUseCaches(false);
        if (size == 0) {
            size = urlconnection.getContentLength();
        }

        FileUtils.checkFile(file);

        FileOutputStream output = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int n;
        while (-1 != (n = input.read(buffer))) {
            downloaded += n;
            output.write(buffer, 0, n);

            final int progress = (int) (downloaded * 100 / size);

            final long finalDownloaded = downloaded;
            final long finalSize = size;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if ((int) (progressBar.getProgress() * 100) != progress) {
                        progressBar.setProgress(progress);
                        progressBar.setSize(FormatUtils.formatSize(finalDownloaded, true) + "/" + FormatUtils.formatSize(finalSize, true));
                    }
                }
            });

            if (System.currentTimeMillis() - lastSpeedUpdate >= 250 || downloaded == size) {
                lastSpeedUpdate = System.currentTimeMillis();
                try {
                    long timeTook = System.currentTimeMillis() - startTime;
                    final long speedInBytes = (long) (downloaded / (timeTook / 1000D));
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setSpeed(FormatUtils.formatSize(speedInBytes, true) + "/cек");
                        }
                    });
                } catch (ArithmeticException ignored) {}
            }
        }

        input.close();
        output.close();

        if (new File(_file).exists()) { new File(_file).delete(); }
        file.renameTo(new File(_file));
    }

    public static void downloadFile(String _url, String _file) throws IOException {
        URL url = new URL(_url);
        File file = new File(_file + ".download");
        if (file.exists()) { file.delete(); }

        InputStream input = url.openStream();
        URLConnection urlconnection = url.openConnection();
        urlconnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
        urlconnection.setDefaultUseCaches(false);

        FileUtils.checkFile(file);

        FileOutputStream output = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }

        input.close();
        output.close();

        if (new File(_file).exists()) { new File(_file).delete(); }
        file.renameTo(new File(_file));
    }

}
