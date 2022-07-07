package relevant_craft.vento.r_launcher.r_updater.manager.task;

import javafx.scene.layout.Pane;
import relevant_craft.vento.r_launcher.r_updater.gui.elements.R_TitleText;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private Pane currentTaskBar;
    private R_TitleText titleText;
    private List<R_Task> tasks = new ArrayList<>();
    private int posY;
    private final int SPACE = 30;

    public TaskManager(Pane currentTaskBar, String titleTask) {
        this.currentTaskBar = currentTaskBar;

        this.titleText = new R_TitleText(titleTask);
        this.titleText.doCenter(currentTaskBar.getPrefWidth());
        this.titleText.render(20, currentTaskBar);

        posY = (int) this.titleText.getLayoutY() + 60;
    }

    public void addTask(String taskName, TaskType id) {
        tasks.add(new R_Task(currentTaskBar, posY, taskName, id));
        posY += SPACE;
    }

    public List<R_Task> getTasks() {
        return tasks;
    }

    public R_Task getTask(TaskType id) {
        for (R_Task task : tasks) {
            if (task.getType() == id) {
                return task;
            }
        }

        return null;
    }

    public int getHeight() {
        return posY + SPACE;
    }

    public void removeEllipsis() {
        if (titleText.getText() == null) {
            return;
        }
        if (titleText.getText().length() < 3) {
            return;
        }
        if (!titleText.getText().endsWith("...")) {
            return;
        }

        titleText.setText(titleText.getText().substring(0, titleText.getText().length() - 3));
    }
}
