package leolem.demo.books.business;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.books.data.Book;
import leolem.demo.books.data.BookRepository;
import lombok.val;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;

  public Book create(String title, String author, LocalDate publishedOn, int availableCopies)
      throws IllegalArgumentException {

    if (title == null || author == null || publishedOn == null) {
      throw new IllegalArgumentException("Title, author AND publishedOn are required.");
    }

    val book = Book.builder()
        .title(title)
        .author(author)
        .publishedOn(publishedOn)
        .availableCopies(availableCopies)
        .build();

    return bookRepository.save(book);
  }

  public Book readById(long id) throws EntityNotFoundException {
    return bookRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException());
  }

  public List<Book> readAll() {
    return (List<Book>) bookRepository.findAll();
  }

  public Book update(
      long id,
      Optional<String> title,
      Optional<String> author,
      Optional<LocalDate> publishedOn,
      Optional<Integer> availableCopies) throws EntityNotFoundException {
    var book = readById(id);

    title.ifPresent(book::setTitle);
    author.ifPresent(book::setAuthor);
    publishedOn.ifPresent(book::setPublishedOn);
    availableCopies.ifPresent(arg0 -> book.setAvailableCopies(arg0));

    return bookRepository.save(book);
  }

  public void delete(long id) throws EntityNotFoundException {
    if (!bookRepository.existsById(id))
      throw new EntityNotFoundException();

    bookRepository.deleteById(id);
  }

}
