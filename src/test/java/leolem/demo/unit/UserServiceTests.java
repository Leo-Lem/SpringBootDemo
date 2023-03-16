package leolem.demo.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.*;

import leolem.demo.business.UserService;
import leolem.demo.data.UserRepository;
import leolem.demo.data.model.User;
import lombok.val;

public class UserServiceTests {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreating() {
    val name = "John";
    val firstName = "Wayne";
    val email = "jwayne@xyz.com";
    val password = "1234";

    mockSaving();

    val book = userService.create(name, firstName, email, password);

    assertEquals(book.getName(), name);
    assertEquals(book.getFirstName(), firstName);
    assertEquals(book.getEmail(), email);
    assertEquals(book.getPassword(), password);
  }

  @Test
  void testReadingById() {
    mockFindingById();
    val book = userService.readById(1);
    assertNotNull(book);
  }

  @Test
  void testUpdating() {
    val name = "John";
    val firstName = "Wayne";
    val email = "jwayne@xyz.com";
    val password = "1234";

    mockFindingById();
    mockSaving();

    val book = userService.update(
        1,
        Optional.of(name),
        Optional.of(firstName),
        Optional.of(email),
        Optional.of(password));

    assertEquals(book.getName(), name);
    assertEquals(book.getFirstName(), firstName);
    assertEquals(book.getEmail(), email);
    assertEquals(book.getPassword(), password);
  }

  @Test
  void testDeleting() {
    when(userRepository.existsById(anyInt()))
       .thenReturn(true);

    val wasSuccessful = userService.delete(1);

    assertTrue(wasSuccessful);
  }

  private void mockSaving() {
    when(userRepository.save(any(User.class)))
        .then(AdditionalAnswers.returnsFirstArg());
  }

  private void mockFindingById() {
    when(userRepository.findById(anyInt()))
        .thenReturn(Optional.of(new User()));
  }

}
