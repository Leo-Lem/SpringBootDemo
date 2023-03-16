package leolem.demo.web.dto;

import lombok.Data;

@Data
public class RegisterUserRequest {
  private final String name;
  private final String firstName;
  private final String email;
  private final String password;
}
