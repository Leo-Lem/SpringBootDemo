package leolem.demo.users.dto;

import lombok.Data;

@Data
public class SignInRequest {

  private String email;
  private String password;

}
