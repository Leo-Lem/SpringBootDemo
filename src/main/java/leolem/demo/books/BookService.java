package leolem.demo.books;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.books.data.Book;
import leolem.demo.books.data.repo.BookQuery;
import leolem.demo.books.data.repo.BookRepository;
import lombok.val;

@Service
public class BookService {

  @Autowired
  private BookRepository repo;

  public Book create(String title, String author, LocalDate publishedOn, int availableCopies)
      throws IllegalArgumentException {

    if (title == null || author == null || publishedOn == null)
      throw new IllegalArgumentException("Title, author AND publishedOn are required.");

    val book = Book.builder()
        .title(title)
        .author(author)
        .publishedOn(publishedOn)
        .availableCopies(availableCopies)
        .build();

    return repo.save(book);
  }

  public Book readById(long id) throws EntityNotFoundException {
    return repo
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException());
  }

  public Set<Book> readByQuery(BookQuery query) throws EntityNotFoundException {
    val books = repo.findByQuery(query);
    if (books.isEmpty())
      throw new EntityNotFoundException("No books matching query were found");
    return books;
  }

  public Book update(
      long id,
      Optional<String> title,
      Optional<String> author,
      Optional<LocalDate> publishedOn,
      Optional<Integer> availableCopies) throws EntityNotFoundException {
    val book = readById(id);

    title.ifPresent(book::setTitle);
    author.ifPresent(book::setAuthor);
    publishedOn.ifPresent(book::setPublishedOn);
    availableCopies.ifPresent(arg0 -> book.setAvailableCopies(arg0));

    return repo.save(book);
  }

  public void delete(long id) throws EntityNotFoundException {
    if (!repo.existsById(id))
      throw new EntityNotFoundException();

    repo.deleteById(id);
  }

}
