package leolem.demo.model;

import jakarta.persistence.*;

@Entity(name = "borrowed_book")
public class BorrowedBook {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "book_id")
  private int bookID;

  @Column(name = "user_id")
  private int userID;

}
