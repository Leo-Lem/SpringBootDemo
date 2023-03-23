package leolem.demo.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.*;

import leolem.demo.users.UserService;
import leolem.demo.users.data.User;
import leolem.demo.users.data.UserRepository;
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
  void givenExists_whenReadingByID_thenReturnNotNull() {
    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(new User()));

    val user = userService.readById(1);

    assertNotNull(user);
  }

  @Test
  void givenExists_whenReadingByEmail_thenReturnsNotNull() {
    when(userRepository.findByEmail(anyString()))
        .thenReturn(Optional.of(new User()));

    val user = userService.readByEmail("jwayne@xyz.com");

    assertNotNull(user);
  }

  @Test
  void givenExists_whenUpdatingDetails_thenUpdatedDetailsMatch() {
    val name = "John Wayne";
    val email = "jwayne@xyz.com";
    val password = "1234";

    when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(new User()));
    when(userRepository.save(any(User.class)))
        .then(AdditionalAnswers.returnsFirstArg());

    val user = userService.update(
        1,
        Optional.of(name),
        Optional.of(email),
        Optional.of(password));

    assertEquals(user.getName(), name);
    assertEquals(user.getEmail(), email);
    assertEquals(user.getPassword(), password);
  }

  @Test
  void givenExists_whenDeleting_thenDoesNotThrow() {
    when(userRepository.existsById(anyLong()))
       .thenReturn(true);

    assertDoesNotThrow(() -> userService.delete(1));
  }

}
