package leolem.demo.users;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import leolem.demo.users.data.User;
import leolem.demo.users.data.UserRepository;
import lombok.val;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  public User readById(long id) throws EntityNotFoundException {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException());
  }

  public User readByEmail(String email) throws EntityNotFoundException {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException());
  }

  public User create(String name, String email, String password) throws EntityExistsException {
    if (userRepository.existsByEmail(email)) {
      throw new EntityExistsException();
    }

    val user = User.builder()
        .name(name)
        .email(email)
        .password(encoder.encode(password))
        .roles(Set.of(User.Role.USER))
        .build();

    return userRepository.save(user);
  }

  public User update(
      long id,
      Optional<String> name,
      Optional<String> email,
      Optional<String> password) throws EntityNotFoundException {
    var user = readById(id);

    name.ifPresent(user::setName);
    email.ifPresent(user::setEmail);
    password.ifPresent(user::setPassword);

    return userRepository.save(user);
  }

  public void delete(long id) throws EntityNotFoundException {
    if (!userRepository.existsById(id))
      throw new EntityNotFoundException();

    userRepository.deleteById(id);
  }

}
