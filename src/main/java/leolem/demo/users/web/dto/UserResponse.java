package leolem.demo.users.web.dto;

import leolem.demo.users.data.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserResponse {
  private final long id;
  private final String name;
  private final String firstName;
  private final String email;

  public UserResponse(User user) {
    this(user.getId(), user.getName(), user.getFirstName(), user.getEmail());
  }
}
