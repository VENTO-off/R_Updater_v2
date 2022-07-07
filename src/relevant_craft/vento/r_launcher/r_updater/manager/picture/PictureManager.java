package relevant_craft.vento.r_launcher.r_updater.manager.picture;

import javafx.scene.image.Image;

public class PictureManager {

    public static Image loadImage(String name) {
        return new Image(PictureManager.class.getResourceAsStream("pictures/" + name));
    }
}
