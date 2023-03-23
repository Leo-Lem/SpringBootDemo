package leolem.demo.users.dto;

import lombok.Data;

@Data
public class SignUpRequest {
  private final String name;
  private final String email;
  private final String password;
}
