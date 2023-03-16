package leolem.demo.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import leolem.demo.data.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

}
