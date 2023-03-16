package leolem.demo.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.data.UserRepository;
import leolem.demo.data.model.User;
import lombok.val;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User create(String name, String firstName, String email, String password) {
    if (name == null || firstName == null || email == null || password == null) {
      throw new IllegalArgumentException("Name, first name, e-mail AND password must be provided.");
    }

    val user = User.builder()
        .name(name)
        .firstName(firstName)
        .email(email)
        .password(password)
        .build();

    return userRepository.save(user);
  }

  public Optional<User> readById(int userId) throws EntityNotFoundException {
    return userRepository.findById(userId);
  }

  public User update(
      int id,
      Optional<String> name,
      Optional<String> firstName,
      Optional<String> email,
      Optional<String> password) {
    var user = userRepository
        .findById(id)
        .orElse(User.builder().id(id).build());

    name.ifPresent(user::setName);
    firstName.ifPresent(user::setFirstName);
    email.ifPresent(user::setEmail);
    password.ifPresent(user::setPassword);

    return userRepository.save(user);
  }

  public boolean delete(int id) {
    val exists = userRepository.existsById(id);
    if (exists)
      userRepository.deleteById(id);
    return exists;
  }

}
