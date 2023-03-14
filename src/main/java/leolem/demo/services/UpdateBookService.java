package leolem.demo.services;

import java.sql.Date;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.model.Book;
import leolem.demo.model.User;
import leolem.demo.repos.BookRepository;

@Service
public class UpdateBookService {
  private final GetUserService getUserService;
  private final BookRepository bookRepository;

  public UpdateBookService(GetUserService getUserService, BookRepository bookRepository) {
    this.getUserService = getUserService;
    this.bookRepository = bookRepository;
  }

  public Book updateBook(int id, String title, String author, Date publishedOn, int currentlyAvailableNumber) {
    Book book = bookRepository.findById(id).orElse(new Book());
    book.setTitle(title);
    book.setAuthor(author);
    book.setPublication(publishedOn);
    book.setNumberOfInstances(currentlyAvailableNumber);
    return bookRepository.save(book);
  }

  public void borrow(int bookId, int userId) {
    User user = getUserService.getbyId(userId).orElseThrow(() -> new EntityNotFoundException());
    verifyUserHasNotBorrowedBook(user, bookId);
    verifyUserBorrowLimitIsNotReached(user);

    Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException());
    verifyBookIsInStock(book);
    updateBookStatus(user, book);

    bookRepository.save(book);
  }

  private void updateBookStatus(User user, Book book) {
    book.addBorrower(user);
    book.setNumberOfInstances(book.getNumberOfInstances() + 1);
  }

  private void verifyBookIsInStock(Book book) {
    if (book.getNumberOfInstances() < 1) {
      throw new IllegalStateException("There are no available books!");
    }
  }

  private void verifyUserBorrowLimitIsNotReached(User user) throws IllegalStateException {
    if (user.getBorrowedBooks().size() >= 3) {
      throw new IllegalStateException("User already has maximum number of books borrowed!");
    }
  }

  private void verifyUserHasNotBorrowedBook(User user, long bookId) throws IllegalStateException {
    if (user.getBorrowedBooks().stream().anyMatch(book -> book.getId() == bookId)) {
      throw new IllegalStateException("User already borrowed the book");
    }
  }
}
