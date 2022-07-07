package relevant_craft.vento.r_launcher.r_updater.manager.task;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import relevant_craft.vento.r_launcher.r_updater.manager.animation.SpriteAnimation;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;
import relevant_craft.vento.r_launcher.r_updater.manager.picture.PictureManager;

public class R_Task {
    private Pane currentTaskBar;
    private Label task;
    private ImageView progress;
    private SpriteAnimation animation;
    private TaskType taskType;
    private ProgressType progressType;

    public R_Task(Pane currentTaskBar, int posY, String taskName, TaskType taskType) {
        this.currentTaskBar = currentTaskBar;
        this.taskType = taskType;
        this.animation = new SpriteAnimation(ProgressType.RUNNING.getName(), Duration.millis(1000), 60, 12, 0, 0, 20, 20);
        this.progressType = ProgressType.RUNNING;

        progress = animation.getImageView();
        progress.setLayoutX(20);
        progress.setLayoutY(posY);
        currentTaskBar.getChildren().add(progress);

        task = new Label();
        task.setText(taskName);
        task.setFont(FontManager.loadFont("Exo2-Regular.ttf", 14));
        task.setTextFill(Color.WHITE);
        task.setLayoutX(progress.getLayoutX() + 30);
        task.setLayoutY(progress.getLayoutY() + 1);
        currentTaskBar.getChildren().add(task);
    }

    public void setTaskName(String taskName) {
        task.setText(taskName);
    }

    public void setProgress(final ProgressType type) {
        if (animation != null && animation.getStatus() == Animation.Status.RUNNING) {
            animation.stop();
            animation = null;
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                currentTaskBar.getChildren().remove(progress);
                progress = new ImageView(PictureManager.loadImage(type.getName()));
                progress.setLayoutX(task.getLayoutX() - 25);
                progress.setLayoutY(task.getLayoutY() + 1);
                currentTaskBar.getChildren().add(progress);

                progressType = type;

                removeEllipsis();
            }
        });
    }

    private void removeEllipsis() {
        if (task.getText() == null) {
            return;
        }
        if (task.getText().length() < 3) {
            return;
        }
        if (!task.getText().endsWith("...")) {
            return;
        }

        task.setText(task.getText().substring(0, task.getText().length() - 3));
    }

    public TaskType getType() {
        return taskType;
    }

    public ProgressType getProgressType() {
        return progressType;
    }

    public String getTaskName() {
        return this.task.getText();
    }
}
