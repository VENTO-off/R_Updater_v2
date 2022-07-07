package relevant_craft.vento.r_launcher.r_updater.gui.elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;

public class R_ProgressBarText extends Label {

    private final int PADDING_TOP = 18;
    private final int PADDING_BOTTOM = 8;

    public R_ProgressBarText(String titleText) {
        super();
        this.setText(titleText);
        this.setFont(FontManager.loadFont("Exo2-Regular.ttf", 12));
        this.setTextFill(Color.WHITE);
        this.setOpacity(0.5);
    }

    private void alignLeft() {
        this.setAlignment(Pos.CENTER_LEFT);
    }

    private void alignRight() {
        this.setAlignment(Pos.CENTER_RIGHT);
    }

    public void render(R_ProgressBar progressBar, int position, Pane currentTaskBar) {
        this.setPrefWidth(progressBar.getPrefWidth() / 2);
        if (position == 1) {
            this.alignLeft();
            this.setLayoutX(progressBar.getLayoutX());
            this.setLayoutY(progressBar.getLayoutY() - PADDING_TOP);
        } else if (position == 2) {
            this.alignRight();
            this.setLayoutX(progressBar.getLayoutX() + progressBar.getPrefWidth() / 2);
            this.setLayoutY(progressBar.getLayoutY() - PADDING_TOP);
        } else if (position == 3) {
            this.alignLeft();
            this.setLayoutX(progressBar.getLayoutX());
            this.setLayoutY(progressBar.getLayoutY() + PADDING_BOTTOM);
        } else if (position == 4) {
            this.alignRight();
            this.setLayoutX(progressBar.getLayoutX() + progressBar.getPrefWidth() / 2);
            this.setLayoutY(progressBar.getLayoutY() + PADDING_BOTTOM);
        }
        currentTaskBar.getChildren().add(this);
    }
}
