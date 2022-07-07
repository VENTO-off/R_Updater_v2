package relevant_craft.vento.r_launcher.r_updater.gui.install;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_MessageText;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_ProgressBar;
import relevant_craft.vento.r_launcher.r_updater.manager.download.DownloadManager;
import relevant_craft.vento.r_launcher.r_updater.manager.java.JavaFile;
import relevant_craft.vento.r_launcher.r_updater.manager.java.JavaManager;
import relevant_craft.vento.r_launcher.r_updater.manager.task.ProgressType;
import relevant_craft.vento.r_launcher.r_updater.manager.task.R_Task;
import relevant_craft.vento.r_launcher.r_updater.manager.task.TaskManager;
import relevant_craft.vento.r_launcher.r_updater.manager.task.TaskType;
import relevant_craft.vento.r_launcher.r_updater.manager.updater.UpdaterManager;
import relevant_craft.vento.r_launcher.r_updater.utils.HashUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.LoggerUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.OperatingSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SetupGui {

    public static void initSetupGui(final InstallGui installGui, final Pane taskBar, boolean installJava) {
        final Pane currentTaskBar = installGui.createCurrentTaskBar();

        final TaskManager tasks = new TaskManager(currentTaskBar, "Установка...");
        if (installJava || VENTO.FORCE_INSTALL_JVM) {
            tasks.addTask("Установка Java...", TaskType.INSTALL_JAVA);
        }
        tasks.addTask("Установка лаунчера...", TaskType.INSTALL_LAUNCHER);

        installGui.animateGui(currentTaskBar);

        installGui.resetButtons();

        Button next = installGui.createAcceptButton("Готово");
        next.setDisable(true);
        Button cancel = installGui.createDeclineButton("Отмена");
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VENTO.mainGui.askClose();
            }
        });

        final R_MessageText info = new R_MessageText(tasks.getHeight(), taskBar.getPrefWidth(), currentTaskBar);

        final List<String> messages = new ArrayList<>();

        final R_ProgressBar progressBar = new R_ProgressBar();
        progressBar.render(tasks.getHeight(), currentTaskBar);
        progressBar.hide();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (R_Task task : tasks.getTasks()) {
                    if (task.getType() == TaskType.INSTALL_JAVA) {
                        progressBar.show();
                        progressBar.setTitle(task.getTaskName());

                        LoggerUtils.log("Getting Java files...");
                        List<JavaFile> javaFiles = JavaManager.getJavaFiles(OperatingSystem.getCurrentPlatform(), JavaManager.getRealArch());
                        if (javaFiles == null) {
                            LoggerUtils.log("Error of getting list of Java files.");
                            messages.add("Не удалось получить список файлов для Java.");
                            task.setProgress(ProgressType.WARNING);
                            nextTask();
                            continue;
                        }
                        LoggerUtils.log("Java files received.");

                        long startTime = System.currentTimeMillis();
                        long downloaded = 0;
                        long size = JavaManager.getJavaFilesSize(javaFiles);
                        for (JavaFile jf : javaFiles) {
                            File file = new File(VENTO.LAUNCHER_DIR + File.separator + jf.getUrl());
                            String url = VENTO.REPOSITORY + jf.getUrl();
                            try {
                                LoggerUtils.log("Starting to download: " + jf.getUrl());
                                DownloadManager.downloadFile(url, file.getAbsolutePath(), progressBar, downloaded, size, startTime);
                                downloaded += jf.getSize();
                            } catch (Exception e) {
                                LoggerUtils.log("Error installing Java.");
                                e.printStackTrace();

                                messages.add("Не удалось установить Java.");
                                task.setProgress(ProgressType.WARNING);
                                nextTask();
                                break;
                            }
                        }

                        if (task.getProgressType() == ProgressType.RUNNING) {
                            LoggerUtils.log("Creating hash for JVM...");
                            HashUtils.truncHash("jvm");
                            for (JavaFile jf : javaFiles) {
                                File file = new File(VENTO.LAUNCHER_DIR + File.separator + jf.getUrl());
                                HashUtils.writeHash("jvm", jf.getUrl() + ";" + file.length(), true);
                            }
                            HashUtils.lockHash("jvm");
                            LoggerUtils.log("Hash for JVM created.");
                        }

                    } else if (task.getType() == TaskType.INSTALL_LAUNCHER) {
                        progressBar.show();
                        progressBar.setTitle(task.getTaskName());
                        progressBar.setEnableAnimation(false);

                        long startTime = System.currentTimeMillis();
                        UpdaterManager updaterManager;
                        LoggerUtils.log("Getting launcher info...");
                        try {
                            updaterManager = new UpdaterManager();
                            LoggerUtils.log("Starting to download: " + updaterManager.getLauncherPath());
                            DownloadManager.downloadFile(VENTO.WEB + updaterManager.getLauncherPath(), VENTO.LAUNCHER_PATH.getAbsolutePath(), progressBar, 0, 0, startTime);
                        } catch (Exception e) {
                            LoggerUtils.log("Error downloading launcher.");
                            e.printStackTrace();

                            messages.add("Не удалось скачать лаунчер. Попробуйте отключить антивирус и/или запустить установщик от имени администратора.");
                            task.setProgress(ProgressType.WARNING);
                            nextTask();
                            continue;
                        }

                        LoggerUtils.log("Creating hash for launcher.");
                        HashUtils.truncHash("r_launcher");
                        HashUtils.writeHash("r_launcher", VENTO.LAUNCHER_PATH.getName() + ";" + updaterManager.getMD5(), false);
                        HashUtils.lockHash("r_launcher");
                        LoggerUtils.log("Hash for launcher created.");
                    }

                    task.setProgress(ProgressType.SUCCESSFUL);
                    nextTask();
                }
                return null;
            }

            private void nextTask() throws Exception {
                progressBar.hide();
                Thread.sleep(250);
            }
        };
        new Thread(task).start();

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                tasks.removeEllipsis();

                R_Task launcherTask = tasks.getTask(TaskType.INSTALL_LAUNCHER);
                if (launcherTask != null && launcherTask.getProgressType() == ProgressType.SUCCESSFUL) {
                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            Thread.sleep(2000);
                            return null;
                        }
                    };
                    new Thread(task).start();
                    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            FinishGui.initFinishGui(installGui, taskBar);
                        }
                    });
                } else {
                    installGui.resetButtons();

                    Button finish = installGui.createAcceptButton("Закрыть");
                    finish.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            VENTO.mainGui.askClose();
                        }
                    });
                }

                for (String msg : messages) {
                    info.addMessage(msg);
                }
            }
        });
    }

}
