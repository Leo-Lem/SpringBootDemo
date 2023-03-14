package leolem.demo.model;

import java.util.*;
import jakarta.persistence.*;

@Entity(name = "book")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "title")
  private String title;

  @Column(name = "author")
  private String author;

  @Column(name = "publication")
  private Date publication;

  @Column(name = "numberOfInstances")
  private int numberOfInstances;

  @ManyToMany(mappedBy = "borrowedBooks")
  private List<User> borrowers;

  public Book() {
  }

  public Book(String title, String author, Date publication, int numberOfInstances) {
    this.title = title;
    this.author = author;
    this.publication = publication;
    this.numberOfInstances = numberOfInstances;
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

  public void setTitle(String title) {
    this.title = title;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setPublication(Date publication) {
    this.publication = publication;
  }

  public void setNumberOfInstances(int i) {
    numberOfInstances = i;
  }

  public void addBorrower(User borrower) {
    borrowers.add(borrower);
  }

}
