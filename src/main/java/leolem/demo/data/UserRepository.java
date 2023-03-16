package leolem.demo.data;

import org.springframework.data.repository.CrudRepository;

import leolem.demo.data.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
