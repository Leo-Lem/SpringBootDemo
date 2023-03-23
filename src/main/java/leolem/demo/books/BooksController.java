package leolem.demo.books;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.books.dto.BookResponse;
import leolem.demo.books.dto.CreateBookRequest;
import leolem.demo.books.dto.UpdateBookRequest;
import lombok.val;

@RestController
@RequestMapping("/books")
public class BooksController {

  @Autowired
  private BookService bookService;

  @PostMapping
  ResponseEntity<?> create(@Validated @RequestBody CreateBookRequest request) {
    try {
      val book = bookService.create(
          request.getTitle(),
          request.getAuthor(),
          LocalDate.parse(request.getPublishedOn()),
          request.getAvailableCopies());

      val bookResponse = new BookResponse(
          book.getId(),
          book.getTitle(),
          book.getAuthor(),
          book.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
          book.getAvailableCopies(),
          book.getBorrowableCopies());

      return ResponseEntity.ok().body(bookResponse);
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest().body("Invalid date format: please use yyyy-MM-dd");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Invalid argument: " + e.getMessage());
    }
  }

  @GetMapping
  public List<BookResponse> fetchAllBooks() {
    return bookService.readAll()
        .stream()
        .map(BookResponse::new)
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  ResponseEntity<?> fetchUser(@PathVariable("id") long id) {
    try {
      val book = bookService.readById(id);

      val bookResponse = new BookResponse(book);

      return ResponseEntity.ok().body(bookResponse);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  ResponseEntity<?> update(@PathVariable("id") long id, @Validated @RequestBody UpdateBookRequest request) {
    try {
      val book = bookService.update(
          id,
          request.getTitle(),
          request.getAuthor(),
          request.getPublishedOn().map(LocalDate::parse),
          request.getAvailableCopies());

      val bookResponse = new BookResponse(
          book.getId(),
          book.getTitle(),
          book.getAuthor(),
          book.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
          book.getAvailableCopies(),
          book.getBorrowableCopies());

      return ResponseEntity.ok().body(bookResponse);
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest().body("Invalid date format: please use yyyy-MM-dd");
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> delete(@PathVariable("id") long id) {
    try {
      bookService.delete(id);
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

}
