package services;

import components.TaskComponent;
import enums.TaskFilter;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import enums.ViewMode;


public class TaskUIService {
    private final VBox taskContainer;
    private final DialogService dialogService;
    private final Label taskCountLabel;
    private final TaskService taskService;
    private final DateNavigationService dateService;


    private ViewMode currentView = ViewMode.MAIN_VIEW;
    private TaskFilter currentFilter = TaskFilter.WSZYSTKIE;
    private boolean currentSortAscending = true;

    public TaskUIService(VBox taskContainer, DialogService dialogService, Label taskCountLabel, DateNavigationService dateService) {
        this.taskContainer = taskContainer;
        this.dialogService = dialogService;
        this.taskCountLabel = taskCountLabel;
        this.dateService = dateService;
        this.taskService = new TaskService();
    }

    public void setCurrentViewMain() {
        currentView = ViewMode.MAIN_VIEW;
        loadTasksForDate(getCurrentDate());
    }

    public void setCurrentViewAll() {
        currentView = ViewMode.ALL_TASKS_VIEW;
        loadSortedTasks(currentSortAscending, currentFilter);
    }

    public void addTask(String taskName, LocalDate dueDate) {
        Task task = new Task(taskName, dueDate);
        taskService.addTask(task);

        if (dueDate.equals(getCurrentDate()) && currentView == ViewMode.MAIN_VIEW) {
            taskContainer.getChildren().add(createTaskComponent(task, false));
        } else {
            refresh();
        }

        updateTaskCountLabel();
    }

    public void loadTasksForDate(LocalDate date) {
        loadTasks(task -> task.getDueDate().equals(date), false);
    }

    public void deleteTask(int taskId) {
        taskService.deleteTask(taskId);
        taskContainer.getChildren().removeIf(node ->
                node instanceof TaskComponent && ((TaskComponent) node).getTask().getId() == taskId);
        updateTaskCountLabel();
    }

    public void updateTask(Task task) {
        taskService.updateTask(task);
        refresh();
    }

    public void refresh() {
        if (currentView == ViewMode.MAIN_VIEW) {
            loadTasksForDate(getCurrentDate());
        } else {
            loadSortedTasks(currentSortAscending, currentFilter);
        }
    }

    public void loadSortedTasks(boolean ascendingDate, TaskFilter filter) {
        currentSortAscending = ascendingDate;
        currentFilter = filter;

        List<Task> tasks = taskService.getTasksSorted(ascendingDate, filter);
        displayTasks(tasks, true);
    }

    private void loadTasks(Predicate<Task> filter, boolean showDate) {
        List<Task> tasks = taskService.getTasks(filter);
        displayTasks(tasks, showDate);
    }

    private void displayTasks(List<Task> tasks, boolean showDate) {
        taskContainer.getChildren().setAll(
                tasks.stream()
                        .map(task -> createTaskComponent(task, showDate))
                        .collect(Collectors.toList())
        );
        updateTaskCountLabel();
    }

    private void updateTaskCountLabel() {
        int taskCount = taskContainer.getChildren().size();
        String taskText = switch (taskCount) {
            case 1 -> "zadanie";
            case 2, 3, 4 -> "zadania";
            default -> "zadań";
        };
        taskCountLabel.setText(taskCount + " " + taskText);
    }

    public LocalDate getCurrentDate() {
        return dateService.getCurrentDate();
    }

    private TaskComponent createTaskComponent(Task task, boolean showDate) {
        return new TaskComponent(task, dialogService, taskContainer, this, showDate);
    }
}
