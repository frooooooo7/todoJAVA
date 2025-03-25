package components;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import utils.StyleManager;

import java.time.LocalDate;
import java.util.Optional;

public class TaskDialog {

    public static Pair<String, LocalDate> showTaskDialog(String title, String header, String currentTaskName, LocalDate currentDueDate) {
        Dialog<Pair<String, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStyleClass().add("dialog-pane");
        StyleManager.applyStyles(dialogPane);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        TextField taskNameField = new TextField(currentTaskName != null ? currentTaskName : "");
        taskNameField.setPromptText("Nazwa zadania");

        DatePicker datePicker = new DatePicker(currentDueDate != null ? currentDueDate : LocalDate.now());

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Zadanie:"), 0, 0);
        grid.add(taskNameField, 1, 0);
        grid.add(new Label("Data:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(errorLabel, 1, 2);

        dialogPane.setContent(grid);

        Node okButton = dialogPane.lookupButton(okButtonType);
        okButton.setDisable(taskNameField.getText().trim().isEmpty());

        taskNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isEmpty = newValue.trim().isEmpty();
            okButton.setDisable(isEmpty);
            errorLabel.setVisible(isEmpty);
            errorLabel.setText(isEmpty ? "Nazwa zadania nie moze byc pusta!" : "");
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(taskNameField.getText().trim(), datePicker.getValue());
            }
            return null;
        });

        Optional<Pair<String, LocalDate>> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
