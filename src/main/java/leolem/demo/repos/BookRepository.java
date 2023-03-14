package leolem.demo.repos;

import org.springframework.data.repository.CrudRepository;

import leolem.demo.model.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {

}
