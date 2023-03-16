package leolem.demo.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;

import leolem.demo.business.BookService;
import leolem.demo.web.dto.UpdateBookRequest;
import leolem.demo.web.dto.BookResponse;
import leolem.demo.web.dto.CreateBookRequest;

@RestController
@RequestMapping("/admin/books")
public class AdminBooksRestController {

  @Autowired
  private BookService bookService;

  @PostMapping
  ResponseEntity<?> create(@RequestBody CreateBookRequest request) {
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
          book.getAvailableCopies());

      return ResponseEntity.ok().body(bookResponse);
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest().body("Invalid date format: please use yyyy-MM-dd");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Invalid argument: " + e.getMessage());
    }
  }

  @PutMapping("/{id}")
  ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody UpdateBookRequest request) {
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
          book.getAvailableCopies());

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
