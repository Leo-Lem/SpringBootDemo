package leolem.demo.users.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import leolem.demo.users.business.UserService;
import leolem.demo.users.web.dto.*;
import lombok.val;

@RestController
@RequestMapping("/users")
public class UsersRestController {

  @Autowired
  private UserService userService;

  @GetMapping
  ResponseEntity<?> fetchUserWithEmail(@RequestParam(name = "email", required = false) String email) {
    if (email == null)
      return ResponseEntity.badRequest().body("email parameter is required.");

    try {
      val user = userService.readByEmail(email);
      val userResponse = new UserResponse(user);
      return ResponseEntity.ok().body(userResponse);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{id}")
  ResponseEntity<?> fetchUser(@PathVariable("id") long id) {
    try {
      val user = userService.readById(id);
      val userResponse = new UserResponse(user);
      return ResponseEntity.ok().body(userResponse);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  ResponseEntity<?> updateUser(@PathVariable("id") int id, @RequestBody UpdateUserRequest request) {
    try {
      val user = userService.update(
          id, request.getName(), request.getFirstName(), request.getEmail(), request.getPassword());

      val userResponse = new UserResponse(
          user.getId(), user.getName(), user.getFirstName(), user.getEmail(), user.getRoles());

      return ResponseEntity.ok().body(userResponse);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Invalid argument: " + e.getMessage());
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> delete(@PathVariable("id") long id) {
    try {
      userService.delete(id);
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

}
