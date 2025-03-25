package main;

import db.Db;
import exceptions.DatabaseException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.StylesheetLoader;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static constants.Constants.CSS_FILES;

public class TodoApplication extends Application {

    private static final Logger LOGGER = Logger.getLogger(TodoApplication.class.getName());
    private final Db db = new Db();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = loadFXML("/views/app.fxml");
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            StylesheetLoader.loadStylesheets(scene, CSS_FILES);

            stage.setTitle("To-do List");
            stage.setScene(scene);
            stage.show();

            db.getConnection();
            stage.setOnCloseRequest(event -> {
                try {
                    db.closeConnection();
                } catch (DatabaseException e) {
                    LOGGER.log(Level.SEVERE, "Błąd przy zamykaniu bazy danych", e);
                }
            });

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Blad podczas ladowania pliku FXML", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Wystapil nieoczekiwany blad", e);
        }
    }

    private FXMLLoader loadFXML(String fxmlPath) throws IOException {
        URL fxmlLocation = getClass().getResource(fxmlPath);
        if (fxmlLocation == null) {
            throw new IOException("Plik FXML nie zostal znaleziony: " + fxmlPath);
        }
        return new FXMLLoader(fxmlLocation);
    }

    public static void main(String[] args) {
        try {
            launch();
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Wystapil blad podczas uruchamiania aplikacji", e);
        }
    }
}
