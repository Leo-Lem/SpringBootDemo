package leolem.demo.controllers.dto;

import java.sql.Date;

import leolem.demo.model.Book;

public class BookRequest {
  private String title;
  private String author;
  private Date publishedOn;
  private int currentlyAvailableNumber;

  private BookRequest(String title, String author, Date publishedOn, int currentlyAvailableNumber) {
    this.title = title;
    this.author = author;
    this.publishedOn = publishedOn;
    this.currentlyAvailableNumber = currentlyAvailableNumber;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public Date getPublishedOn() {
    return publishedOn;
  }

  public int getCurrentlyAvailableNumber() {
    return currentlyAvailableNumber;
  }

  public Book getBook() {
    return new Book(getTitle(), getAuthor(), getPublishedOn(), getCurrentlyAvailableNumber());
  }

}