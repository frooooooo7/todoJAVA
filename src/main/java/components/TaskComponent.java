package components;

import constants.Constants;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Pair;
import models.Task;
import services.DialogService;
import services.TaskUIService;

import java.time.LocalDate;

public class TaskComponent extends HBox {
    private Task task;
    private final Label taskLabel;
    private final Label dateLabel;
    private final DialogService dialogService;
    private final TaskUIService taskUIService;
    private final CheckBox checkBox;

    public TaskComponent(Task task, DialogService dialogService, VBox taskContainer, TaskUIService taskUIService,
            boolean showDate) {
        super(10);
        this.task = task;
        this.dialogService = dialogService;
        this.taskUIService = taskUIService;

        getStyleClass().add("task-item");
        setAlignment(Pos.CENTER_LEFT);

        checkBox = new CheckBox();

        HBox contentContainer = new HBox(10);
        contentContainer.setAlignment(Pos.CENTER_LEFT);

        taskLabel = new Label(task.getName());
        taskLabel.getStyleClass().add("task-title");

        dateLabel = new Label();
        dateLabel.getStyleClass().add("task-date");
        updateDateLabel();
        dateLabel.setVisible(showDate);

        contentContainer.getChildren().addAll(taskLabel, dateLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editButton = new Button("Edytuj");
        Button deleteButton = new Button("Usuń");

        editButton.getStyleClass().add("edit-button");
        deleteButton.getStyleClass().add("delete-button");

        getChildren().addAll(checkBox, contentContainer, spacer, editButton, deleteButton);

        if (task.isCompleted()) {
            getStyleClass().add("task-completed");
            checkBox.setSelected(true);
        }

        editButton.setOnAction(event -> handleEditTask(taskContainer));
        deleteButton.setOnAction(event -> handleDeleteTask());
        checkBox.setOnAction(event -> handleToggleTaskCompletion());
    }

    public Task getTask() {
        return task;
    }

    public void updateTask(Task task) {
        this.task = task;
        taskLabel.setText(task.getName());
        updateDateLabel();

        if (task.isCompleted() != checkBox.isSelected()) {
            checkBox.setSelected(task.isCompleted());
        }

        updateTaskStyle();
    }

    private void updateDateLabel() {
        dateLabel.setText(task.getDueDate() != null ? task.getDueDate().format(Constants.DATE_FORMATTER) : "");
    }

    private void updateTaskStyle() {
        getStyleClass().remove("task-completed");
        if (task.isCompleted()) {
            getStyleClass().add("task-completed");
        }
    }

    private void handleEditTask(VBox taskContainer) {
        Pair<String, LocalDate> result = dialogService.showEditTaskDialog(task.getName(), task.getDueDate());
        if (result == null)
            return;

        String editedTaskName = result.getKey();
        LocalDate editedDueDate = result.getValue();

        if (editedTaskName != null && !editedTaskName.isEmpty() &&
                (!editedTaskName.equals(task.getName()) || !editedDueDate.equals(task.getDueDate()))) {

            task.setName(editedTaskName);
            task.setDueDate(editedDueDate);
            taskUIService.updateTask(task);

            if (!task.getDueDate().equals(editedDueDate)) {
                taskContainer.getChildren().remove(this);
                taskUIService.loadTasksForDate(taskUIService.getCurrentDate());
            } else {
                updateTask(task);
            }
        }
    }

    private void handleDeleteTask() {
        if (dialogService.showDeleteConfirmationDialog()) {
            taskUIService.deleteTask(task.getId());
        }
    }

    private void handleToggleTaskCompletion() {
        task.setCompleted(checkBox.isSelected());
        updateTaskStyle();
        taskUIService.updateTask(task);
    }
}
