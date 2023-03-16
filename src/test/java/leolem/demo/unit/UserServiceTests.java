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
  void testRegistering() {
    val name = "Wayne";
    val firstName = "John";
    val email = "jwayne@xyz.com";
    val password = "1234";

    when(userRepository.save(any(User.class)))
        .then(AdditionalAnswers.returnsFirstArg());

    val book = userService.register(name, firstName, email, password);

    assertEquals(book.getName(), name);
    assertEquals(book.getFirstName(), firstName);
    assertEquals(book.getEmail(), email);
    assertEquals(book.getPassword(), password);
  }

  @Test
  void testReadingById() {
    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(new User()));

    val user = userService.readById(1);

    assertNotNull(user);
  }

  @Test
  void testReadingByEmail() {
    when(userRepository.findByEmail(anyString()))
        .thenReturn(Optional.of(new User()));

    val user = userService.readByEmail("jwayne@xyz.com");

    assertNotNull(user);
  }

  @Test
  void testUpdating() {
    val name = "John";
    val firstName = "Wayne";
    val email = "jwayne@xyz.com";
    val password = "1234";

    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(new User()));
    when(userRepository.save(any(User.class)))
        .then(AdditionalAnswers.returnsFirstArg());

    val user = userService.update(
        1,
        Optional.of(name),
        Optional.of(firstName),
        Optional.of(email),
        Optional.of(password));

    assertEquals(user.getName(), name);
    assertEquals(user.getFirstName(), firstName);
    assertEquals(user.getEmail(), email);
    assertEquals(user.getPassword(), password);
  }

  @Test
  void testDeleting() {
    when(userRepository.existsById(anyLong()))
       .thenReturn(true);

    assertDoesNotThrow(() -> userService.delete(1));
  }

}
