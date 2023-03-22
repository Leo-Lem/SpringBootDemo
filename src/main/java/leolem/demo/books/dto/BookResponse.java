package leolem.demo.books.dto;

import java.time.format.DateTimeFormatter;

import leolem.demo.books.data.Book;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookResponse {
  private final long id;
  private final String title;
  private final String author;
  private final String publication;
  private final int availableCopies;
  private final int borrowableCopies;

  public BookResponse(Book book) {
    this(
        book.getId(),
        book.getTitle(),
        book.getAuthor(),
        book.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        book.getAvailableCopies(),
        book.getBorrowableCopies());
  }
}
