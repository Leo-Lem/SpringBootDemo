package leolem.demo.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import leolem.demo.security.jwt.JWTUtils;
import leolem.demo.users.data.User;
import leolem.demo.users.dto.*;
import lombok.val;

@RestController
@RequestMapping("/users")
public class UsersController {

  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JWTUtils jwtUtils;

  @PostMapping
  public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpRequest request) {
    try {
      val user = userService.create(request.getName(), request.getEmail(), request.getPassword());
      val response = new UserResponse(user);
      return ResponseEntity.ok().body(response);
    } catch (EntityExistsException e) {
      return ResponseEntity
          .status(409)
          .body("Email is already in use!");
    }
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Validated @RequestBody SignInRequest request) {

    val token = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
    Authentication authentication = authenticationManager.authenticate(token);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    val jwt = jwtUtils.generateJWTToken(authentication);
    val user = (User) authentication.getPrincipal();

    val response = JWTResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .roles(user.getRoles())
        .token(jwt)
        .build();

    return ResponseEntity.ok(response);
  }

  @GetMapping
  ResponseEntity<?> fetchUserWithEmail(@RequestParam(name = "email", required = true) String email) {
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
  ResponseEntity<?> updateUser(@PathVariable("id") int id, @Validated @RequestBody UpdateUserRequest request) {
    try {
      val user = userService.update(id, request.getName(), request.getEmail(), request.getPassword());
      val response = new UserResponse(user);
      return ResponseEntity.ok().body(response);
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
