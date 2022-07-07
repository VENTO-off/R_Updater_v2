package relevant_craft.vento.r_launcher.r_updater.gui.elements;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import relevant_craft.vento.r_launcher.r_updater.manager.font.FontManager;

public class R_MessageText extends VBox {

    private FlowPane line;
    private Label word;
    private boolean doHighlight;
    private boolean doParagraph;

    public R_MessageText(double posY, double width, Pane currentTaskBar) {
        this.setLayoutX(20);
        this.setLayoutY(posY);
        this.setPrefWidth(width - this.getLayoutX() * 2);
        currentTaskBar.getChildren().add(this);
        this.doParagraph = true;
    }

    public void disableParagraph() {
        this.doParagraph = false;
    }

    public void addMessage(String msg) {
        line = new FlowPane();

        doHighlight = false;
        word = new Label();

        String[] letters = msg.split("");
        for (String letter : letters) {
            if (letter.equals("«")) {
                addWord();
                doHighlight = true;
            }
            if (letter.equals("`")) {
                addWord();
                doHighlight = !doHighlight;
                continue;
            }
            if (letter.equals(" ")) {
                addWord();
                line.getChildren().add(new Text(" "));
                continue;
            }

            word.setText(word.getText() + letter);

            if (letter.equals("»")) {
                addWord();
                doHighlight = false;
            }
        }

        if (!word.getText().isEmpty()) {
            addWord();
        }

        if (doParagraph) {
            this.getChildren().addAll(line, new Text());
        } else {
            this.getChildren().add(line);
        }
    }

    private void addWord() {
        if (doHighlight) {
            word.setTextFill(Paint.valueOf("#f0f051"));
            word.setFont(FontManager.loadFont("Exo2-Bold.ttf", 16));
        } else {
            word.setTextFill(Color.WHITE);
            word.setFont(FontManager.loadFont("Exo2-Regular.ttf", 16));
        }

        line.getChildren().add(word);
        word = new Label();
    }
}
