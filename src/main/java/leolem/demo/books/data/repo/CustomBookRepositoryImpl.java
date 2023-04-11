package leolem.demo.books.data.repo;

import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import leolem.demo.books.data.Book;
import lombok.val;

public class CustomBookRepositoryImpl implements CustomBookRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Set<Book> findByQuery(BookQuery bookQuery) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    val query = cb.createQuery(Book.class);
    val book = query.from(Book.class);
    Set<Predicate> predicates = new HashSet<>();

    bookQuery.getTitle().ifPresent(unwrapped -> predicates.add(cb.like(book.get("title"), unwrapped)));
    bookQuery.getAuthor().ifPresent(unwrapped -> predicates.add(cb.like(book.get("author"), unwrapped)));

    Path<Integer> availableCopiesPath = book.get("availableCopies");
    bookQuery.getIsAvailable().ifPresent(unwrapped -> predicates.add(
        unwrapped ? cb.greaterThan(availableCopiesPath, 0) : cb.lessThan(availableCopiesPath, 1)));

    Path<LocalDate> publicationPath = book.get("publishedOn");
    bookQuery.getPublishedAfter()
        .ifPresent(unwrapped -> predicates.add(cb.greaterThanOrEqualTo(publicationPath, unwrapped)));
    bookQuery.getPublishedBefore()
        .ifPresent(unwrapped -> predicates.add(cb.lessThanOrEqualTo(publicationPath, unwrapped)));

    query.where(cb.and(predicates.toArray(new Predicate[0])));

    return new HashSet<>(entityManager.createQuery(query).getResultList());
  }

}
