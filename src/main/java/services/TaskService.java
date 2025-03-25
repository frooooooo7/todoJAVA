package services;

import db.Db;
import enums.TaskFilter;
import exceptions.DatabaseException;
import models.Task;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TaskService {
    private static final Logger LOGGER = Logger.getLogger(TaskService.class.getName());
    private final Db db;

    public TaskService() {
        this.db = new Db();
        try {
            db.getConnection();
        } catch (DatabaseException e) {
            LOGGER.log(Level.SEVERE, "Blad przy laczeniu z baza danych", e);
        }
    }

    public void addTask(Task task) {
        try {
            db.addTask(task);
        } catch (DatabaseException e) {
            LOGGER.log(Level.SEVERE, "Blad przy dodawaniu zadania", e);
        }
    }

    public void deleteTask(int taskId) {
        try {
            db.deleteTask(taskId);
        } catch (DatabaseException e) {
            LOGGER.log(Level.SEVERE, "Blad przy usuwaniu zadania", e);
        }
    }

    public void updateTask(Task task) {
        try {
            db.updateTask(task);
        } catch (DatabaseException e) {
            LOGGER.log(Level.SEVERE, "Blad przy aktualizacji zadania", e);
        }
    }

    public List<Task> getTasksSorted(boolean ascending, TaskFilter filter) {
        try {
            return switch (filter) {
                case WSZYSTKIE -> db.getTasksSortedByDate(ascending);
                case ZROBIONE -> db.getCompletedTasksSortedByDate(ascending);
                case NIEZROBIONE -> db.getUncompletedTasksSortedByDate(ascending);
            };
        } catch (DatabaseException e) {
            LOGGER.log(Level.SEVERE, "Błąd przy pobieraniu zadań", e);
            return List.of();
        }
    }


    public List<Task> getTasks(Predicate<Task> filter) {
        try {
            return db.getAllTasks().stream()
                    .filter(filter)
                    .collect(Collectors.toList());
        } catch (DatabaseException e) {
            LOGGER.log(Level.SEVERE, "Blad przy pobieraniu zadan", e);
            return List.of();
        }
    }
}
