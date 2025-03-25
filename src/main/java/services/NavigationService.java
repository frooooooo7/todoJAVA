package services;

import enums.TaskFilter;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import constants.Constants;

import java.time.LocalDate;

public class NavigationService {
    private final TaskUIService taskUIService;
    private final DateNavigationService dateService;
    private final Label navigationLabel;
    private final Button previousDayButton;
    private final Button nextDayButton;
    private final ComboBox<TaskFilter> filterComboBox;
    private final Button sortByDateButton;

    public NavigationService(TaskUIService taskUIService, DateNavigationService dateService, Label navigationLabel,
                             Button previousDayButton, Button nextDayButton, ComboBox<TaskFilter> filterComboBox, Button sortByDateButton) {
        this.taskUIService = taskUIService;
        this.dateService = dateService;
        this.navigationLabel = navigationLabel;
        this.previousDayButton = previousDayButton;
        this.nextDayButton = nextDayButton;
        this.filterComboBox = filterComboBox;
        this.sortByDateButton = sortByDateButton;
    }

    public void showMainView() {
        dateService.setCurrentDate(LocalDate.now());
        taskUIService.setCurrentViewMain();
        navigationLabel.setText(dateService.getFormattedDate());

        previousDayButton.setVisible(true);
        previousDayButton.setManaged(true);
        nextDayButton.setVisible(true);
        nextDayButton.setManaged(true);

        filterComboBox.setVisible(false);
        filterComboBox.setManaged(false);
        sortByDateButton.setVisible(false);
        sortByDateButton.setManaged(false);
    }

    public void showAllTasksView() {
        taskUIService.setCurrentViewAll();
        navigationLabel.setText(Constants.ALL_TASKS_LABEL);

        previousDayButton.setVisible(false);
        previousDayButton.setManaged(false);
        nextDayButton.setVisible(false);
        nextDayButton.setManaged(false);

        filterComboBox.setVisible(true);
        filterComboBox.setManaged(true);
        sortByDateButton.setVisible(true);
        sortByDateButton.setManaged(true);
    }
}
