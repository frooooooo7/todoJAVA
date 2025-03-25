package services;

import components.TaskDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Pair;
import utils.StyleManager;

import java.time.LocalDate;

public class DialogService {

    private final DateNavigationService dateService;

    public DialogService(DateNavigationService dateService) {
        this.dateService = dateService;
    }

    public Pair<String, LocalDate> showAddTaskDialog() {
        return TaskDialog.showTaskDialog(
                "Dodaj zadanie",
                "Wpisz nazwę zadania i wybierz datę",
                null,
                dateService.getCurrentDate()
        );
    }

    public Pair<String, LocalDate> showEditTaskDialog(String currentTaskName, LocalDate currentDueDate) {
        return TaskDialog.showTaskDialog(
                "Edytuj zadanie",
                "Edytuj nazwę zadania i datę",
                currentTaskName,
                currentDueDate
        );
    }

    public boolean showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        StyleManager.applyStyles(alert.getDialogPane());
        alert.setTitle("Usuń zadanie");
        alert.setHeaderText("Czy na pewno chcesz usunąć to zadanie?");

        return alert.showAndWait().map(response -> response == ButtonType.OK).orElse(false);
    }
}