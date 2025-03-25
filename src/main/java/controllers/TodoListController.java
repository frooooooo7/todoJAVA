package controllers;

import constants.Constants;
import enums.TaskFilter;
import javafx.fxml.FXML;
import services.*;

import java.time.LocalDate;

public class TodoListController extends BaseController {

    public TodoListController() {}

    @FXML
    public void initialize() {
        dateService = new DateNavigationService();
        dialogService = new DialogService(dateService);
        taskUIService = new TaskUIService(taskContainer, dialogService, taskCountLabel, dateService);
        sideMenuService = new SideMenuService(sideMenu, overlay);
        navigationService = new NavigationService(
                taskUIService,
                dateService,
                navigationLabel,
                previousDayButton,
                nextDayButton,
                filterComboBox,
                sortByDateButton
        );

        updateDateLabel();
        navigationLabel.setText(LocalDate.now().format(Constants.NAVIGATION_DATE_FORMATTER));
        navigationService.showMainView();
        setupEventHandlers();

        filterComboBox.getItems().setAll(TaskFilter.values());
        filterComboBox.getSelectionModel().select(TaskFilter.WSZYSTKIE);

        filterComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleFilterTasks(newValue)
        );
    }

    @Override
    protected void setupEventHandlers() {
        addTaskButton.setOnAction(event -> handleAddTask());
        previousDayButton.setOnAction(event -> handlePreviousDay());
        nextDayButton.setOnAction(event -> handleNextDay());
        menuButton.setOnAction(event -> openSideMenu());
        closeMenuButton.setOnAction(event -> closeSideMenu());
        mainButton.setOnAction(event -> showMainView());
        allTasksButton.setOnAction(event -> showAllTasksView());
        sortByDateButton.setOnAction(event -> handleSortByDate());
    }
}
