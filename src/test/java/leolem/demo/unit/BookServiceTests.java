package leolem.demo.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lombok.val;

import leolem.demo.business.BookService;
import leolem.demo.data.BookRepository;
import leolem.demo.data.model.Book;

public class BookServiceTests {

  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private BookService bookService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreating() {
    val title = "Screwjack";
    val author = "Hunter S. Thompson";
    val publishedOn = LocalDate.parse("1991-01-01");
    val availableCopies = 4;

    mockSaving();

    val book = bookService.create(title, author, publishedOn, availableCopies);

    assertEquals(book.getTitle(), title);
    assertEquals(book.getAuthor(), author);
    assertEquals(book.getPublishedOn(), publishedOn);
    assertEquals(book.getAvailableCopies(), availableCopies);
  }

  @Test
  void testReadingById() {
    mockFindingById();
    val book = bookService.readById(1);
    assertNotNull(book);
  }

  @Test
  void testReadingAll() {
    when(bookRepository.findAll())
        .thenReturn(List.of(new Book()));
    val books = bookService.readAll();
    assertTrue(books.size() > 0);
  }

  @Test
  void testUpdating() {
    val title = "Screwjack";
    val author = "Hunter S. Thompson";
    val publishedOn = LocalDate.parse("1991-01-01");
    val availableCopies = 4;

    mockFindingById();
    mockSaving();

    val book = bookService.update(
        1,
        Optional.of(title),
        Optional.of(author),
        Optional.of(publishedOn),
        Optional.of(availableCopies));

    assertEquals(book.getTitle(), title);
    assertEquals(book.getAuthor(), author);
    assertEquals(book.getPublishedOn(), publishedOn);
    assertEquals(book.getAvailableCopies(), availableCopies);
  }

  @Test
  void testDeleting() {
    when(bookRepository.existsById(anyInt()))
       .thenReturn(true);

    val wasSuccessful = bookService.delete(1);

    assertTrue(wasSuccessful);
  }

  private void mockSaving() {
    when(bookRepository.save(any()))
        .then(AdditionalAnswers.returnsFirstArg());
  }

  private void mockFindingById() {
    when(bookRepository.findById(anyInt()))
        .thenReturn(Optional.of(new Book()));
  }

}
