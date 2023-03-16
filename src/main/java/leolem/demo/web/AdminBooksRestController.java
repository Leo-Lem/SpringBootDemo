package leolem.demo.web;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.val;

import leolem.demo.business.BookService;
import leolem.demo.web.dto.UpdateBookRequest;
import leolem.demo.web.dto.BookResponse;
import leolem.demo.web.dto.CreateBookRequest;

@RestController
@RequestMapping("/admin/books")
public class AdminBooksRestController {

  private final BookService bookService;

  @Autowired
  public AdminBooksRestController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping
  ResponseEntity<?> create(@RequestBody CreateBookRequest request) {
    try {
      val book = bookService.create(
          request.getTitle(),
          request.getAuthor(),
          LocalDate.parse(request.getPublishedOn()),
          request.getAvailableCopies());
      val bookResponse = new BookResponse(book);
      return ResponseEntity.ok().body(bookResponse);
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest().body("Invalid date format: please use yyyy-MM-dd");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Invalid argument: " + e.getMessage());
    }
  }

  @PutMapping("/{id}")
  ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody UpdateBookRequest request) {
    try {
      val book = bookService.update(
          id,
          request.getTitle(),
          request.getAuthor(),
          request.getPublishedOn().map(LocalDate::parse),
          request.getAvailableCopies());
      val bookResponse = new BookResponse(book);
      return ResponseEntity.ok().body(bookResponse);
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest().body("Invalid date format: please use yyyy-MM-dd");
    }
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> delete(@PathVariable("id") int id) {
    val exists = bookService.delete(id);
    return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

}
