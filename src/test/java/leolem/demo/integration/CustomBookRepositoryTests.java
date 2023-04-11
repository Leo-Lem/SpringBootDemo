package leolem.demo.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import leolem.demo.books.data.Book;
import leolem.demo.books.data.repo.*;
import lombok.*;

@DataJpaTest
public class CustomBookRepositoryTests {

  @Autowired
  private BookRepository repo;

  @BeforeEach
  public void setup() {
    repo.save(hellsAngels);
    repo.save(fearAndLoathing);
    repo.save(lotr);
    repo.save(silmarillion);
    repo.save(trainspotting);
    repo.save(trainspottingClone);
  }

  @Test
  public void testFindingByTitleAndAuthor() {
    var query = BookQuery.builder().title(trainspotting.getTitle()).build();
    var books = repo.findByQuery(query);

    assertTrue(books.contains(trainspotting));
    assertTrue(books.contains(trainspottingClone));

    query = BookQuery.builder().title(trainspotting.getTitle()).author(trainspotting.getAuthor()).build();
    books = repo.findByQuery(query);

    assertTrue(books.contains(trainspotting));
    assertFalse(books.contains(trainspottingClone));
  }

  @Test
  public void testFindingByIsAvailable() {
    var query = BookQuery.builder().isAvailable(true).build();
    var books = repo.findByQuery(query);

    assertEquals(4, books.size());

    query = BookQuery.builder().isAvailable(false).build();
    books = repo.findByQuery(query);

    assertEquals(4, books.size());
  }

  @Test
  public void testFindByPublishedInRange() {
    var query = BookQuery.builder()
        .publishedAfter(LocalDate.of(1960, 1, 1))
        .publishedBefore(LocalDate.of(1980, 1, 1))
        .build();
    var books = repo.findByQuery(query);

    assertTrue(books.contains(hellsAngels));
    assertTrue(books.contains(fearAndLoathing));
    assertTrue(books.contains(silmarillion));
    assertFalse(books.contains(lotr));
    assertFalse(books.contains(trainspotting));
  }

  @Test
  public void testFindByFullQuery() {
    var query = BookQuery.builder()
        .title("Trainspotting")
        .author("Irvine Welsh")
        .publishedAfter(LocalDate.of(1992, 1, 1))
        .publishedBefore(LocalDate.of(1994, 1, 1))
        .isAvailable(true)
        .build();
    var books = repo.findByQuery(query);

    assertEquals(1, books.size());
    assertTrue(books.contains(trainspotting));
    assertFalse(books.contains(trainspottingClone));
  }

  private Book hellsAngels = Book.builder()
      .title("Hell's Angels")
      .author("Hunter S. Thompson")
      .publishedOn(LocalDate.of(1967, 1, 1))
      .availableCopies(10)
      .build();

  private Book fearAndLoathing = Book.builder()
      .title("Fear and Loathing in Las Vegas")
      .author("Hunter S. Thompson")
      .publishedOn(LocalDate.of(1971, 1, 1))
      .availableCopies(0)
      .build();

  private Book lotr = Book.builder()
      .title("The Lord of the Rings")
      .author("J.R.R. Tolkien")
      .publishedOn(LocalDate.of(1954, 1, 1))
      .availableCopies(2)
      .build();

  private Book silmarillion = Book.builder()
      .title("The Silmarillion")
      .author("J.R.R. Tolkien")
      .publishedOn(LocalDate.of(1977, 1, 1))
      .availableCopies(5)
      .build();

  private Book trainspotting = Book.builder()
      .title("Trainspotting")
      .author("Irvine Welsh")
      .publishedOn(LocalDate.of(1993, 1, 1))
      .availableCopies(6)
      .build();

  private Book trainspottingClone = Book.builder()
      .title("Trainspotting")
      .author("Wirvine Elsh")
      .publishedOn(LocalDate.of(2013, 1, 1))
      .availableCopies(0)
      .build();

}
