package leolem.demo.unit;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

import leolem.demo.books.data.Book;
import leolem.demo.books.data.BookRepository;
import leolem.demo.borrow.business.BorrowService;
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
    void testBorrowingBook() {
        val book = mock(Book.class);
        val user = mock(User.class);

        when(book.getId()).thenReturn(1L);
        when(book.getBorrowableCopies()).thenReturn(1);
        when(book.getBorrowers()).thenReturn(new ArrayList<>(List.of(user)));
        when(user.getId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        borrowService.borrowBook(1, 1);
    }

    @Test
    void testVerifyingStatus() {
        val book = mock(Book.class);
        val user = mock(User.class);

        when(book.getId()).thenReturn(1L);
        when(user.getId()).thenReturn(1L);
        when(user.getBorrowedBooks()).thenReturn(new ArrayList<>(List.of(book)));
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertTrue(borrowService.verifyStatus(1, 1));
        assertFalse(borrowService.verifyStatus(2, 1));
    }

    @Test
    void testReturningBook() {
        val book = mock(Book.class);
        val user = mock(User.class);

        when(book.getId()).thenReturn(1L);
        when(user.getId()).thenReturn(1L);
        when(user.getBorrowedBooks()).thenReturn(new ArrayList<>(List.of(book)));
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        borrowService.returnBook(1, 1);
    }

}
