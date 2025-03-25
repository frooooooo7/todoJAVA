package models;

import java.time.LocalDate;

public abstract class BaseTask {
    protected int id;
    protected String name;
    protected boolean completed;
    protected LocalDate dueDate;

    public BaseTask(String name, LocalDate dueDate) {
        this.name = name;
        this.dueDate = dueDate;
        this.completed = false;
    }

    public BaseTask(int id, String name, boolean completed, LocalDate dueDate) {
        this.id = id;
        this.name = name;
        this.completed = completed;
        this.dueDate = dueDate;
    }

    public abstract void printInfo();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
