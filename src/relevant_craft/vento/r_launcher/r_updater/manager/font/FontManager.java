package relevant_craft.vento.r_launcher.r_updater.manager.font;

import javafx.scene.text.Font;

public class FontManager {

    public static Font loadFont(String name, int size) {
        return Font.loadFont(FontManager.class.getResourceAsStream("fonts/" + name), size);
    }

}
