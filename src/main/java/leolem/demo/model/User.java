package leolem.demo.model;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;

@Entity(name = "_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @ManyToMany
  @JoinTable(name = "borrowed_book", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
  private List<Book> borrowedBooks;

  public List<Book> getBorrowedBooks() {
    return Collections.unmodifiableList(borrowedBooks);
  }

}
