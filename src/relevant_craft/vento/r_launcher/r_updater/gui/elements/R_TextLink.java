package relevant_craft.vento.r_launcher.r_updater.gui.elements;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;
import relevant_craft.vento.r_launcher.r_updater.utils.DesktopUtils;

public class R_TextLink {

    private Label titleText;
    private Label linkText;

    public R_TextLink(final String title, final String link, double posY) {
        this(title, link, posY, 11);
    }

    public R_TextLink(final String title, final String link, double posY, int fontSize) {
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();

        titleText = new Label();
        titleText.setText(title);
        titleText.setFont(FontManager.loadFont("Exo2-Regular.ttf", fontSize));
        titleText.setTextFill(Color.WHITE);
        titleText.setOpacity(0.4);
        titleText.setLayoutX(20);
        titleText.setLayoutY(posY);

        linkText = new Label();
        linkText.setText("открыть");
        linkText.setFont(FontManager.loadFont("Exo2-Regular.ttf", fontSize));
        linkText.setTextFill(Paint.valueOf("#4382e8"));
        linkText.setLayoutX(20 + fontLoader.computeStringWidth(titleText.getText(), titleText.getFont()) + 5);
        linkText.setLayoutY(titleText.getLayoutY());
        linkText.setCursor(Cursor.HAND);
        linkText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DesktopUtils.openUrl(link);
            }
        });
        linkText.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                linkText.setUnderline(true);
            }
        });
        linkText.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                linkText.setUnderline(false);
            }
        });
    }

    public void render(Pane currentTaskBar) {
        currentTaskBar.getChildren().add(titleText);
        currentTaskBar.getChildren().add(linkText);
    }
}
