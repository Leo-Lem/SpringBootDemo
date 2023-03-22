package leolem.demo.users.dto;

import java.util.Set;

import leolem.demo.users.data.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserResponse {
  private final long id;
  private final String name;
  private final String email;
  private final Set<User.Role> roles;

  public UserResponse(User user) {
    this(user.getId(), user.getName(), user.getEmail(), user.getRoles());
  }
}
