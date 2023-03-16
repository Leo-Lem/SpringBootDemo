package leolem.demo.web.dto;

import lombok.Data;

@Data
public class CreateBookRequest {
  private final String title;
  private final String author;
  private final String publishedOn;
  private final int availableCopies;
}