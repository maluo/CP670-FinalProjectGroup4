package com.example.cp670_finalprojectgroup4.data.dao;

import com.example.cp670_finalprojectgroup4.Status;
import com.example.cp670_finalprojectgroup4.Todo;
import com.example.cp670_finalprojectgroup4.data.model.UserModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        TodoDAO.connection = connection;
    }

    public TodoDAO() {
    }

    public static List<Todo> getAllToDoItems(){
        List<Todo> todos = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select todoId, userId, title, description, location, startdate, duration, starttime, status from todo_activity;");
            while (resultSet.next()){
                Todo newTodo = new Todo();

                newTodo.setTodoId(resultSet.getInt(1));
                newTodo.setUserId(resultSet.getInt(2));
                newTodo.setTitle(resultSet.getString(3));
                newTodo.setDescription(resultSet.getString(4));
                newTodo.setLocation(resultSet.getString(5));
                newTodo.setStartdate(resultSet.getDate(6));
                newTodo.setDuration(resultSet.getInt(7));
                newTodo.setStarttime(resultSet.getTime(8));
                newTodo.setStatus(Status.INPROGRESS);
                todos.add(newTodo);
            }
            return todos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Todo> getAllToDoItemsForUser(int userId){
        List<Todo> todos = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select todoId, userId, title, description, location, startdate, duration, starttime, status from todo_activity where userId =" + userId + ";");
            while (resultSet.next()){
                Todo newTodo = new Todo();

                newTodo.setTodoId(resultSet.getInt(1));
                newTodo.setUserId(resultSet.getInt(2));
                newTodo.setTitle(resultSet.getString(3));
                newTodo.setDescription(resultSet.getString(4));
                newTodo.setLocation(resultSet.getString(5));
                newTodo.setStartdate(resultSet.getDate(6));
                newTodo.setDuration(resultSet.getInt(7));
                newTodo.setStarttime(resultSet.getTime(8));
                String status = resultSet.getString(9);
                if(status.equals(String.valueOf(Status.INPROGRESS))){
                    newTodo.setStatus(Status.INPROGRESS);
                }
                if(status.equals(String.valueOf(Status.TBD))){
                    newTodo.setStatus(Status.TBD);
                }
                if(status.equals(String.valueOf(Status.FINISHED))){
                    newTodo.setStatus(Status.FINISHED);
                }

                todos.add(newTodo);
            }
            return todos;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int addTodo(Todo todo){
        try {
            String query = "" +
                    "insert into todo_activity( userId, title, description, location, startdate, duration, starttime, status)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt (1, todo.getUserId());
            preparedStmt.setString (2, todo.getTitle());
            preparedStmt.setString   (3, todo.getDescription());
            preparedStmt.setString(4, todo.getLocation());
            preparedStmt.setDate (5, new java.sql.Date(todo.getStartdate().getTime()));
            preparedStmt.setInt (6, todo.getDuration());
            preparedStmt.setTime(7, todo.getStarttime());
            preparedStmt.setString(8, String.valueOf(todo.getStatus()));

            int rows = preparedStmt.executeUpdate();

            if(rows != 0){
                ResultSet generatedKeys = preparedStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = (int)generatedKeys.getLong(1);
                    return id;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void deleteTodo(int todoId){
        try {
            String query = "" +
                    "delete from todo_activity where todoId = " + todoId +";";

            Statement stmt = connection.createStatement();

            stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
