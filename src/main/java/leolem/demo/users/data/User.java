package leolem.demo.users.data;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import leolem.demo.books.data.Book;
import lombok.*;

@Entity(name = "_user")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "roles")
  private Set<Role> roles;

  @ManyToMany
  @JoinTable(name = "borrowed_book", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
  private List<Book> borrowedBooks;

  @Override
  public Set<GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(Role::getValue)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private String value;

    private Role(String value) {
      this.value = value;
    }

    public String getValue() {
      return "ROLE_" + value;
    }
  }

}