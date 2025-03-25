package models;

import java.time.LocalDate;

public class Task extends BaseTask {

    public Task(String name, LocalDate dueDate) {
        super(name, dueDate);
    }

    public Task(int id, String name, boolean completed, LocalDate dueDate) {
        super(id, name, completed, dueDate);
    }

    @Override
    public void printInfo() {
        System.out.println("Zadanie: " + name +
                           ", Termin: " + dueDate +
                           ", Ukończone: " + (completed ? "Tak" : "Nie"));
    }
}
