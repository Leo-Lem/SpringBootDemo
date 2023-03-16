package leolem.demo.web.dto;

import java.util.Optional;

import lombok.Data;

@Data
public class UpdateUserRequest {
  private final Optional<String> name;
  private final Optional<String> firstName;
  private final Optional<String> email;
  private final Optional<String> password;
}
