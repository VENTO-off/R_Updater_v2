package relevant_craft.vento.r_launcher.r_updater.manager.animation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class AnimElement {

    private Pane line;
    private Animation animationPos = null;
    private Animation animationHeight = null;

    private final int SPACE = 7;
    private final int SIZE = 10;
    private final int SPEED = 225;

    protected AnimElement(String color) {
        line = new Pane();
        line.setPrefWidth(SIZE);
        line.setPrefHeight(SIZE);
        line.setStyle("-fx-background-radius: 50; -fx-background-color: " + color);
    }

    protected void render(int index, int amount, Pane layout) {
        double start_x = (layout.getPrefWidth() - (amount * (SIZE + SPACE))) / 2;
        line.setLayoutX(start_x + (index * (SIZE + SPACE)));
        line.setLayoutY(layout.getPrefHeight() / 2 - line.getPrefHeight() / 2);
        layout.getChildren().add(line);
    }

    protected void animate(double height) {
        if (line.getPrefHeight() == height) {
            return;
        }

        if (height == 0) {
            height = SIZE;
        }

        if (animationPos != null && animationPos.getStatus() == Animation.Status.RUNNING) {
            animationPos.stop();
        }
        animationPos = new Timeline(new KeyFrame(Duration.millis(SPEED), new KeyValue(line.layoutYProperty(), line.getLayoutY() + line.getPrefHeight() / 2 - height / 2)));
        animationPos.play();

        if (animationHeight != null && animationHeight.getStatus() == Animation.Status.RUNNING) {
            animationHeight.stop();
        }
        animationHeight = new Timeline(new KeyFrame(Duration.millis(SPEED), new KeyValue(line.prefHeightProperty(), height)));
        animationHeight.play();
    }

    protected int getSpeed() {
        return SPEED;
    }
}
