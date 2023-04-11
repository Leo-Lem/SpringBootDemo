package leolem.demo.borrow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import leolem.demo.books.data.*;
import leolem.demo.users.data.*;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class BorrowService {

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private final BookRepository bookRepository;

  public void borrowBook(long bookId, long userId) throws EntityNotFoundException, IllegalStateException {
    val user = fetchUser(userId);
    val book = fetchBook(bookId);

    if (isBookBorrowedByUser(bookId, user))
      throw new IllegalStateException("User already borrowed the book.");
    verifyBookIsInStock(book);
    verifyUserBorrowLimitIsNotReached(user);

    user.getBorrowedBooks().add(book);
    userRepository.save(user);
  }

  public boolean verifyStatus(long bookId, long userId) throws EntityNotFoundException, IllegalStateException {
    val user = fetchUser(userId);

    return isBookBorrowedByUser(bookId, user);
  }

  public void returnBook(long bookId, long userId) throws EntityNotFoundException {
    val user = fetchUser(userId);
    val book = fetchBook(bookId);

    if (!isBookBorrowedByUser(bookId, user))
      throw new EntityNotFoundException("User has not borrowed the book.");

    if (user.getBorrowedBooks().remove(book)) {
      userRepository.save(user);
    }
  }

  private boolean isBookBorrowedByUser(long bookId, User user) {
    return user.getBorrowedBooks().stream().anyMatch(book -> book.getId() == bookId);
  }

  private void verifyUserBorrowLimitIsNotReached(User user) throws IllegalStateException {
    if (user.getBorrowedBooks().size() >= 3) {
      throw new IllegalStateException("User already has maximum number of books borrowed!");
    }
  }

  private void verifyBookIsInStock(Book book) throws IllegalStateException {
    if (book.getBorrowableCopies() < 1) {
      throw new IllegalStateException("There are no borrowable books!");
    }
  }

  private User fetchUser(long userId) throws EntityNotFoundException {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Cannot find User with id: " + userId));
  }

  private Book fetchBook(long bookId) throws EntityNotFoundException {
    return bookRepository
        .findById(bookId)
        .orElseThrow(() -> new EntityNotFoundException("Cannot find Book with id: " + bookId));
  }

}
