package relevant_craft.vento.r_launcher.r_updater.gui.elements;

import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;

public class R_CheckBox extends CheckBox {

    public R_CheckBox(String text) {
        super();
        this.setSelected(true);
        this.setText(text);
        this.setCursor(Cursor.HAND);
        this.setLayoutX(20);
        this.setTextFill(Color.WHITE);
        this.setFont(FontManager.loadFont("Exo2-Regular.ttf", 14));
        this.getStylesheets().add(getClass().getResource("R_CheckBox.css").toExternalForm());
        this.setFocusTraversable(false);
    }

    public void unCheck() {
        this.setSelected(false);
    }

    public void render(int position, Pane currentTaskBar) {
        this.setLayoutY(currentTaskBar.getPrefHeight() - 20 - position * 25);
        currentTaskBar.getChildren().add(this);
    }
}
