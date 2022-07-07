package relevant_craft.vento.r_launcher.r_updater.gui.notify;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_MessageText;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;
import relevant_craft.vento.r_launcher.r_updater.manager.picture.PictureManager;

public class NotifyWindow {
    private NotifyType type;
    private String title_text;
    private String message_text;
    private boolean YesOrNo = false;
    private boolean hasAnswer = false;
    private boolean answer;

    public NotifyWindow(NotifyType type) {
        this.type = type;
    }

    public void setTitle(String title_text) {
        this.title_text = title_text;
    }

    public void setMessage(String message_text) {
        this.message_text = message_text;
    }

    public void setYesOrNo(boolean YesOrNo) {
        this.YesOrNo = YesOrNo;
    }

    public boolean getAnswer() {
        return answer;
    }

    public boolean hasAnswer() {
        return hasAnswer;
    }

    public void showInMainThread() throws Exception {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showNotify();
            }
        });
        while (!hasAnswer()) { Thread.sleep(1); }
    }

    public void showNotify() {
        final Stage window = new Stage();
        window.initStyle(StageStyle.TRANSPARENT);
        window.initModality(Modality.WINDOW_MODAL);
        window.initOwner(VENTO.stage);

        AnchorPane layout = new AnchorPane();
        layout.setStyle("-fx-background-color: transparent;");
        layout.setPrefWidth(420);
        layout.setPrefHeight(250);

        final AnchorPane notify = new AnchorPane();
        notify.setPrefWidth(layout.getPrefWidth() - 30);
        notify.setPrefHeight(layout.getPrefHeight() - 30);
        notify.setStyle("-fx-background-color: #2d2d2d;");
        notify.setLayoutX(layout.getPrefWidth() / 2 - notify.getPrefWidth() / 2);
        notify.setLayoutY(layout.getPrefHeight() / 2 - notify.getPrefHeight() / 2);
        layout.getChildren().add(notify);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(13);
        shadow.setColor(Color.valueOf("rgba(0, 0, 0, 0.50)"));
        notify.setEffect(shadow);

        window.setScene(new Scene(layout, layout.getPrefWidth(), layout.getPrefHeight()));
        window.getScene().setFill(null);
        window.setX(VENTO.stage.getX() + VENTO.stage.getWidth() / 2 - layout.getPrefWidth() / 2);
        window.setY(VENTO.stage.getY() + VENTO.stage.getHeight() / 2 - layout.getPrefHeight() / 2);

        ImageView icon = new ImageView(PictureManager.loadImage(type.getName()));
        icon.setLayoutX(20);
        icon.setLayoutY(15);
        notify.getChildren().add(icon);

        Label title = new Label();
        title.setText(title_text);
        title.setLayoutX(icon.getLayoutX() + 45);
        title.setLayoutY(icon.getLayoutY() + 3);
        title.setTextFill(Color.WHITE);
        title.setFont(FontManager.loadFont("Exo2-Bold.ttf", 18));
        notify.getChildren().add(title);

        Pane messageArea = new Pane();
        messageArea.setLayoutY(icon.getLayoutY() + 40);
        messageArea.setPrefWidth(notify.getPrefWidth());
        messageArea.setPrefHeight(notify.getPrefHeight() - messageArea.getLayoutY());
        notify.getChildren().add(messageArea);

        R_MessageText info = new R_MessageText(0, notify.getPrefWidth(), messageArea);
        String[] lines = message_text.split("\n");
        for (String text : lines) {
            info.addMessage(text);
        }

        Pane buttonBar = new Pane();
        buttonBar.setPrefWidth(notify.getPrefWidth());
        buttonBar.setPrefHeight(40);
        buttonBar.setStyle("-fx-background-color: #252525;");
        buttonBar.setLayoutY(notify.getPrefHeight() - buttonBar.getPrefHeight());
        notify.getChildren().add(buttonBar);

        DropShadow buttonShadow = new DropShadow();
        buttonShadow.setWidth(30);
        buttonShadow.setHeight(30);
        buttonShadow.setSpread(0.5);
        buttonShadow.setBlurType(BlurType.GAUSSIAN);
        buttonShadow.setRadius(14.5);
        buttonShadow.setColor(Color.valueOf("rgba(0, 0, 0, 0.25)"));

        final Button confirm = new Button();
        confirm.setPrefWidth(70);
        confirm.setPrefHeight(26);
        confirm.setText(YesOrNo ? "Да" : "ОК");
        confirm.setFont(FontManager.loadFont("Exo2-Bold.ttf", 14));
        confirm.setTextFill(Color.WHITE);
        confirm.setCursor(Cursor.HAND);
        confirm.setStyle("-fx-background-color: #1bb556; -fx-background-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
        confirm.setEffect(buttonShadow);
        confirm.setLayoutX(buttonBar.getPrefWidth() - confirm.getPrefWidth() - 25);
        confirm.setLayoutY(buttonBar.getPrefHeight() / 2 - confirm.getPrefHeight() / 2);
        confirm.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                confirm.setStyle("-fx-background-color: #20d164; -fx-background-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
            }
        });
        confirm.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                confirm.setStyle("-fx-background-color: #1bb556; -fx-background-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
            }
        });
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                hasAnswer = true;
                answer = true;
                close(window, notify);
            }
        });
        buttonBar.getChildren().add(confirm);

        if (YesOrNo) {
            final Button deny = new Button();
            deny.setPrefWidth(70);
            deny.setPrefHeight(26);
            deny.setText("Нет");
            deny.setFont(FontManager.loadFont("Exo2-Bold.ttf", 14));
            deny.setTextFill(Color.WHITE);
            deny.setCursor(Cursor.HAND);
            deny.setStyle("-fx-background-color: #f44336; -fx-background-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
            deny.setEffect(buttonShadow);
            deny.setLayoutX(buttonBar.getPrefWidth() - deny.getPrefWidth() * 2 - 20 * 2);
            deny.setLayoutY(buttonBar.getPrefHeight() / 2 - deny.getPrefHeight() / 2);
            deny.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    deny.setStyle("-fx-background-color: #ff7961; -fx-background-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
                }
            });
            deny.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    deny.setStyle("-fx-background-color: #f44336; -fx-background-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
                }
            });
            deny.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    hasAnswer = true;
                    answer = false;
                    close(window, notify);
                }
            });
            buttonBar.getChildren().add(deny);
        }

        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(150));
        transition.setNode(notify);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();

        window.showAndWait();
    }

    private void close(final Stage window, AnchorPane notify) {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(150));
        transition.setNode(notify);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.play();
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                window.close();
            }
        });
    }
}
