package relevant_craft.vento.r_launcher.r_updater.manager.java;

import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.utils.FileUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.HashUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.OperatingSystem;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaManager {

    public static boolean isJavaValid() {
        if (!System.getProperty("java.version").startsWith("1.8")) {
            return false;
        }

        File jre = new File(System.getProperty("java.home"));
        if (!jre.exists()) {
            return false;
        }

        if (!jre.isDirectory()) {
            return false;
        }

        long creationTime = FileUtils.getCreationTime(jre);
        List<File> jreFiles = FileUtils.listFiles(jre);
        if (jreFiles.size() < 100) {
            return false;
        }

        for (File file : jreFiles) {
            if (Math.abs(FileUtils.getCreationTime(file) - creationTime) >= 1000 * 60) {
                return false;
            }
            if (Math.abs(FileUtils.getModifiedTime(file) - creationTime) >= 1000 * 60) {
                return false;
            }
            if ((file.getName().endsWith(".jar") || file.getName().endsWith(".dll") || file.getName().endsWith(".exe")) && file.length() == 0) {
                return false;
            }
        }

        if (getJavaArch() != getRealArch()) {
            return false;
        }

        return true;
    }

    public static int getRealArch() {
        if (OperatingSystem.getCurrentPlatform() == OperatingSystem.WINDOWS) {
            if (System.getenv("ProgramFiles(x86)") != null) {
                return 64;
            } else {
                return 32;
            }
        } else {
            return getJavaArch();
        }
    }

    private static int getJavaArch() {
        Map<String, Integer> archValues = new HashMap<>();
        archValues.put("x86", 32);
        archValues.put("i386", 32);
        archValues.put("i486", 32);
        archValues.put("i586", 32);
        archValues.put("i686", 32);
        archValues.put("x86_64", 64);
        archValues.put("amd64", 64);

        String arch = System.getProperty("os.arch");
        if (archValues.containsKey(arch)) {
            return archValues.get(arch);
        } else {
            return 32;
        }
    }

    public static List<JavaFile> getJavaFiles(OperatingSystem os, int arch) {
        String response;
        try {
            URL url = new URL(VENTO.WEB + "jvm.php?os=" + os.getName() + "&arch=" + arch);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            response = bufferedreader.readLine();
            bufferedreader.close();
        } catch (IOException e) {
            return null;
        }

        List<JavaFile> javaFiles = new ArrayList<>();
        String[] files = response.split("<::>");
        for (String file : files) {
            String[] currentFile = file.split(";");
            javaFiles.add(new JavaFile(currentFile[0], Integer.valueOf(currentFile[1])));
        }

        return javaFiles;
    }

    public static long getJavaFilesSize(List<JavaFile> javaFiles) {
        long size = 0;
        for (JavaFile jf : javaFiles) {
            size += jf.getSize();
        }

        return size;
    }

    public static boolean isLauncherJavaValid() {
        File jvmHash = new File(VENTO.LAUNCHER_DIR + File.separator + "hashes" + File.separator + "jvm.md5");
        if (!jvmHash.exists()) {
            return false;
        }

        try {
            FileInputStream fstream = new FileInputStream(jvmHash);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                File javaFile = new File(VENTO.LAUNCHER_DIR + File.separator + data[0]);
                int size = Integer.valueOf(data[1]);
                if (!javaFile.exists()) {
                    return false;
                }
                if (javaFile.length() != size) {
                    return false;
                }
            }
            fstream.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static String getLauncherJavaDir() {
        try {
            String line = HashUtils.readHash("jvm");
            if (line == null) {
                return null;
            }

            String[] data = line.split(";");
            String firstJvmFile = data[0];

            File launcherJavaHome = new File(VENTO.LAUNCHER_DIR + File.separator + firstJvmFile);
            while (launcherJavaHome != null) {
                if (launcherJavaHome.getName().startsWith("jre-") && launcherJavaHome.isDirectory()) {
                    break;
                }
                launcherJavaHome = launcherJavaHome.getParentFile();
            }

            if (launcherJavaHome == null) {
                return null;
            }

            String path = launcherJavaHome.getAbsolutePath() + File.separator + "bin" + File.separator;

            if (OperatingSystem.getCurrentPlatform() == OperatingSystem.WINDOWS && new File(path + "javaw.exe").isFile()) {
                return path + "javaw.exe";
            }

            return path + "java";
        } catch (Exception e) {
            return null;
        }
    }
}
