package relevant_craft.vento.r_launcher.r_updater.utils;

import relevant_craft.vento.r_launcher.r_updater.VENTO;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Formatter;

public class HashUtils {

    public static String md5(String filename) {
        if (!new File(filename).exists()) return "0";
        FileInputStream fis = null;
        DigestInputStream dis = null;
        BufferedInputStream bis = null;
        Formatter formatter = null;
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(filename);
            bis = new BufferedInputStream(fis);
            dis = new DigestInputStream(bis, messagedigest);
            while (dis.read() != -1) ;
            byte[] abyte0 = messagedigest.digest();
            formatter = new Formatter();
            byte[] abyte1 = abyte0;
            int i = abyte1.length;
            for (int j = 0; j < i; j++) {
                byte byte0 = abyte1[j];
                formatter.format("%02x", new Object[]{Byte.valueOf(byte0)});
            }
            return formatter.toString();
        } catch (Exception e) {
            return "0";
        } finally {
            try {
                fis.close();
            } catch (Exception ignored) {}
            try {
                dis.close();
            } catch (Exception ignored) {}
            try {
                bis.close();
            } catch (Exception ignored) {}
            try {
                formatter.close();
            } catch (Exception ignored) {}
        }
    }

    public static void truncHash(String hashFile) {
        File hash = new File(VENTO.LAUNCHER_DIR + File.separator + "hashes" + File.separator + hashFile + ".md5");
        if (hash.exists()) {
            hash.delete();
        }
    }

    public static void writeHash(String hashFile, String data, boolean append) {
        File hashes = new File(VENTO.LAUNCHER_DIR + File.separator + "hashes");
        if (!hashes.exists()) { hashes.mkdir(); }

        try (FileWriter writer = new FileWriter(hashes + File.separator + hashFile + ".md5", append)) {
            writer.append(data + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void lockHash(String hashFile) {
        File hash = new File(VENTO.LAUNCHER_DIR + File.separator + "hashes" + File.separator + hashFile + ".md5");
        if (hash.exists()) {
            hash.setReadOnly();
        }
    }

    public static String readHash(String hashFile) {
        File hash = new File(VENTO.LAUNCHER_DIR + File.separator + "hashes" + File.separator + hashFile + ".md5");
        if (!hash.exists()) {
            return null;
        }

        try {
            FileInputStream fstream = new FileInputStream(hash);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String line = br.readLine();

            return line;
        } catch (Exception e) {
            return null;
        }
    }
}
