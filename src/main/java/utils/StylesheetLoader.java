package utils;

import javafx.scene.Scene;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StylesheetLoader {

    private static final Logger LOGGER = Logger.getLogger(StylesheetLoader.class.getName());

    public static void loadStylesheets(Scene scene, List<String> cssFiles) {
        for (String cssFile : cssFiles) {
            try {
                scene.getStylesheets().add(StylesheetLoader.class.getResource(cssFile).toExternalForm());
            } catch (NullPointerException e) {
                LOGGER.log(Level.WARNING, "Nie znaleziono pliku CSS: " + cssFile, e);
            }
        }
    }
}
