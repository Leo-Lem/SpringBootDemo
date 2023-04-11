package leolem.demo.books.data.repo;

import org.springframework.data.repository.CrudRepository;

import leolem.demo.books.data.Book;

public interface BookRepository extends CrudRepository<Book, Long>, CustomBookRepository {
}