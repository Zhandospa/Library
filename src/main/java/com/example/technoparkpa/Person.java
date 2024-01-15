package com.example.technoparkpa;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person {
    private Long id;
    @Min(value = 1900, message = "Age cannot be more than 124")
    @Max(value = 2024, message = "Are you ok?")
    private int age;
    @NotEmpty(message = "Name should not empty")
    @Size(min = 4, max = 25, message = "Name should be between 4 and 25")
    private String fullName;
    private List<Book> books = new ArrayList<>();

    public Person() {}

    public Person(String fullName, int age, Long id) {
        this.age = age;
        this.fullName = fullName;
        this.id = id;
    }

    public Person(String fullName, int age) {
        this.age = age;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book b){
        books.add(b);
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void insertPerson(Connection connection) {
        String sql = "INSERT INTO people (id, age, fullName) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (id != null) {
                statement.setLong(1, id);
            } else {
                statement.setNull(1, Types.BIGINT);
            }
            statement.setInt(2, age);
            statement.setString(3, fullName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Book book : books) {
            book.insertBook(connection);
        }
    }


    public void delBook(Connection connection, long bookId) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removePersonFromBook(Connection connection, long bookId) {
        String sql = "UPDATE books SET person_id = null WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
