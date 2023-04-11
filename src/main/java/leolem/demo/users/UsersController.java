package leolem.demo.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.*;
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
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse registerUser(@Validated @RequestBody SignUpRequest request) {
    try {
      val user = userService.create(request.getName(), request.getEmail(), request.getPassword());

      val token = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
      Authentication authentication = authenticationManager.authenticate(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      return new UserResponse(user, jwtUtils.generateJWTToken(authentication));
    } catch (EntityExistsException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
    }
  }

  @PostMapping("/signin")
  public UserResponse authenticateUser(@Validated @RequestBody SignInRequest request) {

    val token = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
    Authentication authentication = authenticationManager.authenticate(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return new UserResponse((User) authentication.getPrincipal(), jwtUtils.generateJWTToken(authentication));
  }

  @GetMapping
  public UserResponse fetchUserWithEmail(@RequestParam(name = "email", required = true) String email) {
    try {
      val user = userService.readByEmail(email);
      val userResponse = new UserResponse(user, null);
      return userResponse;
    } catch (EntityNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public UserResponse fetchUser(@PathVariable("id") long id) {
    try {
      val user = userService.readById(id);
      return new UserResponse(user, null);
    } catch (EntityNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public UserResponse updateUser(@PathVariable("id") int id, @Validated @RequestBody UpdateUserRequest request) {
    try {
      return new UserResponse(
          userService.update(id, request.getName(), request.getEmail(), request.getPassword()),
          null);
    } catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (EntityNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") long id) {

    try {
      userService.delete(id);
    } catch (EntityNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

}
