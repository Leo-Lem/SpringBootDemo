package leolem.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import leolem.demo.users.data.User;
import leolem.demo.users.data.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with email: " + email));

    return user;
  }

}