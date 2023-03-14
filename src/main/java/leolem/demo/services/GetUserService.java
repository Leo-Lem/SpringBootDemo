package leolem.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import leolem.demo.model.User;
import leolem.demo.repos.UserRepository;

@Service
public class GetUserService {

  private final UserRepository userRepository;

  @Autowired
  public GetUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> getbyId(int userId) {
    return userRepository.findById(userId);
  }

}
