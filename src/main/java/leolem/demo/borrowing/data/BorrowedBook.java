package leolem.demo.borrowing.data;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "borrowed_book")
@Data
public class BorrowedBook {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "book_id")
  private int bookID;

  @Column(name = "user_id")
  private int userID;

}
