package leolem.demo.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import leolem.demo.business.BookService;
import leolem.demo.web.dto.BookResponse;

@RestController
@RequestMapping("/books")
public class BooksRestController {

  private final BookService bookService;

  @Autowired
  public BooksRestController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public List<BookResponse> fetchAllBooks() {
    return bookService.readAll()
        .stream()
        .map(book -> new BookResponse(book))
        .collect(Collectors.toList());
  }

}
