package relevant_craft.vento.r_launcher.r_updater.gui.install;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.util.Duration;
import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.gui.main.MainGui;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;
import relevant_craft.vento.r_launcher.r_updater.manager.picture.PictureManager;

import java.util.ArrayList;
import java.util.List;

public class InstallGui {

    private MainGui mainGui;
    private Pane workAreaElements;
    private Pane logoBar;
    private Pane taskBar;
    private Pane buttonBar;
    private Pane logoBarAdvantages;
    private List<String> advantages;
    private Thread advantagesTask;

    private final double HEIGHT = 500;
    private final long DURATION = 400;

    public InstallGui(MainGui mainGui) {
        this.mainGui = mainGui;
    }

    public void init() {
        mainGui.getAnimArea().hide();

        mainGui.getWorkArea().setPrefHeight(HEIGHT - mainGui.getTitleBar().getPrefHeight());

        workAreaElements = new Pane();
        workAreaElements.setPrefWidth(mainGui.getLayout().getPrefWidth());
        workAreaElements.setPrefHeight(mainGui.getWorkArea().getPrefHeight());

        initLogoBar();
        initButtonBar();
        initTaskBar();

        workAreaElements.translateXProperty().set(mainGui.getLayout().getPrefWidth());
        mainGui.getWorkArea().getChildren().add(workAreaElements);

        mainGui.removeHelp();

        Animation animationHeight = new Timeline(new KeyFrame(Duration.millis(DURATION), new KeyValue(VENTO.writableHeight, HEIGHT)));
        animationHeight.play();

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        Animation animationY = new Timeline(new KeyFrame(Duration.millis(DURATION), new KeyValue(VENTO.writableY, screen.getHeight() / 2 - HEIGHT / 2)));
        animationY.play();

        Animation animation = new Timeline(new KeyFrame(Duration.millis(250), new KeyValue(workAreaElements.translateXProperty(), 0, Interpolator.EASE_IN)));
        animation.setDelay(Duration.millis(DURATION - DURATION / 4));
        animation.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainGui.getWorkArea().getChildren().remove(mainGui.getWorkAreaElements());
            }
        });
        animation.play();
    }

    private void initLogoBar() {
        logoBar = new Pane();
        logoBar.setPrefWidth(mainGui.getLayout().getPrefWidth());
        logoBar.setPrefHeight(80);
        logoBar.setStyle("-fx-background-color: #252525;");
        workAreaElements.getChildren().add(logoBar);

        Label rlauncher = new Label();
        rlauncher.setText("R-Launcher");
        rlauncher.setTextFill(Color.WHITE);
        rlauncher.setFont(FontManager.loadFont("Exo2-Regular.ttf", 40));
        final FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        rlauncher.setLayoutX(logoBar.getPrefWidth() / 2 - fontLoader.computeStringWidth(rlauncher.getText(), rlauncher.getFont()) / 2);
        rlauncher.setLayoutY(7);
        logoBar.getChildren().add(rlauncher);

        logoBarAdvantages = new Pane();
        logoBarAdvantages.setPrefWidth(logoBar.getPrefWidth());
        logoBarAdvantages.setPrefHeight(30);
        logoBarAdvantages.setLayoutY(logoBar.getPrefHeight() - logoBarAdvantages.getPrefHeight());
        logoBar.getChildren().add(logoBarAdvantages);

        advantages = new ArrayList<>();
        advantages.add("Установка модов");
        advantages.add("Установка ресурс-паков");
        advantages.add("Установка карт");
        advantages.add("Установка модпаков");
        advantages.add("Создание сборок");
        advantages.add("Поддержка скинов");
        advantages.add("Вход с лицензии");
        advantages.add("Online поддержка ВК");
        advantages.add("Приятный дизайн");

        final Image check = PictureManager.loadImage("check.png");

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    for (final String adv : advantages) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (!logoBarAdvantages.getChildren().isEmpty()) {
                                    if (logoBarAdvantages.getChildren().size() == 1) {
                                        final Label oldAdv = (Label) logoBarAdvantages.getChildren().get(0);
                                        Animation dissolve = new Timeline(new KeyFrame(Duration.millis(250), new KeyValue(oldAdv.opacityProperty(), 0)));
                                        dissolve.play();
                                        dissolve.setOnFinished(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                logoBarAdvantages.getChildren().remove(oldAdv);
                                            }
                                        });
                                    } else {
                                        logoBarAdvantages.getChildren().clear();
                                    }
                                }

                                final Label newAdv = new Label();
                                newAdv.setText(' ' + adv);
                                newAdv.setGraphic(new ImageView(check));
                                newAdv.setFont(FontManager.loadFont("Exo2-Regular.ttf", 16));
                                newAdv.setTextFill(Paint.valueOf("#bfbfbf"));
                                newAdv.setLayoutX(logoBarAdvantages.getPrefWidth());
                                newAdv.setPrefHeight(logoBarAdvantages.getPrefHeight());
                                newAdv.setAlignment(Pos.CENTER);
                                newAdv.setOpacity(0);
                                GaussianBlur blur = new GaussianBlur();
                                blur.setRadius(20);
                                newAdv.setEffect(blur);
                                logoBarAdvantages.getChildren().add(newAdv);

                                Animation animationMove = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(newAdv.layoutXProperty(), logoBarAdvantages.getPrefWidth() / 2 - fontLoader.computeStringWidth(newAdv.getText(), newAdv.getFont()) / 2)));
                                animationMove.setDelay(Duration.millis(200));
                                animationMove.play();

                                Animation animationOpacity = new Timeline(new KeyFrame(Duration.millis(150), new KeyValue(newAdv.opacityProperty(), 1)));
                                animationOpacity.setDelay(Duration.millis(200));
                                animationOpacity.play();

                                Animation animationBlur = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(blur.radiusProperty(), 0)));
                                animationBlur.setDelay(Duration.millis(200));
                                animationBlur.play();
                                animationBlur.setOnFinished(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        newAdv.setEffect(null);
                                    }
                                });
                            }
                        });

                        Thread.sleep(4000);
                    }
                }
            }
        };
        advantagesTask = new Thread(task);
        advantagesTask.start();
    }

    private void initButtonBar() {
        buttonBar = new Pane();
        buttonBar.setPrefWidth(mainGui.getLayout().getPrefWidth());
        buttonBar.setPrefHeight(50);
        buttonBar.setStyle("-fx-background-color: #252525;");
        buttonBar.setLayoutY(workAreaElements.getPrefHeight() - buttonBar.getPrefHeight());

        workAreaElements.getChildren().add(buttonBar);
    }

    public Button createAcceptButton(String caption) {
        final Button btn = new Button();
        btn.setPrefWidth(80);
        btn.setPrefHeight(30);
        btn.setText(caption);
        btn.setFont(FontManager.loadFont("Exo2-Bold.ttf", 14));
        btn.setTextFill(Color.WHITE);
        btn.setCursor(Cursor.HAND);
        btn.setStyle("-fx-background-color: #1bb556; -fx-background-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
        DropShadow shadow = new DropShadow();
        shadow.setWidth(30);
        shadow.setHeight(30);
        shadow.setSpread(0.5);
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setRadius(14.5);
        shadow.setColor(Color.valueOf("rgba(0, 0, 0, 0.25)"));
        btn.setEffect(shadow);
        btn.setLayoutX(buttonBar.getPrefWidth() - btn.getPrefWidth() - 25);
        btn.setLayoutY(buttonBar.getPrefHeight() / 2 - btn.getPrefHeight() / 2);
        btn.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setStyle("-fx-background-color: #20d164; -fx-background-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
            }
        });
        btn.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setStyle("-fx-background-color: #1bb556; -fx-background-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
            }
        });
        buttonBar.getChildren().add(btn);

        return btn;
    }

    public Button createDeclineButton(String caption) {
        final Button btn = new Button();
        btn.setPrefWidth(80);
        btn.setPrefHeight(30);
        btn.setText(caption);
        btn.setFont(FontManager.loadFont("Exo2-Bold.ttf", 14));
        btn.setTextFill(Color.WHITE);
        btn.setCursor(Cursor.HAND);
        btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 3; -fx-border-color: #ffffff; -fx-border-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
        DropShadow shadow = new DropShadow();
        shadow.setWidth(30);
        shadow.setHeight(30);
        shadow.setSpread(0.5);
        shadow.setBlurType(BlurType.GAUSSIAN);
        shadow.setRadius(14.5);
        shadow.setColor(Color.valueOf("rgba(0, 0, 0, 0.25)"));
        btn.setEffect(shadow);
        btn.setLayoutX(buttonBar.getPrefWidth() - btn.getPrefWidth() * 2 - 25 * 2);
        btn.setLayoutY(buttonBar.getPrefHeight() / 2 - btn.getPrefHeight() / 2);
        btn.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.25); -fx-background-radius: 3; -fx-border-color: #ffffff; -fx-border-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
            }
        });
        btn.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 3; -fx-border-color: #ffffff; -fx-border-radius: 3; -fx-background-insets: 0 0 0 0, 0, 0, 0;");
            }
        });
        buttonBar.getChildren().add(btn);

        return btn;
    }

    public void resetButtons() {
        buttonBar.getChildren().clear();
    }

    private void initTaskBar() {
        taskBar = new Pane();
        taskBar.setPrefWidth(mainGui.getLayout().getPrefWidth());
        taskBar.setPrefHeight(mainGui.getWorkArea().getPrefHeight() - logoBar.getPrefHeight() - buttonBar.getPrefHeight());
        taskBar.setLayoutY(logoBar.getPrefHeight());
        workAreaElements.getChildren().add(taskBar);

        WelcomeGui.initWelcomeGui(this, taskBar);
    }

    public Pane createCurrentTaskBar() {
        Pane currentTaskBar = new Pane();
        currentTaskBar.setPrefWidth(taskBar.getPrefWidth());
        currentTaskBar.setPrefHeight(taskBar.getPrefHeight());
        currentTaskBar.setStyle("-fx-background-color: #2d2d2d;");

        return currentTaskBar;
    }

    public void animateGui(Pane currentTaskBar) {
        currentTaskBar.translateXProperty().set(taskBar.getPrefWidth());
        taskBar.getChildren().add(currentTaskBar);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(250), new KeyValue(currentTaskBar.translateXProperty(), 0, Interpolator.EASE_IN)));
        timeline.play();
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                taskBar.getChildren().remove(0);
            }
        });
    }
}
