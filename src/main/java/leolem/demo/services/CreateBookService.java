package leolem.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import leolem.demo.model.Book;
import leolem.demo.repos.BookRepository;

@Service
public class CreateBookService {

  private final BookRepository bookRepository;

  @Autowired
  public CreateBookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public Book createBook(Book book) {
    bookRepository.save(book);
    return book;
  }

}
