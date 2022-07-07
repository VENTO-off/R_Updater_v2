package relevant_craft.vento.r_launcher.r_updater.gui.install;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_CheckBox;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_MessageText;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_TitleText;
import relevant_craft.vento.r_launcher.r_updater.utils.DesktopUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.LoggerUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.StarterUtils;

import java.io.File;
import java.nio.file.Files;

public class FinishGui {

    public static void initFinishGui(final InstallGui installGui, final Pane taskBar) {
        final Pane currentTaskBar = installGui.createCurrentTaskBar();

        R_TitleText titleText = new R_TitleText("Всё готово!");
        titleText.render(currentTaskBar.getPrefHeight() / 2 - 120, currentTaskBar);

        R_MessageText info = new R_MessageText(currentTaskBar.getPrefHeight() / 2 - 60, taskBar.getPrefWidth(), currentTaskBar);
        info.addMessage("`R-Launcher` успешно установлен.");
        info.addMessage("Для завершения установки нажмите кнопку «Готово».");

        final R_CheckBox addToDesktop = new R_CheckBox("Добавить лаунчер на рабочий стол");
        addToDesktop.render(3, currentTaskBar);

        final R_CheckBox runLauncher = new R_CheckBox("Запустить R-Launcher");
        runLauncher.render(2, currentTaskBar);

        final R_CheckBox subscribeVK = new R_CheckBox("Вступить в группу ВК");
        subscribeVK.unCheck();
        subscribeVK.render(1, currentTaskBar);

        installGui.resetButtons();
        installGui.animateGui(currentTaskBar);

        final Button finish = installGui.createAcceptButton("Готово");
        finish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                finish.setDisable(true);

                if (addToDesktop.isSelected()) {
                    LoggerUtils.log("Copying launcher to desktop...");
                    File desktopDirectory = new File(System.getProperty("user.home") + File.separator + "Desktop");
                    if (desktopDirectory.exists()) {
                        File desktopLauncher = new File(desktopDirectory + File.separator + VENTO.UPDATER_PATH.getName());
                        if (!desktopLauncher.exists()) {
                            try {
                                Files.copy(VENTO.UPDATER_PATH.toPath(), desktopLauncher.toPath());
                                LoggerUtils.log("Launcher coptied to desktop.");
                            } catch (Exception e) {
                                LoggerUtils.log("Error to copy launcher to desktop");
                                e.printStackTrace();
                            }
                        }
                    }
                }

                if (runLauncher.isSelected()) {
                    LoggerUtils.log("Running R-Launcher...");
                    StarterUtils.runLauncher(true);
                }

                if (subscribeVK.isSelected()) {
                    LoggerUtils.log("Opening VK URL...");
                    DesktopUtils.openUrl(VENTO.SUBSCRIBE_URL);
                }

                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(1000);
                        return null;
                    }
                };
                new Thread(task).start();

                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        VENTO.mainGui.close();
                    }
                });
            }
        });
    }
}
