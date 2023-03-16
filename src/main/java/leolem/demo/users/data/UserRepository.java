package leolem.demo.users.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

}
