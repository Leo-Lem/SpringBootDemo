package leolem.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import leolem.demo.model.Book;
import leolem.demo.repos.BookRepository;

@Service
public class GetBookService {
  private final BookRepository bookRepository;

  @Autowired
  public GetBookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public Optional<Book> getbyId(int bookId) {
    return bookRepository.findById(bookId);
  }

  public List<Book> getAllBooks() {
    return (List<Book>) bookRepository.findAll();
  }
}
