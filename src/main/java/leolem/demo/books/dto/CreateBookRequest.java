package leolem.demo.books.dto;

import lombok.Data;

@Data
public class CreateBookRequest {
  private final String title;
  private final String author;
  private final String publishedOn;
  private final int availableCopies;
}