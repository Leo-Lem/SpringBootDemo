package leolem.demo.users.dto;

import java.util.Set;

import leolem.demo.users.data.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JWTResponse {

  private Long id;
  private String email;
  private Set<User.Role> roles;
  private String token;
  private final String type = "Bearer";

}
