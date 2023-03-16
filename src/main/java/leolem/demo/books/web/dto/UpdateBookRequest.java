package leolem.demo.books.web.dto;

import java.util.Optional;
import lombok.Data;

@Data
public class UpdateBookRequest {
  private final Optional<String> title;
  private final Optional<String> author;
  private final Optional<String> publishedOn;
  private final Optional<Integer> availableCopies;
}