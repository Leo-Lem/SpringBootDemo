package leolem.demo.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.mockito.*;
import lombok.val;
import leolem.demo.books.BookService;
import leolem.demo.books.data.*;

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
  void whenCreatingBook_thenCreatedBookDetailsMatch() {
    val title = "Screwjack";
    val author = "Hunter S. Thompson";
    val publishedOn = LocalDate.parse("1991-01-01");
    val availableCopies = 4;

    when(bookRepository.save(any()))
        .then(AdditionalAnswers.returnsFirstArg());

    val book = bookService.create(title, author, publishedOn, availableCopies);

    assertEquals(book.getTitle(), title);
    assertEquals(book.getAuthor(), author);
    assertEquals(book.getPublishedOn(), publishedOn);
    assertEquals(book.getAvailableCopies(), availableCopies);
  }

  @Test
  void givenBookExists_whenReadingByID_thenReturnsNotNull() {
    when(bookRepository.findById(anyLong()))
        .thenReturn(Optional.of(new Book()));

    val book = bookService.readById(1);

    assertNotNull(book);
  }

  @Test
  void givenBookExists_whenUpdatingWithNewDetails_thenUpdatedBookDetailsMatch() {
    val title = "Screwjack";
    val author = "Hunter S. Thompson";
    val publishedOn = LocalDate.parse("1991-01-01");
    val availableCopies = 4;

    when(bookRepository.findById(anyLong()))
        .thenReturn(Optional.of(new Book()));
    when(bookRepository.save(any()))
        .then(AdditionalAnswers.returnsFirstArg());

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
  void givenBookExists_whenDeletingBook_thenDoesNotThrowError() {
    when(bookRepository.existsById(anyLong()))
       .thenReturn(true);

    assertDoesNotThrow(() -> bookService.delete(1));
  }

}
