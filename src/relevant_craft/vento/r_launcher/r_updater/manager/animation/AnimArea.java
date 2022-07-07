package relevant_craft.vento.r_launcher.r_updater.manager.animation;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AnimArea {

    private Pane layout;
    private List<AnimElement> elements = new ArrayList<>();
    private Thread animationTask = null;

    public AnimArea() {
        layout = new Pane();

        elements.add(new AnimElement("#ec2d73"));   //pink
        elements.add(new AnimElement("#eb5324"));   //red
        elements.add(new AnimElement("#fdc800"));   //yellow
        elements.add(new AnimElement("#47b264"));   //green
        elements.add(new AnimElement("#1470bd"));   //blue
        elements.add(new AnimElement("#76469a"));   //purple
        elements.add(new AnimElement("#ec2d73"));   //pink
        elements.add(new AnimElement("#eb5324"));   //red
        elements.add(new AnimElement("#fdc800"));   //yellow
    }

    public void init(Pane parent) {
        layout.setPrefWidth(parent.getPrefWidth());
        layout.setPrefHeight(75);
        layout.setLayoutX(0);
        layout.setStyle("-fx-background-color: transparent;");

        int index = 0;
        for (AnimElement element : elements) {
            element.render(index, elements.size(), layout);
            index++;
        }

        parent.getChildren().add(layout);
    }

    public void hide() {
        FadeTransition transition = new FadeTransition();
        transition.setNode(layout);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setDuration(Duration.millis(200));
        transition.play();
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (animationTask != null && animationTask.isAlive()) {
                    animationTask.stop();
                    animationTask = null;
                }

                layout.setVisible(false);
                layout.getChildren().clear();
                layout = null;
            }
        });
    }

    public void setLayoutY(double Y) {
        layout.setLayoutY(Y);
    }

    public void anim() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int center = -1;
                while (true) {
                    int index = 0;
                    int speed = 0;

                    for (final AnimElement element : elements) {
                        speed = element.getSpeed() - 25;
                        int prev = center - 1;
                        int next = center + 1;

                        if (prev >= 0 && prev == index) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    element.animate(25);
                                }
                            });
                        } else if (center >= 0 && center == index) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    element.animate(40);
                                }
                            });
                        } else if (next >= 0 && next == index) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    element.animate(25);
                                }
                            });
                        } else {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    element.animate(0);
                                }
                            });
                        }
                        index++;
                    }

                    center++;
                    if (center > elements.size()) {
                        center = -1;
                    }
                    Thread.sleep(speed);
                }
            }
        };
        animationTask = new Thread(task);
        animationTask.start();
    }
}
