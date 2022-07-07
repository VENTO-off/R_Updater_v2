package relevant_craft.vento.r_launcher.r_updater;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.WritableValue;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import relevant_craft.vento.r_launcher.r_updater.gui.help.HelpGui;
import relevant_craft.vento.r_launcher.r_updater.gui.install.InstallGui;
import relevant_craft.vento.r_launcher.r_updater.gui.main.MainGui;
import relevant_craft.vento.r_launcher.r_updater.gui.notify.NotifyType;
import relevant_craft.vento.r_launcher.r_updater.gui.notify.NotifyWindow;
import relevant_craft.vento.r_launcher.r_updater.gui.notify.SideNotifyWindow;
import relevant_craft.vento.r_launcher.r_updater.manager.download.DownloadManager;
import relevant_craft.vento.r_launcher.r_updater.manager.picture.PictureManager;
import relevant_craft.vento.r_launcher.r_updater.manager.updater.UpdaterManager;
import relevant_craft.vento.r_launcher.r_updater.utils.*;

import java.io.File;

public class VENTO extends Application {
    public final static String REPOSITORY = "https://files.r-launcher.su/";
    public final static String SITE = "https://www.r-launcher.su";
    public final static String WEB = SITE + "/r-launcher/";
    public final static String UPDATER_VERSION = "2.1";
    public static File UPDATER_PATH = null;
    public static File LAUNCHER_DIR = null;
    public static File LAUNCHER_PATH = null;

    public static Stage stage = null;
    public static WritableValue<Double> writableHeight = null;
    public static WritableValue<Double> writableY = null;

    public static MainGui mainGui = null;
    public static InstallGui installGui = null;
    public static HelpGui helpGui = null;

    public static final String FAQ_URL = "https://r-launcher.su/faq";
    public static final String MANAGER_URL = "https://vk.com/im?sel=-160540244";
    public static final String SUBSCRIBE_URL = "https://vk.com/widget_community.php?act=a_subscribe_box&oid=-160540244&state=1";

    public static boolean FORCE_INSTALL_LAUNCHER = false;
    public static boolean FORCE_INSTALL_JVM = false;

    public static Thread loader = null;

    @Override
    public void start(final Stage stage) throws Exception {
        VENTO.stage = stage;
        UPDATER_PATH = new File(VENTO.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        LAUNCHER_DIR = new File(System.getProperty("user.home") + File.separator + ".r-launcher");
        if (!LAUNCHER_DIR.exists()) { LAUNCHER_DIR.mkdirs(); }
        LAUNCHER_PATH = new File(VENTO.LAUNCHER_DIR + File.separator + "R-Launcher.jar");

        Platform.setImplicitExit(false);
        LoggerUtils.initLogger();

        LoggerUtils.log("Starting R-Launcher Updater v" + UPDATER_VERSION + "...");

        LoggerUtils.log("Initializing mainGui...");
        mainGui = new MainGui();
        stage.setTitle(mainGui.getTitle());
        stage.getIcons().setAll(PictureManager.loadImage("icon.png"));
        stage.setScene(new Scene(mainGui.getLayout()));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.show();
        LoggerUtils.log("Initialized mainGui.");

        final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (FORCE_INSTALL_LAUNCHER || FORCE_INSTALL_JVM) {
                    LoggerUtils.log("Force running launcher installer...");
                    this.delay();

                    LoggerUtils.log("Purging launcher directory...");
                    FileUtils.removeDirectory(LAUNCHER_DIR);
                    LAUNCHER_DIR.mkdir();
                    LoggerUtils.log("Purged launcher directory.");

                    this.runLauncherInstaller();
                    return null;
                }

                LoggerUtils.log("Checking for existence JAR...");
                if (LAUNCHER_PATH.exists()) {
                    LoggerUtils.log("JAR exists.");
                    boolean realInternetConnection = InternetUtils.checkRealInternetConnection();
                    LoggerUtils.log("Real internet connection state: " + realInternetConnection + ".");
                    if (!realInternetConnection) {
                        NotifyWindow notify = new NotifyWindow(NotifyType.ALERT);
                        notify.setTitle("Предупреждение");
                        notify.setMessage("Отсутствует подключение к интернету.\nБольшая часть функций лаунчера будет недоступна.");
                        notify.showInMainThread();
                    }

                    boolean firewallInternetConnection = false;
                    if (realInternetConnection) {
                        firewallInternetConnection = InternetUtils.checkFirewallInternetConnection();
                    }
                    LoggerUtils.log("Firewall internet connection state: " + firewallInternetConnection + ".");
                    if (realInternetConnection && !firewallInternetConnection) {
                        final String antivirus = AntivirusUtils.getAntivirus();
                        if (antivirus != null) {
                            LoggerUtils.log("Found antivirus '" + antivirus + "'.");
                            NotifyWindow notify = new NotifyWindow(NotifyType.ALERT);
                            notify.setTitle("Предупреждение");
                            notify.setMessage("Установленный антивирус «" + antivirus + "» блокирует подключение к сети.\nПожалуйста, отключите его на время запуска лаунчера или добавьте лаунчер в исключение.");
                            notify.showInMainThread();
                        } else {
                            LoggerUtils.log("Antivirus not found.");
                            NotifyWindow notify = new NotifyWindow(NotifyType.ALERT);
                            notify.setTitle("Предупреждение");
                            notify.setMessage("Проблемы с подключением к сети.\nПожалуйста, проверьте, чтобы ничего не блокировало подключение.");
                            notify.showInMainThread();
                        }
                    }

                    String md5Launcher = HashUtils.readHash("r_launcher");
                    if (md5Launcher != null) {
                        LoggerUtils.log("MD5 launcher exists.");
                        this.runLauncherChecker(md5Launcher, firewallInternetConnection);
                    } else {
                        LoggerUtils.log("MD5 launcher doesn't exists.");
                    }

                    LoggerUtils.log("Running R-Launcher...");
                    this.runLauncher(firewallInternetConnection);

                    if (!isUpdated && firewallInternetConnection) {
                        LoggerUtils.log("Running update checker...");
                        this.hide();
                        this.runLauncherUpdater();
                    } else {
                        LoggerUtils.log("The latest version of launcher is installed.");
                        this.close();
                    }
                } else {
                    LoggerUtils.log("JAR doesn't exists.");
                    this.delay();
                    LoggerUtils.log("Running launcher installer...");
                    this.runLauncherInstaller();
                }
                return null;
            }

            private final int DELAY = 1000;

            private void delay() {
                try {
                    Thread.sleep(DELAY);
                } catch (Exception ignored) {}
            }

            private void close() {
                delay();
                LoggerUtils.log("Closing R-Launcher Updater.");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mainGui.close();
                    }
                });
            }

            private void hide() {
                delay();
                LoggerUtils.log("Hiding R-Launcher Updater.");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        mainGui.hide();
                    }
                });
            }

            private void runLauncher(boolean isOnline) {
                StarterUtils.runLauncher(isOnline);
            }

            private void runLauncherInstaller() {
                installGui = new InstallGui(mainGui);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        installGui.init();
                    }
                });
            }

            private boolean isUpdated = false;

            private void runLauncherChecker(String md5Launcher, boolean firewallInternetConnection) {
                try {
                    File updateLauncher = new File(LAUNCHER_PATH.getAbsolutePath() + ".update");
                    if (updateLauncher.exists()) {
                        LoggerUtils.log("Updated launcher exists.");
                        if (HashUtils.md5(updateLauncher.getAbsolutePath()).equalsIgnoreCase(md5Launcher.split(";")[1])) {
                            boolean isDeleted = LAUNCHER_PATH.delete();
                            if (isDeleted) {
                                boolean isRenamed = updateLauncher.renameTo(LAUNCHER_PATH);
                                if (isRenamed) {
                                    isUpdated = true;
                                    LoggerUtils.log("Updated launcher.");
                                } else {
                                    LoggerUtils.log("Error renaming updated launcher.");
                                }
                            } else {
                                LoggerUtils.log("Error deleting outdated launcher.");
                            }
                            return;
                        }
                    }

                    if (firewallInternetConnection) {
                        if (!HashUtils.md5(LAUNCHER_PATH.getAbsolutePath()).equalsIgnoreCase(md5Launcher.split(";")[1])) {
                            LoggerUtils.log("Force downloading launcher...");
                            UpdaterManager updaterManager = new UpdaterManager();
                            DownloadManager.downloadFile(VENTO.WEB + updaterManager.getLauncherPath(), VENTO.LAUNCHER_PATH.getAbsolutePath());
                            HashUtils.truncHash("r_launcher");
                            HashUtils.writeHash("r_launcher", VENTO.LAUNCHER_PATH.getName() + ";" + updaterManager.getMD5(), false);
                            HashUtils.lockHash("r_launcher");
                            LoggerUtils.log("Launcher downloaded.");
                            isUpdated = true;
                        }
                    }
                } catch (Exception ignored) {}
            }

            private void runLauncherUpdater() {
                try {
                    LoggerUtils.log("Checking for updates...");
                    final UpdaterManager updaterManager = new UpdaterManager();
                    if (!HashUtils.md5(LAUNCHER_PATH.getAbsolutePath()).equalsIgnoreCase(updaterManager.getMD5())) {
                        LoggerUtils.log("Starting download of new version...");
                        DownloadManager.downloadFile(VENTO.WEB + updaterManager.getLauncherPath(), VENTO.LAUNCHER_PATH.getAbsolutePath() + ".update");
                        HashUtils.truncHash("r_launcher");
                        HashUtils.writeHash("r_launcher", VENTO.LAUNCHER_PATH.getName() + ";" + updaterManager.getMD5(), false);
                        HashUtils.lockHash("r_launcher");
                        LoggerUtils.log("New version of R-Launcher v" + updaterManager.getLauncherVersion() + " downloaded.");

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                SideNotifyWindow sideNotify = new SideNotifyWindow(NotifyType.INFO);
                                sideNotify.setTitle("Обновление R-Launcher");
                                sideNotify.setMessage("Доступна новая версия `R-Launcher v" + updaterManager.getLauncherVersion() + "`.\nПерезапустите лаунчер для обновления.");
                                sideNotify.showSideNotify();
                            }
                        });
                    } else {
                        LoggerUtils.log("The latest version of launcher is installed.");
                        System.exit(0);
                    }
                } catch (Exception e) {
                    LoggerUtils.log("Error running launcher updater.");
                    e.printStackTrace();
                }
            }
        };

        loader = new Thread(task);
        loader.start();
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                if (args[0].equalsIgnoreCase("install-launcher")) {
                    FORCE_INSTALL_LAUNCHER = true;
                } else if (args[0].equalsIgnoreCase("install-all")) {
                    FORCE_INSTALL_LAUNCHER = true;
                    FORCE_INSTALL_JVM = true;
                }
            } catch (Exception ignored) {}
        }
        launch(args);
    }
}
