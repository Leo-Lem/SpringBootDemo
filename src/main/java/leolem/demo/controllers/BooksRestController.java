package leolem.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import leolem.demo.controllers.dto.BookResponse;
import leolem.demo.services.GetBookService;

@RestController
@RequestMapping("/books")
public class BooksRestController {

  private final GetBookService getBookService;

  @Autowired
  public BooksRestController(GetBookService getBookService) {
    this.getBookService = getBookService;
  }

  @GetMapping
  public List<BookResponse> fetchAllBooks() {
    return getBookService.getAllBooks()
        .stream()
        .map(book -> new BookResponse(book))
        .collect(Collectors.toList());
  }

}
