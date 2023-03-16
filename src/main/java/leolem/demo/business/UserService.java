package leolem.demo.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import leolem.demo.data.UserRepository;
import leolem.demo.data.model.User;
import lombok.val;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public User register(String name, String firstName, String email, String password)
      throws IllegalArgumentException, EntityExistsException {
    if (name == null || firstName == null || email == null || password == null) {
      throw new IllegalArgumentException("Name, first name, e-mail AND password are required");
    }

    if (userRepository.existsByEmail(email))
      throw new EntityExistsException("User with e-mail exists: " + email);

    val user = User.builder()
        .name(name)
        .firstName(firstName)
        .email(email)
        .password(password)
        .build();

    return userRepository.save(user);
  }

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

  public User update(
      long id,
      Optional<String> name,
      Optional<String> firstName,
      Optional<String> email,
      Optional<String> password) throws EntityNotFoundException {
    var user = readById(id);

    name.ifPresent(user::setName);
    firstName.ifPresent(user::setFirstName);
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
