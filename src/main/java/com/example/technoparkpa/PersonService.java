package com.example.technoparkpa;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    public List<Person> getPeople() {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM people";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getLong("id"));
                person.setAge(resultSet.getInt("age"));
                person.setFullName(resultSet.getString("fullName"));
                people.add(person);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    public void addPerson(Person person) {
        String sql = "INSERT INTO people (id, age, fullName) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement statement = connection.prepareStatement(sql)) {

            Long bookId = person.getId();
            if (bookId != null) {
                statement.setLong(1, bookId);
                statement.setInt(2, person.getAge());
                statement.setString(3, person.getFullName());
                statement.executeUpdate();
            } else {
                bookId = (long)(Math.random()*10000000);
                statement.setLong(1, bookId);
                statement.setInt(2, person.getAge());
                statement.setString(3, person.getFullName());
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void deletePerson(long id) {
        String sql = "DELETE FROM people WHERE id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upDatePerson( Person person) {
        String sql = "UPDATE people SET age = ?, fullName = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, person.getAge());
            statement.setString(2, person.getFullName());
            statement.setLong(3, person.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Person getPerson(long id) {
        String personSql = "SELECT * FROM people WHERE id = ?";
        String booksSql = "SELECT * FROM books WHERE person_id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement personStatement = connection.prepareStatement(personSql);
             PreparedStatement booksStatement = connection.prepareStatement(booksSql)) {

            personStatement.setLong(1, id);
            try (ResultSet personResultSet = personStatement.executeQuery()) {
                if (personResultSet.next()) {
                    Person person = new Person();
                    person.setId(personResultSet.getLong("id"));
                    person.setAge(personResultSet.getInt("age"));
                    person.setFullName(personResultSet.getString("fullName"));

                    booksStatement.setLong(1, id);
                    try (ResultSet booksResultSet = booksStatement.executeQuery()) {
                        List<Book> books = new ArrayList<>();
                        while (booksResultSet.next()) {
                            Book book = new Book();
                            book.setId(booksResultSet.getLong("id"));
                            book.setFullName(booksResultSet.getString("fullname"));
                            book.setAuthor(booksResultSet.getString("author"));
                            book.setAge(booksResultSet.getInt("age"));
                            books.add(book);
                        }
                        person.setBooks(books);
                    }

                    return person;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
