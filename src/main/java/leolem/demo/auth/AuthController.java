package leolem.demo.auth;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import leolem.demo.auth.dto.JWTResponse;
import leolem.demo.auth.dto.LoginRequest;
import leolem.demo.security.jwt.JWTUtils;
import leolem.demo.users.data.User;
import leolem.demo.users.data.UserRepository;
import leolem.demo.users.web.dto.RegisterUserRequest;
import leolem.demo.users.web.dto.UserResponse;
import lombok.val;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JWTUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest request) {

		val token = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		val jwt = jwtUtils.generateJWTToken(authentication);
		val user = (User) authentication.getPrincipal();

		val response = JWTResponse.builder()
				.token(jwt)
				.id(user.getId())
				.email(user.getEmail())
				.roles(user.getRoles())
				.build();

		return ResponseEntity.ok(response);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Validated @RequestBody RegisterUserRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			return ResponseEntity
					.status(409)
					.body("Error: Email is already in use!");
		}

		val user = User.builder()
				.name(request.getName())
				.firstName(request.getFirstName())
				.email(request.getEmail())
				.password(encoder.encode(request.getPassword()))
				.roles(Set.of(User.Role.USER))
				.build();

		userRepository.save(user);
		val userResponse = new UserResponse(user);
		return ResponseEntity.ok().body(userResponse);
	}
}
