package leolem.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import leolem.demo.repos.BookRepository;

@Service
public class DeleteBookService {

  private final BookRepository bookRepository;

  @Autowired
  public DeleteBookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public void deleteBook(int id) {
    bookRepository.deleteById(id);
  }

}
