package leolem.demo.business;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.data.model.*;
import leolem.demo.data.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowService {
  private final UserRepository userRepository;
  private final BookRepository bookRepository;

  public void borrow(int bookId, int userId) throws EntityNotFoundException {
    User user = userRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Cannot find User with id: " + bookId));

    Book book = bookRepository
        .findById(bookId)
        .orElseThrow(() -> new EntityNotFoundException("Cannot find Book with id: " + bookId));

    verifyUserHasNotBorrowedBook(user, bookId);
    verifyBookIsInStock(book);
    verifyUserBorrowLimitIsNotReached(user);

    updateBookStatus(user, book);

    bookRepository.save(book);
  }

  private void verifyUserHasNotBorrowedBook(User user, long bookId) throws IllegalStateException {
    if (user.getBorrowedBooks().stream().anyMatch(book -> book.getId() == bookId)) {
      throw new IllegalStateException("User already borrowed the book.");
    }
  }

  private void verifyUserBorrowLimitIsNotReached(User user) throws IllegalStateException {
    if (user.getBorrowedBooks().size() >= 3) {
      throw new IllegalStateException("User already has maximum number of books borrowed!");
    }
  }

  private void verifyBookIsInStock(Book book) {
    if (book.getBorrowableCopies() < 1) {
      throw new IllegalStateException("There are no borrowable books!");
    }
  }

  private void updateBookStatus(User user, Book book) {
    user.getBorrowedBooks().add(book);
  }

}
