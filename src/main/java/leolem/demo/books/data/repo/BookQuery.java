package leolem.demo.books.data.repo;

import java.time.LocalDate;
import java.util.Optional;

import lombok.*;

@Value
@Builder
public class BookQuery {
  private String title;
  private String author;
  private Boolean isAvailable;
  private LocalDate publishedAfter;
  private LocalDate publishedBefore;

  public Optional<String> getTitle() {
    return Optional.ofNullable(title);
  }

  public Optional<String> getAuthor() {
    return Optional.ofNullable(author);
  }

  public Optional<Boolean> getIsAvailable() {
    return Optional.ofNullable(isAvailable);
  }

  public Optional<LocalDate> getPublishedAfter() {
    return Optional.ofNullable(publishedAfter);
  }

  public Optional<LocalDate> getPublishedBefore() {
    return Optional.ofNullable(publishedBefore);
  }
}
