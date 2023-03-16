package leolem.demo.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.business.BookService;
import leolem.demo.web.dto.BookResponse;
import lombok.val;

@RestController
@RequestMapping("/books")
public class BooksRestController {

  @Autowired
  private BookService bookService;

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

}
