package leolem.demo.data.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "_user")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
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

}
