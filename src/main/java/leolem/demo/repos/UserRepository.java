package leolem.demo.repos;

import org.springframework.data.repository.CrudRepository;

import leolem.demo.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
