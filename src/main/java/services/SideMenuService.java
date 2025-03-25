package services;

import constants.Constants;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SideMenuService {
    private VBox sideMenu;
    private StackPane overlay;

    public SideMenuService(VBox sideMenu, StackPane overlay) {
        this.sideMenu = sideMenu;
        this.overlay = overlay;
    }

    public void openSideMenu() {
        overlay.setVisible(true);
        animateMenu(0);
    }

    public void closeSideMenu() {
        animateMenu(-250);
    }

    private void animateMenu(double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(Constants.MENU_ANIMATION_DURATION), sideMenu);
        transition.setToX(targetX);
        if (targetX != 0) {
            transition.setOnFinished(event -> overlay.setVisible(false));
        }
        transition.play();
    }
}
