package leolem.demo.security;

import java.util.function.Supplier;

import org.springframework.lang.Nullable;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import leolem.demo.users.data.User;
import lombok.val;

@Component
public class UserIDAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

  @Override
  @Nullable
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    val principalID = ((User) supplier.get().getPrincipal()).getId();
    val requestID = Long.parseLong(context.getVariables().get("userID"));

    return new AuthorizationDecision(principalID == requestID);
  }

}
