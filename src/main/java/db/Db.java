package db;

import exceptions.DatabaseException;
import models.Task;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Db {
    private Connection connection;
    private static final Logger logger = Logger.getLogger(Db.class.getName());

    public void getConnection() throws DatabaseException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:todo.db");
                logger.info("Polaczenie z baza danych nawiazane");
                createTable();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Blad polaczenia z baza danych", e);
            throw new DatabaseException("Blad polaczenia z baza danych", e);
        }
    }

    public void closeConnection() throws DatabaseException {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Polaczenie z baza danych zamkniete.");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Blad przy zamykaniu polaczenia z baza danych", e);
                throw new DatabaseException("Blad przy zamykaniu polaczenia z baza danych", e);
            }
        }
    }

    private void createTable() throws DatabaseException {
        String query = "CREATE TABLE IF NOT EXISTS todo (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name TEXT, completed BOOLEAN, due_date DATE)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
            logger.info("Tabela utworzona");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Blad przy tworzeniu tabeli", e);
            throw new DatabaseException("Blad przy tworzeniu tabeli", e);
        }
    }

    public void addTask(Task task) throws DatabaseException {
        getConnection();
        String query = "INSERT INTO todo (name, completed, due_date) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, task.getName());
            statement.setBoolean(2, task.isCompleted());
            statement.setDate(3, (task.getDueDate() != null) ? Date.valueOf(task.getDueDate()) : null);
            statement.executeUpdate();
            logger.info("Zadanie dodane: " + task.getName());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Blad przy dodawaniu zadania", e);
            throw new DatabaseException("Blad przy dodawaniu zadania", e);
        }
    }

    public List<Task> getAllTasks() throws DatabaseException {
        getConnection();
        String query = "SELECT * FROM todo";
        return getTasksByQuery(query);
    }

    public List<Task> getTasksSortedByDate(boolean ascending) throws DatabaseException {
        getConnection();
        String order = ascending ? "ASC" : "DESC";
        String query = "SELECT * FROM todo ORDER BY due_date " + order;
        return getTasksByQuery(query);
    }

    public List<Task> getCompletedTasksSortedByDate(boolean ascending) throws DatabaseException {
        getConnection();
        String order = ascending ? "ASC" : "DESC";
        String query = "SELECT * FROM todo WHERE completed = true ORDER BY due_date " + order;
        return getTasksByQuery(query);
    }

    public List<Task> getUncompletedTasksSortedByDate(boolean ascending) throws DatabaseException {
        getConnection();
        String order = ascending ? "ASC" : "DESC";
        String query = "SELECT * FROM todo WHERE completed = false ORDER BY due_date " + order;
        return getTasksByQuery(query);
    }

    private List<Task> getTasksByQuery(String query) throws DatabaseException {
        List<Task> tasks = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                boolean completed = resultSet.getBoolean("completed");

                Date sqlDate = resultSet.getDate("due_date");
                LocalDate dueDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                tasks.add(new Task(id, name, completed, dueDate));
            }
            logger.info("Zadania zaladowane z bazy danych");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Blad przy ladowaniu zadan", e);
            throw new DatabaseException("Blad przy ladowaniu zadan", e);
        }
        return tasks;
    }

    public void deleteTask(int taskId) throws DatabaseException {
        getConnection();
        String query = "DELETE FROM todo WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, taskId);
            statement.executeUpdate();
            logger.info("Zadanie usuniete: " + taskId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Blad przy usuwaniu zadania", e);
            throw new DatabaseException("Blad przy usuwaniu zadania", e);
        }
    }

    public void updateTask(Task task) throws DatabaseException {
        getConnection();
        String query = "UPDATE todo SET name = ?, completed = ?, due_date = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, task.getName());
            statement.setBoolean(2, task.isCompleted());
            if (task.getDueDate() != null) {
                statement.setDate(3, Date.valueOf(task.getDueDate()));
            } else {
                statement.setNull(3, Types.DATE);
            }
            statement.setInt(4, task.getId());
            statement.executeUpdate();
            logger.info("Zadanie zaktualizowane: " + task.getId());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Blad przy aktualizacji zadania", e);
            throw new DatabaseException("Blad przy aktualizacji zadania", e);
        }
    }
}
