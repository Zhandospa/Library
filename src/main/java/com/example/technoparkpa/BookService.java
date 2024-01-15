package com.example.technoparkpa;

import org.springframework.stereotype.Service;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

@Service
public class BookService {

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setAge(resultSet.getInt("age"));
                book.setFullName(resultSet.getString("fullName"));
                book.setAuthor(resultSet.getString("author"));
                // You may want to fetch and set the person details as well
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO books (id, age, fullName, author) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement statement = connection.prepareStatement(sql)) {

            Long bookId = book.getId();
            if (bookId != null) {
                statement.setLong(1, bookId);
                statement.setInt(2, book.getAge());
                statement.setString(3, book.getFullName());
                statement.setString(4, book.getAuthor());
                statement.executeUpdate();
            } else {
                bookId = (long)(Math.random()*1000000);
                statement.setLong(1, bookId);
                statement.setInt(2, book.getAge());
                statement.setString(3, book.getFullName());
                statement.setString(4, book.getAuthor());
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteBook(long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upDateBook( Book book) {
        String sql = "UPDATE books SET age = ?, fullName = ?, author = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, book.getAge());
            statement.setString(2, book.getFullName());
            statement.setString(3, book.getAuthor());
            statement.setLong(4, book.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void upDateBook( Book book, long id) {
        String sql = "UPDATE books SET person_id = ? ,age = ?, fullName = ?, author = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if(id == -456){
                statement.setObject(1,null);
            }else{
            statement.setLong(1,id);}
            statement.setInt(2, book.getAge());
            statement.setString(3, book.getFullName());
            statement.setString(4, book.getAuthor());
            statement.setLong(5, book.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book getBook(long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        String sql2 = "SELECT * FROM people WHERE id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika");
             PreparedStatement statement = connection.prepareStatement(sql);
             PreparedStatement statement2 = connection.prepareStatement(sql2)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Book book = new Book();
                    book.setId(resultSet.getLong("id"));
                    book.setAge(resultSet.getInt("age"));
                    book.setFullName(resultSet.getString("fullName"));
                    book.setAuthor(resultSet.getString("author"));
                    long personId = resultSet.getLong("person_id");

                    if(personId != 0){
                        statement2.setLong(1, personId);
                        try (ResultSet personResulSet = statement2.executeQuery()) {
                            Person p = new Person();
                            while (personResulSet.next()) {
                                p.setId(personResulSet.getLong("id"));
                                p.setFullName(personResulSet.getString("fullname"));
                                p.setAge(personResulSet.getInt("age"));
                            }
                            book.setPerson(p);
                        }
                    }

                    return book;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
