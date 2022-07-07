package relevant_craft.vento.r_launcher.r_updater.gui.elements;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class R_ProgressBar extends ProgressBar {

    private R_ProgressBarText title;
    private R_ProgressBarText progress;
    private R_ProgressBarText size;
    private R_ProgressBarText speed;
    private boolean enableAnimation;

    public R_ProgressBar() {
        super();
        this.setLayoutX(20);
        this.setProgress(0D);
        this.setPrefHeight(2);
        this.getStylesheets().add(getClass().getResource("R_ProgressBar.css").toExternalForm());

        this.enableAnimation = true;

        this.title = new R_ProgressBarText(null);
        this.progress = new R_ProgressBarText("0%");
        this.size = new R_ProgressBarText(null);
        this.speed = new R_ProgressBarText(null);
    }

    public void setProgress(int percent) {
        this.progress.setText(percent + "%");

        double progress = (double) percent / 100;

        if (this.enableAnimation) {
            Animation animation = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(this.progressProperty(), progress)));
            animation.play();
        } else {
            this.setProgress(progress);
        }

        if (percent == 100) {
            hideFileData();
        }
    }

    public void setSize(String size) {
        this.size.setText(size);
    }

    public void render(double posY, Pane currentTaskBar) {
        this.setLayoutY(posY);
        this.setPrefWidth(currentTaskBar.getPrefWidth() - this.getLayoutX() * 2);
        currentTaskBar.getChildren().add(this);

        this.title.render(this, 1, currentTaskBar);
        this.progress.render(this, 2, currentTaskBar);
        this.size.render(this, 3, currentTaskBar);
        this.speed.render(this, 4, currentTaskBar);
    }

    private void hideFileData() {
        this.size.setVisible(false);
        this.speed.setVisible(false);
    }

    public void hide() {
        if (this.isVisible()) {
            this.setVisible(false);
            this.title.setVisible(false);
            this.progress.setVisible(false);
            this.size.setVisible(false);
            this.speed.setVisible(false);
        }
    }

    public void show() {
        if (!this.isVisible()) {
            this.setVisible(true);
            this.title.setVisible(true);
            this.progress.setVisible(true);
            this.size.setVisible(true);
            this.speed.setVisible(true);
        }
    }

    public void setTitle(final String titleText) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setProgress(0D);
                title.setText(titleText);
                progress.setText(null);
                size.setText(null);
                speed.setText(null);
            }
        });
    }

    public void setSpeed(String speed) {
        this.speed.setText(speed);
    }

    public void setEnableAnimation(boolean value) {
        this.enableAnimation = value;
    }
}
