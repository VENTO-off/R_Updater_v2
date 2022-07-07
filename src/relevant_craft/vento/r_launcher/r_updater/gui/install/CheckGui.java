package relevant_craft.vento.r_launcher.r_updater.gui.install;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_MessageText;
import relevant_craft.vento.r_launcher.r_updater.manager.java.JavaManager;
import relevant_craft.vento.r_launcher.r_updater.manager.task.ProgressType;
import relevant_craft.vento.r_launcher.r_updater.manager.task.R_Task;
import relevant_craft.vento.r_launcher.r_updater.manager.task.TaskManager;
import relevant_craft.vento.r_launcher.r_updater.manager.task.TaskType;
import relevant_craft.vento.r_launcher.r_updater.utils.AntivirusUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.InternetUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

public class CheckGui {

    public static void initCheckGui(final InstallGui installGui, final Pane taskBar) {
        final Pane currentTaskBar = installGui.createCurrentTaskBar();

        final TaskManager tasks = new TaskManager(currentTaskBar, "Подготовка...");
        tasks.addTask("Проверка подключения к интернету...", TaskType.CHECK_INTERNET);
        tasks.addTask("Проверка установленной Java...", TaskType.CHECK_JAVA);

        installGui.animateGui(currentTaskBar);

        installGui.resetButtons();
        Button next = installGui.createAcceptButton("Далее");
        next.setDisable(true);
        final Button cancel = installGui.createDeclineButton("Отмена");
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VENTO.mainGui.askClose();
            }
        });

        final R_MessageText info = new R_MessageText(tasks.getHeight(), taskBar.getPrefWidth(), currentTaskBar);

        final List<String> messages = new ArrayList<>();

        final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(500);

                for (R_Task task : tasks.getTasks()) {
                    if (task.getType() == TaskType.CHECK_INTERNET) {
                        boolean realInternetConnection = InternetUtils.checkRealInternetConnection();
                        LoggerUtils.log("Real internet connection state: " + realInternetConnection + ".");
                        if (!realInternetConnection) {
                            messages.add("Нет подключения к интернету. Пожалуйста, проверьте подключение к сети. Возможно, антивирус блокирует подключение.");
                            task.setProgress(ProgressType.WARNING);
                            nextTask();
                            continue;
                        }

                        boolean firewallInternetConnection = InternetUtils.checkFirewallInternetConnection();
                        LoggerUtils.log("Firewall internet connection state: " + firewallInternetConnection + ".");
                        if (!firewallInternetConnection) {
                            String antivirus = AntivirusUtils.getAntivirus();

                            if (antivirus != null) {
                                LoggerUtils.log("Found antivirus '" + antivirus + "'.");
                                messages.add("Установленный антивирус «" + antivirus + "» блокирует подключение к сети. Пожалуйста, отключите его временно.");
                            } else {
                                LoggerUtils.log("Antivirus not found.");
                                messages.add("Проблемы с подключением к сети. Возможно, антивирус блокирует подключение.");
                            }

                            task.setProgress(ProgressType.WARNING);
                            nextTask();
                            continue;
                        }
                    } else if (task.getType() == TaskType.CHECK_JAVA) {
                        if (!JavaManager.isJavaValid()) {
                            LoggerUtils.log("System Java isn't valid.");
                            R_Task internetTask = tasks.getTask(TaskType.CHECK_INTERNET);
                            if (internetTask != null && internetTask.getProgressType() == ProgressType.SUCCESSFUL) {
                                messages.add("Java будет переустановлена, т.к. она установлена неправильно.");
                            } else {
                                messages.add("Java будет переустановлена.");
                            }
                            task.setProgress(ProgressType.WARNING);
                            continue;
                        } else {
                            LoggerUtils.log("System Java valid.");
                        }
                    }

                    task.setProgress(ProgressType.SUCCESSFUL);
                    nextTask();
                }

                return null;
            }

            private void nextTask() throws Exception {
                Thread.sleep(250);
            }
        };
        new Thread(task).start();

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                tasks.removeEllipsis();
                installGui.resetButtons();

                R_Task internetTask = tasks.getTask(TaskType.CHECK_INTERNET);
                if (internetTask != null && internetTask.getProgressType() == ProgressType.SUCCESSFUL) {
                    messages.add("Для начала установки нажмите кнопку «Далее».");

                    Button next = installGui.createAcceptButton("Далее");
                    next.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            R_Task javaTask = tasks.getTask(TaskType.CHECK_JAVA);
                            SetupGui.initSetupGui(installGui, taskBar, javaTask.getProgressType() == ProgressType.WARNING);
                        }
                    });

                    Button cancel = installGui.createDeclineButton("Отмена");
                    cancel.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            VENTO.mainGui.askClose();
                        }
                    });
                } else if (internetTask != null && internetTask.getProgressType() == ProgressType.WARNING) {
                    messages.add("Исправьте проблемы с интернетом и нажмите кнопку «Повтор».");

                    Button repeat = installGui.createAcceptButton("Повтор");
                    repeat.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            initCheckGui(installGui, taskBar);
                        }
                    });

                    Button cancel = installGui.createDeclineButton("Отмена");
                    cancel.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
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
