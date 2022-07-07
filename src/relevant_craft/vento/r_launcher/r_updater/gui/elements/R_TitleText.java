package relevant_craft.vento.r_launcher.r_updater.gui.elements;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;

public class R_TitleText extends Label {

    public R_TitleText(String titleText) {
        super();
        this.setText(titleText);
        this.setFont(FontManager.loadFont("Exo2-Bold.ttf", 25));
        this.setTextFill(Color.WHITE);
        this.setLayoutX(20);
    }

    public void doCenter(double width) {
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        this.setLayoutX(width / 2 - fontLoader.computeStringWidth(this.getText(), this.getFont()) / 2);
    }

    public void render(double posY, Pane currentTaskBar) {
        this.setLayoutY(posY);
        currentTaskBar.getChildren().add(this);
    }
}
