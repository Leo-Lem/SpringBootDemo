package leolem.demo.users.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.users.data.User;
import leolem.demo.users.data.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

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
