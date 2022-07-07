package relevant_craft.vento.r_launcher.r_updater.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<File> listFiles(File directory) {
        List<File> files = new ArrayList<>();

        File[] list = directory.listFiles();
        if (list != null) {
            for (File file : list) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    files.addAll(listFiles(file));
                }
            }
        }

        return files;
    }

    public static long getCreationTime(File file) {
        if (!file.exists()) {
            return 0;
        }

        try {
            Path p = Paths.get(file.getAbsolutePath());
            BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
            return view.creationTime().toMillis();
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getModifiedTime(File file) {
        if (!file.exists()) {
            return 0;
        }

        try {
            Path p = Paths.get(file.getAbsolutePath());
            BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
            return view.lastModifiedTime().toMillis();
        } catch (Exception e) {
            return 0;
        }
    }

    public static void checkFile(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written");
            }
        } else {
            File parent = file.getParentFile();
            if ((parent != null) && (!parent.exists()) && (!parent.mkdirs())) {
                throw new IOException("File '" + file + "' could not be created");
            }
        }
    }

    public static void removeDirectory(File directory) {
        if (directory == null)
            return;
        if (!directory.exists())
            return;
        if (!directory.isDirectory())
            return;

        String[] list = directory.list();

        if (list != null) {
            for (String file : list) {
                File entry = new File(directory, file);

                if (entry.isDirectory()) {
                    removeDirectory(entry);
                } else {
                    entry.delete();
                }
            }
        }

        directory.delete();
    }
}
