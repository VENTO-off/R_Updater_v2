package relevant_craft.vento.r_launcher.r_updater.gui.install;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_MessageText;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_TextLink;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_TitleText;

public class WelcomeGui {

    public static void initWelcomeGui(final InstallGui installGui, final Pane taskBar) {
        Pane currentTaskBar = installGui.createCurrentTaskBar();

        R_TitleText titleText = new R_TitleText("Добро пожаловать!");
        titleText.render(currentTaskBar.getPrefHeight() / 2 - 120, currentTaskBar);

        R_MessageText info = new R_MessageText(currentTaskBar.getPrefHeight() / 2 - 60, taskBar.getPrefWidth(), currentTaskBar);
        info.addMessage("Спасибо, что выбрали R-Launcher.");
        info.addMessage("Сейчас на Ваш компьютер будет установлен `R-Launcher`.");
        info.addMessage("Для продолжения установки нажмите кнопку «Далее».");

        R_TextLink faq = new R_TextLink("Часто задаваемые вопросы:", VENTO.FAQ_URL, currentTaskBar.getPrefHeight() - 40);
        faq.render(currentTaskBar);
        R_TextLink manager = new R_TextLink("Задать вопрос менеджеру:", VENTO.MANAGER_URL, currentTaskBar.getPrefHeight() - 25);
        manager.render(currentTaskBar);

        taskBar.getChildren().add(currentTaskBar);

        Button next = installGui.createAcceptButton("Далее");
        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CheckGui.initCheckGui(installGui, taskBar);
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

}
