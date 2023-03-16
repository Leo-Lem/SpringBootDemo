package leolem.demo.data.model;

import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "book")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "author")
  private String author;

  @Column(name = "publication")
  private LocalDate publishedOn;

  @Column(name = "numberOfInstances")
  private int availableCopies;

  @ManyToMany(mappedBy = "borrowedBooks")
  private List<User> borrowers;

  public int getBorrowableCopies() {
    return availableCopies - borrowers.size();
  }

}
