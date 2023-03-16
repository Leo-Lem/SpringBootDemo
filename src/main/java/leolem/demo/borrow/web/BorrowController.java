package leolem.demo.borrow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.borrow.business.BorrowService;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
  @Autowired
  private BorrowService borrowService;

  @GetMapping("/{bookId}/{userId}")
  ResponseEntity<?> verifyStatus(@PathVariable long bookId, @PathVariable long userId) {
    try {
      return borrowService.verifyStatus(bookId, userId)
          ? ResponseEntity.ok().build()
          : ResponseEntity.notFound().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/{bookId}/{userId}")
  ResponseEntity<?> borrowBook(@PathVariable long bookId, @PathVariable long userId) {
    try {
      borrowService.borrowBook(bookId, userId);
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{bookId}/{userId}")
  ResponseEntity<?> returnBook(@PathVariable long bookId, @PathVariable long userId) {
    try {
      borrowService.returnBook(bookId, userId);
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
