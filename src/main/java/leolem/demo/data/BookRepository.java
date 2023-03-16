package leolem.demo.data;

import org.springframework.data.repository.CrudRepository;

import leolem.demo.data.model.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {

}
