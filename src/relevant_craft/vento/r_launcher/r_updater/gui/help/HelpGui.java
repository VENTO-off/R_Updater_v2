package relevant_craft.vento.r_launcher.r_updater.gui.help;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_MessageText;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_TextLink;
import relevant_craft.vento.r_launcher.r_updater.gui.main.MainGui;
import relevant_craft.vento.r_launcher.r_updater.gui.notify.NotifyType;
import relevant_craft.vento.r_launcher.r_updater.gui.notify.NotifyWindow;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;
import relevant_craft.vento.r_launcher.r_updater.manager.picture.PictureManager;
import relevant_craft.vento.r_launcher.r_updater.utils.OperatingSystem;

import java.io.IOException;
import java.util.ArrayList;

public class HelpGui {

    private MainGui mainGui;
    private Pane workAreaElements;

    public HelpGui(MainGui mainGui) {
        this.mainGui = mainGui;
    }

    public void init() {
        mainGui.getAnimArea().hide();

        workAreaElements = new Pane();
        workAreaElements.setPrefWidth(mainGui.getLayout().getPrefWidth());
        workAreaElements.setPrefHeight(mainGui.getWorkArea().getPrefHeight());

        mainGui.removeHelp();

        initButtons();

        mainGui.getWorkArea().getChildren().setAll(workAreaElements);
    }

    private void initButtons() {
        Button install = createHelpButton("Переустановить\nR-Launcher", "install_big.png", 35, 50);
        install.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                NotifyWindow notify = new NotifyWindow(NotifyType.QUESTION);
                notify.setTitle("Подтвердите действие");
                notify.setMessage("Вы действительно хотите `переустановить R-Launcher`?");
                notify.setYesOrNo(true);
                notify.showNotify();
                if (!notify.getAnswer()) {
                    return;
                }

                NotifyWindow notifyJava = new NotifyWindow(NotifyType.QUESTION);
                notifyJava.setTitle("Подтвердите действие");
                notifyJava.setMessage("Сейчас будет `переустановлен R-Launcher`.\nРазрешить `установку Java`? В некоторых случаях это решает большинство проблем с лаунчером.");
                notifyJava.setYesOrNo(true);
                notifyJava.showNotify();

                String arg = notifyJava.getAnswer() ? "install-all" : "install-launcher";

                ArrayList<String> args = new ArrayList<>();
                args.add("\"" + OperatingSystem.getCurrentPlatform().getJavaDir() + "\"");
                args.add("-jar");
                args.add("\"" + VENTO.UPDATER_PATH.getAbsolutePath() + "\"");
                args.add(arg);
                ProcessBuilder pb = new ProcessBuilder(args);
                try {
                    pb.start();
                } catch (Exception ignored) {}

                VENTO.mainGui.close();
            }
        });
        workAreaElements.getChildren().add(install);

        Button help = createHelpButton("Помощь по\nR-Launcher", "help_big.png", -35, 100);
        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                workAreaElements.getChildren().clear();

                Pane textArea = new Pane();
                textArea.setPrefWidth(workAreaElements.getPrefWidth());
                textArea.setPrefHeight(workAreaElements.getPrefHeight());
                workAreaElements.getChildren().add(textArea);

                R_MessageText messages = new R_MessageText(15, textArea.getPrefWidth(), textArea);
                messages.addMessage("Если у Вас есть `вопросы`, `предложения` или `проблемы`, наша команда с радостью поможет Вам.");

                R_TextLink faq = new R_TextLink("Часто задаваемые вопросы:", VENTO.FAQ_URL, workAreaElements.getPrefHeight() - 60, 14);
                faq.render(workAreaElements);
                R_TextLink manager = new R_TextLink("Задать вопрос менеджеру:", VENTO.MANAGER_URL, workAreaElements.getPrefHeight() - 40, 14);
                manager.render(workAreaElements);
            }
        });
        workAreaElements.getChildren().add(help);
    }

    private Button createHelpButton(String text, String image, double posX, int delay) {
        final Button button = new Button();
        button.setPrefWidth(120);
        button.setPrefHeight(120);
        button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.15); -fx-background-radius: 3;");
        if (posX > 0) {
            button.setLayoutX(posX);
        } else {
            button.setLayoutX(workAreaElements.getPrefWidth() - button.getPrefWidth() - Math.abs(posX));
        }
        button.setLayoutY(workAreaElements.getPrefHeight());
        button.setGraphic(new ImageView(PictureManager.loadImage(image)));
        button.setContentDisplay(ContentDisplay.TOP);
        button.setAlignment(Pos.CENTER);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setCursor(Cursor.HAND);
        button.setText("\n" + text);
        button.setTextFill(Color.WHITE);
        button.setFont(FontManager.loadFont("Exo2-Regular.ttf", 12));
        button.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.25); -fx-background-radius: 3;");
            }
        });
        button.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.15); -fx-background-radius: 3;");
            }
        });

        Animation animation = new Timeline(new KeyFrame(Duration.millis(120), new KeyValue(button.layoutYProperty(), workAreaElements.getPrefHeight() / 2 - button.getPrefHeight() / 2)));
        animation.setDelay(Duration.millis(delay));
        animation.play();

        return button;
    }
}
