package leolem.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import leolem.demo.controllers.dto.BookRequest;
import leolem.demo.controllers.dto.BookResponse;
import leolem.demo.services.CreateBookService;
import leolem.demo.services.DeleteBookService;
import leolem.demo.services.UpdateBookService;

@RestController
@RequestMapping("/admin/books")
public class AdminBooksRestController {

  private final CreateBookService createBookService;
  private final UpdateBookService updateBookService;
  private final DeleteBookService deleteBookService;

  @Autowired
  public AdminBooksRestController(
      CreateBookService createBookService, UpdateBookService updateBookService, DeleteBookService deleteBookService) {
    this.createBookService = createBookService;
    this.updateBookService = updateBookService;
    this.deleteBookService = deleteBookService;
  }

  @PostMapping
  BookResponse create(@RequestBody BookRequest request) {
    return new BookResponse(createBookService.createBook(request.getBook()));
  }

  @PutMapping("/{id}")
  BookResponse update(@PathVariable("id") int id, @RequestBody BookRequest request) {
    return new BookResponse(updateBookService.updateBook(
        id, request.getTitle(), request.getAuthor(), request.getPublishedOn(), request.getCurrentlyAvailableNumber()));
  }

  @DeleteMapping("/{id}")
  void delete(@PathVariable("id") int id) {
    deleteBookService.deleteBook(id);
  }

}
