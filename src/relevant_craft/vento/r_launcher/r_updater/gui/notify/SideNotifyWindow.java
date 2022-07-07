package relevant_craft.vento.r_launcher.r_updater.gui.notify;

import javafx.animation.*;
import javafx.beans.value.WritableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_MessageText;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;
import relevant_craft.vento.r_launcher.r_updater.manager.picture.PictureManager;
import relevant_craft.vento.r_launcher.r_updater.utils.AnimationUtils;

import java.awt.*;

public class SideNotifyWindow {
    private NotifyType type;
    private String title_text;
    private String message_text;

    private WritableValue<Double> writableX = null;

    public SideNotifyWindow(NotifyType type) {
        this.type = type;
    }

    public void setTitle(String title_text) {
        this.title_text = title_text;
    }

    public void setMessage(String message_text) {
        this.message_text = message_text;
    }

    public void showSideNotify() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Stage window = new Stage();

        window.setWidth(375);
        window.setHeight(100);
        window.setX(screenSize.width);
        window.setY(50);
        window.initStyle(StageStyle.TRANSPARENT);
        window.setTitle(VENTO.mainGui.getTitle());
        window.getIcons().setAll(PictureManager.loadImage("icon.png"));
        window.setResizable(false);

        final Pane layout = new Pane();
        layout.setPrefWidth(window.getWidth());
        layout.setPrefHeight(window.getHeight());
        layout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75);");

        initWritableX(window);

        final Label closeButton = new javafx.scene.control.Label();
        closeButton.setGraphic(new ImageView(PictureManager.loadImage("close.png")));
        closeButton.setLayoutX(layout.getPrefWidth() - closeButton.getFont().getSize() - 5);
        closeButton.setLayoutY(3);
        closeButton.setTextFill(javafx.scene.paint.Color.WHITE);
        closeButton.setCursor(Cursor.HAND);
        closeButton.setOpacity(0.4);
        closeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AnimationUtils.onMouseEnter(closeButton);
            }
        });
        closeButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AnimationUtils.onMouseExit(closeButton);
            }
        });
        closeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                close(window, layout);
            }
        });
        layout.getChildren().add(closeButton);

        Image imageIcon = PictureManager.loadImage(type.getName());
        ImageView icon = new ImageView(imageIcon);
        icon.setLayoutX(20);
        icon.setLayoutY(10);
        layout.getChildren().add(icon);

        Label title = new javafx.scene.control.Label();
        title.setText(title_text);
        title.setLayoutX(icon.getLayoutX() + 45);
        title.setLayoutY(icon.getLayoutY() + 3);
        title.setTextFill(javafx.scene.paint.Color.WHITE);
        title.setFont(FontManager.loadFont("Exo2-Bold.ttf", 18));
        layout.getChildren().add(title);

        Pane messageArea = new Pane();
        messageArea.setLayoutY(icon.getY() + imageIcon.getHeight() + 15);
        messageArea.setPrefWidth(layout.getPrefWidth());
        messageArea.setPrefHeight(layout.getPrefHeight() - messageArea.getLayoutY());
        layout.getChildren().add(messageArea);

        R_MessageText info = new R_MessageText(0, layout.getPrefWidth(), messageArea);
        info.disableParagraph();
        String[] lines = message_text.split("\n");
        for (String text : lines) {
            info.addMessage(text);
        }

        window.setScene(new Scene(layout));
        window.getScene().setFill(Color.TRANSPARENT);
        window.show();

        Animation animation = new Timeline(new KeyFrame(Duration.millis(250), new KeyValue(writableX, screenSize.getWidth() - window.getWidth() - 75)));
        animation.play();
        animation.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Animation animation2 = new Timeline(new KeyFrame(Duration.millis(50), new KeyValue(writableX, screenSize.getWidth() - window.getWidth() - 50)));
                animation2.play();
                initTimer(8, window, layout);
            }
        });
    }

    private void initWritableX(final Stage window) {
        writableX = new WritableValue<Double>() {
            @Override
            public Double getValue() {
                return window.getX();
            }

            @Override
            public void setValue(Double value) {
                window.setX(value);
            }
        };
    }

    private static void initTimer(final int seconds, final Stage window, final Pane layout) {
        final Pane progress = new Pane();
        progress.setStyle("-fx-background-color: rgba(49, 237, 68, 0.75);");
        progress.setPrefWidth(layout.getPrefWidth());
        progress.setPrefHeight(3);
        progress.setLayoutX(0);
        progress.setLayoutY(layout.getPrefHeight() - progress.getPrefHeight());
        layout.getChildren().add(progress);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (progress.getPrefWidth() > 0) {
                    double step = layout.getPrefWidth() / 100 / seconds;
                    progress.setPrefWidth(progress.getPrefWidth() - step);
                    Thread.sleep(10);
                }
                return null;
            }
        };
        new Thread(task).start();

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                close(window, layout);
            }
        });
    }

    private static void close(final Stage window, Pane layout) {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(150));
        transition.setNode(layout);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.play();
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
    }
}
