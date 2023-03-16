package leolem.demo.business;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.data.BookRepository;
import leolem.demo.data.model.Book;
import lombok.val;

@Service
public class BookService {

  private final BookRepository bookRepository;

  @Autowired
  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public Book create(String title, String author, LocalDate publishedOn, int availableCopies)
      throws IllegalArgumentException {
    if (title == null || author == null || publishedOn == null) {
      throw new IllegalArgumentException("Title, author AND publishedOn must be provided.");
    }

    return bookRepository.save(Book.builder()
        .title(title)
        .author(author)
        .publishedOn(publishedOn)
        .availableCopies(availableCopies)
        .build());
  }

  public Optional<Book> readById(int bookId) throws EntityNotFoundException {
    return bookRepository.findById(bookId);
  }

  public List<Book> readAll() {
    return (List<Book>) bookRepository.findAll();
  }

  public Book update(
      int id,
      Optional<String> title,
      Optional<String> author,
      Optional<LocalDate> publishedOn,
      Optional<Integer> availableCopies) {
    var book = bookRepository
        .findById(id)
        .orElse(Book.builder().id(id).build());

    title.ifPresent(book::setTitle);
    author.ifPresent(book::setAuthor);
    publishedOn.ifPresent(book::setPublishedOn);
    availableCopies.ifPresent(arg0 -> book.setAvailableCopies(arg0));

    return bookRepository.save(book);
  }

  public boolean delete(int id) {
    val exists = bookRepository.existsById(id);
    if (exists)
      bookRepository.deleteById(id);
    return exists;
  }

}
