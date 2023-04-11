package leolem.demo.books.data.repo;

import java.util.*;

import leolem.demo.books.data.Book;

public interface CustomBookRepository {

  public Set<Book> findByQuery(BookQuery query);

}
