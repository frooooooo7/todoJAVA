package utils;

import javafx.scene.control.DialogPane;

public class StyleManager {
    private static final String CSS_PATH = "/views/dialog.css";

    public static String getStylesheet() {
        try {
            return StyleManager.class.getResource(CSS_PATH).toExternalForm();
        } catch (NullPointerException e) {
            System.err.println("Nie znaleziono pliku CSS " + CSS_PATH);
            return "";
        }
    }

    public static void applyStyles(DialogPane dialogPane) {
        dialogPane.getStylesheets().add(getStylesheet());
        dialogPane.getStyleClass().add("dialog-pane");
    }
}