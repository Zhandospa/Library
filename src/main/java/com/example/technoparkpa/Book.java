package com.example.technoparkpa;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import javax.persistence.*;
import java.util.Objects;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Book {
    @Id
    private Long id;
    @Min(value = 0, message = "Cant be less")
    @Max(value = 2025, message = "Are you ok?")
    private int age;
    @NotEmpty(message = "Name should not empty")
    @Size(min = 4, max = 15, message = "Name should be between 4 and 15")
    private String fullName;
    @NotEmpty(message = "Name should not empty")
    @Size(min = 4, max = 15, message = "Name should be between 4 and 15")
    private String author;
    private Person person;

    public Book() {}

    public Book(String fullName, String author, int age, Long id) {
        this.age = age;
        this.fullName = fullName;
        this.id = id;
        this.author = author;
    }

    public Book(String fullName, String author, int age) {
        this.age = age;
        this.fullName = fullName;
        this.author = author;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void delPerson(){
        person = null;
    }
    public void insertBook(Connection connection) {
        String sql = "INSERT INTO books (id, age, fullName, author, person_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.setInt(2, age);
            statement.setString(3, fullName);
            statement.setString(4, author);
            statement.setLong(5, person != null ? person.getId() : 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
