package leolem.demo.books;

import java.time.LocalDate;
import java.time.format.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import leolem.demo.books.data.Book;
import leolem.demo.books.dto.*;
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
  public List<BookResponse> fetchAllBooks(
      HttpServletResponse response,
      @RequestParam Optional<Integer> page,
      @RequestParam Optional<Integer> size,
      @RequestParam Optional<List<String>> sort,
      @RequestParam Optional<Boolean> sortAscending,
      @RequestParam Map<String, String> params) {
    Book example = Book.builder()
        .title(params.get("title"))
        .author(params.get("author"))
        .availableCopies(Optional.ofNullable(params.get("availableCopies")).map(Integer::parseInt).orElse(null))
        .publishedOn(Optional.ofNullable(params.get("publishedOn")).map(LocalDate::parse).orElse(null))
        .build();
    List<Book> books;

    if (page.isPresent()) {
      Page<Book> bookPage = bookService.readAllMatching(example, page, size, sort, sortAscending);

      response.setHeader("X-Total-Count", String.valueOf(bookPage.getTotalElements()));

      response.addHeader("Link", buildPageLinkHeader("first", 0, bookPage));
      response.addHeader("Link", buildPageLinkHeader("last", bookPage.getTotalPages(), bookPage));
      if (bookPage.hasNext())
        response.addHeader("Link", buildPageLinkHeader("next", bookPage.getNumber() + 1, bookPage));
      if (bookPage.hasPrevious())
        response.addHeader("Link", buildPageLinkHeader("previous", bookPage.getNumber() - 1, bookPage));

      books = bookPage.getContent();
    } else
      books = bookService.readAllMatching(example, sort, sortAscending);

    return books.stream().map(BookResponse::new).collect(Collectors.toList());
  }

  private String buildPageLinkHeader(String rel, Integer page, Page<Book> bookPage) {
    val builder = ServletUriComponentsBuilder.fromCurrentRequest();
    builder.replaceQueryParam("page", page);
    builder.replaceQueryParam("size", bookPage.getSize());
    return "<" + builder.toUriString() + ">; rel=\"" + rel + "\"";
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
