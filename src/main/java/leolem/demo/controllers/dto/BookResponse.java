package leolem.demo.controllers.dto;

import java.util.Date;

import leolem.demo.model.Book;

public class BookResponse {

  private int id;
  private String title;
  private String author;
  private Date publication;
  private int numberOfInstances;

  public BookResponse(Book book) {
    this.id = book.getId();
    this.title = book.getTitle();
    this.author = book.getAuthor();
    this.publication = book.getPublication();
    this.numberOfInstances = book.getNumberOfInstances();
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public Date getPublication() {
    return publication;
  }

  public int getNumberOfInstances() {
    return numberOfInstances;
  }

}
