package leolem.demo.users.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import leolem.demo.users.data.User;
import lombok.*;

@Data
@RequiredArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserResponse {

  private final long id;
  private final String name;
  private final String email;
  private final Set<User.Role> roles;
  private final String token;
  private final String type = "Bearer";

  public UserResponse(User user, String token) {
    this(user.getId(), user.getName(), user.getEmail(), user.getRoles(), token);
  }
}
