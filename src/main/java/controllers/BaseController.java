package controllers;

import enums.TaskFilter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import services.*;

import java.time.LocalDate;

public abstract class BaseController {

    @FXML protected VBox taskContainer;
    @FXML protected Button addTaskButton;
    @FXML protected Label navigationLabel;
    @FXML protected Button previousDayButton;
    @FXML protected Button nextDayButton;
    @FXML protected Label taskCountLabel;
    @FXML protected Button menuButton;
    @FXML protected Button closeMenuButton;
    @FXML protected StackPane overlay;
    @FXML protected VBox sideMenu;
    @FXML protected BorderPane mainContainer;
    @FXML protected Button mainButton;
    @FXML protected Button allTasksButton;
    @FXML protected Button sortByDateButton;
    @FXML protected ComboBox<TaskFilter> filterComboBox;

    protected TaskUIService taskUIService;
    protected DialogService dialogService;
    protected DateNavigationService dateService;
    protected SideMenuService sideMenuService;
    protected NavigationService navigationService;

    protected boolean ascendingDate = true;

    protected void updateDateLabel() {
        navigationLabel.setText(dateService.getFormattedDate());
    }

    protected void openSideMenu() {
        sideMenuService.openSideMenu();
    }

    @FXML
    protected void closeSideMenu() {
        sideMenuService.closeSideMenu();
    }

    protected void handlePreviousDay() {
        dateService.previousDay();
        updateDateLabel();
        taskUIService.loadTasksForDate(dateService.getCurrentDate());
    }

    protected void handleNextDay() {
        dateService.nextDay();
        updateDateLabel();
        taskUIService.loadTasksForDate(dateService.getCurrentDate());
    }

    protected void handleSortByDate() {
        ascendingDate = !ascendingDate;
        taskUIService.loadSortedTasks(ascendingDate, filterComboBox.getValue());
    }

    protected void handleFilterTasks(TaskFilter filter) {
        taskUIService.loadSortedTasks(ascendingDate, filter);
    }

    protected void handleAddTask() {
        Pair<String, LocalDate> result = dialogService.showAddTaskDialog();
        if (result == null) return;

        String taskName = result.getKey();
        LocalDate dueDate = result.getValue();

        if (!taskName.isEmpty()) {
            taskUIService.addTask(taskName, dueDate);
        }
    }

    protected void showMainView() {
        navigationService.showMainView();
        sideMenuService.closeSideMenu();
    }

    protected void showAllTasksView() {
        navigationService.showAllTasksView();
        sideMenuService.closeSideMenu();
    }

    protected abstract void setupEventHandlers();
}
