package leolem.demo.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.books.data.Book;
import leolem.demo.books.data.BookRepository;
import leolem.demo.borrow.BorrowService;
import leolem.demo.users.data.User;
import leolem.demo.users.data.UserRepository;
import lombok.val;

public class BorrowServiceTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BorrowService borrowService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenBookAndUserExist_whenBorrowingBook_thenBorrowingDoesNotThrow() {
        val book = mock(Book.class);
        val user = mock(User.class);

        when(book.getId()).thenReturn(1L);
        when(book.getBorrowableCopies()).thenReturn(1);
        when(book.getBorrowers()).thenReturn(new ArrayList<>(List.of(user)));
        when(user.getId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertDoesNotThrow(() -> borrowService.borrowBook(1, 1));
    }

    @Test
    void givenBookIsBorrowed_whenVerifyingStatus_thenReturnsTrue() {
        val book = mock(Book.class);
        val user = mock(User.class);

        when(book.getId()).thenReturn(1L);
        when(user.getId()).thenReturn(1L);
        when(user.getBorrowedBooks()).thenReturn(new ArrayList<>(List.of(book)));
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertTrue(borrowService.verifyStatus(1, 1));
    }

    @Test
    void givenBookIsNotBorrowed_whenVerifyingStatus_thenReturnsFalse() {
        val book = mock(Book.class);
        val user = mock(User.class);

        when(book.getId()).thenReturn(1L);
        when(user.getId()).thenReturn(1L);
        when(user.getBorrowedBooks()).thenReturn(new ArrayList<>(List.of(book)));
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertFalse(borrowService.verifyStatus(2, 1));
    }

    @Test
    void givenBookIsNotBorrowed_whenReturningBook_thenDoesThrow() {
        val book = mock(Book.class);
        val user = mock(User.class);

        when(book.getId()).thenReturn(1L);
        when(user.getId()).thenReturn(1L);
        when(user.getBorrowedBooks()).thenReturn(new ArrayList<>(List.of(book)));
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> borrowService.returnBook(2, 1));
    }

    @Test
    void givenBookIsBorrowed_whenReturningBook_thenDoesNotThrow() {
        val book = mock(Book.class);
        val user = mock(User.class);

        when(book.getId()).thenReturn(1L);
        when(user.getId()).thenReturn(1L);
        when(user.getBorrowedBooks()).thenReturn(new ArrayList<>(List.of(book)));
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertDoesNotThrow(() -> borrowService.returnBook(1, 1));
    }

}
