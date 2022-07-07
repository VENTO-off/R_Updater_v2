package relevant_craft.vento.r_launcher.r_updater.utils;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtils {

    public static void onMouseEnter(Node node) {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(100));
        transition.setNode(node);
        transition.setFromValue(0.4);
        transition.setToValue(1.0);
        transition.play();
    }

    public static void onMouseExit(Node node) {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(200));
        transition.setNode(node);
        transition.setFromValue(1.0);
        transition.setToValue(0.4);
        transition.play();
    }
}
