package relevant_craft.vento.r_launcher.r_updater.gui.main;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.WritableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import relevant_craft.vento.r_launcher.r_updater.VENTO;
import relevant_craft.vento.r_launcher.r_updater.gui.help.HelpGui;
import relevant_craft.vento.r_launcher.r_updater.gui.notify.NotifyType;
import relevant_craft.vento.r_launcher.r_updater.gui.notify.NotifyWindow;
import relevant_craft.vento.r_launcher.r_updater.manager.animation.AnimArea;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;
import relevant_craft.vento.r_launcher.r_updater.manager.picture.PictureManager;
import relevant_craft.vento.r_launcher.r_updater.utils.AnimationUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.DesktopUtils;
import relevant_craft.vento.r_launcher.r_updater.utils.StarterUtils;

public class MainGui {

    private Pane layout;
    private Pane titleBar;
    private Pane workArea;
    private Pane workAreaElements;
    private ImageView logo;
    private AnimArea animArea;

    private final String TITLE = "R-Launcher Updater v" + VENTO.UPDATER_VERSION;

    private double xOffset = 0;
    private double yOffset = 0;

    public MainGui() {
        layout = new Pane();
        layout.setPrefWidth(350);
        layout.setPrefHeight(180);
        layout.setStyle("-fx-background-color: #2d2d2d;");
        layout.setOpacity(0);

        initStageHeight();

        initTitleBar();
        initWorkArea();
        initLogo();
        initLoading();

        animate();
    }

    private void initTitleBar() {
        titleBar = new Pane();
        titleBar.setPrefWidth(layout.getPrefWidth());
        titleBar.setPrefHeight(20);
        titleBar.setStyle("-fx-background-color: #252525;");
        titleBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VENTO.stage.setX(event.getScreenX() + xOffset);
                VENTO.stage.setY(event.getScreenY() + yOffset);
            }
        });
        titleBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = VENTO.stage.getX() - event.getScreenX();
                yOffset = VENTO.stage.getY() - event.getScreenY();
            }
        });
        layout.getChildren().add(titleBar);

        final Label title = new Label();
        title.setText(TITLE);
        title.setTextFill(Color.WHITE);
        title.setOpacity(0.4);
        title.setLayoutX(5);
        title.setLayoutY(0);
        title.setPrefHeight(titleBar.getPrefHeight());
        title.setAlignment(Pos.CENTER_LEFT);
        title.setCursor(Cursor.HAND);
        title.setFont(FontManager.loadFont("Exo2-Regular.ttf", 13));
        title.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AnimationUtils.onMouseEnter(title);
            }
        });
        title.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AnimationUtils.onMouseExit(title);
            }
        });
        title.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DesktopUtils.openUrl(VENTO.SITE);
            }
        });
        titleBar.getChildren().add(title);

        final Label closeButton = new Label();
        closeButton.setGraphic(new ImageView(PictureManager.loadImage("close.png")));
        closeButton.setLayoutX(titleBar.getPrefWidth() - closeButton.getFont().getSize() - 5);
        closeButton.setLayoutY(3);
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
                askClose();
            }
        });
        titleBar.getChildren().add(closeButton);

        final Label helpButton = new Label();
        helpButton.setGraphic(new ImageView(PictureManager.loadImage("help.png")));
        helpButton.setLayoutX(closeButton.getLayoutX() - helpButton.getFont().getSize() - 5);
        helpButton.setLayoutY(3);
        helpButton.setCursor(Cursor.HAND);
        helpButton.setOpacity(0.4);
        helpButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AnimationUtils.onMouseEnter(helpButton);
            }
        });
        helpButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AnimationUtils.onMouseExit(helpButton);
            }
        });
        helpButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VENTO.loader.stop();
                StarterUtils.kill();
                VENTO.helpGui = new HelpGui(VENTO.mainGui);
                VENTO.helpGui.init();
            }
        });
        titleBar.getChildren().add(helpButton);
    }

    private void initWorkArea() {
        workArea = new Pane();
        workArea.setPrefWidth(layout.getPrefWidth());
        workArea.setPrefHeight(layout.getPrefHeight() - titleBar.getPrefHeight());
        workArea.setLayoutY(titleBar.getPrefHeight());
        layout.getChildren().add(workArea);

        workAreaElements = new Pane();
        workAreaElements.setPrefWidth(workArea.getPrefWidth());
        workAreaElements.setPrefHeight(workArea.getPrefHeight() - titleBar.getPrefHeight());
        workArea.getChildren().add(workAreaElements);
    }

    private void initLogo() {
        Image image = PictureManager.loadImage("logo.png");
        logo = new ImageView(image);
        logo.setLayoutX(layout.getPrefWidth() / 2 - image.getWidth() / 2);
        logo.setLayoutY(10);
        workAreaElements.getChildren().add(logo);
    }

    private void initLoading() {
        animArea = new AnimArea();
        animArea.init(workAreaElements);
        animArea.setLayoutY(90);
        animArea.anim();
    }

    private void animate() {
        VENTO.stage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                FadeTransition transition = new FadeTransition();
                transition.setDuration(Duration.millis(300));
                transition.setNode(layout);
                transition.setFromValue(0);
                transition.setToValue(1);
                transition.play();
            }
        });
    }

    public void initStageHeight() {
        VENTO.writableHeight = new WritableValue<Double>() {
            @Override
            public Double getValue() {
                return VENTO.stage.getHeight();
            }

            @Override
            public void setValue(Double value) {
                VENTO.stage.setHeight(value);
            }
        };

        VENTO.writableY = new WritableValue<Double>() {
            @Override
            public Double getValue() {
                return VENTO.stage.getY();
            }

            @Override
            public void setValue(Double value) {
                VENTO.stage.setY(value);
            }
        };
    }

    public void close() {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(200));
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setNode(this.layout);
        transition.play();
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }

    public void hide() {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(200));
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setNode(this.layout);
        transition.play();
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                VENTO.stage.hide();
            }
        });
    }

    public void askClose() {
        if (VENTO.installGui != null) {
            NotifyWindow notify = new NotifyWindow(NotifyType.QUESTION);
            notify.setYesOrNo(true);
            notify.setTitle("Подтвердите действие");
            notify.setMessage("Установка лаунчера еще не завершена.\nВы действительно хотите прервать установку лаунчера?");
            notify.showNotify();
            if (notify.getAnswer()) {
                close();
            }
        } else {
            close();
        }
    }

    public void removeHelp() {
        titleBar.getChildren().remove(titleBar.getChildren().size() - 1);
    }

    public Pane getLayout() {
        return layout;
    }

    public Pane getTitleBar() {
        return titleBar;
    }

    public Pane getWorkArea() {
        return workArea;
    }

    public Pane getWorkAreaElements() {
        return workAreaElements;
    }

    public AnimArea getAnimArea() {
        return animArea;
    }

    public String getTitle() {
        return TITLE;
    }
}
