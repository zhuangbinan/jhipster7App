package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Book.
 */
@Entity
@Table(name = "book")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "publisher_name")
    private String publisherName;

    @Column(name = "publisher_date")
    private Instant publisherDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "books" }, allowSetters = true)
    private Author author;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Book price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPublisherName() {
        return this.publisherName;
    }

    public Book publisherName(String publisherName) {
        this.publisherName = publisherName;
        return this;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public Instant getPublisherDate() {
        return this.publisherDate;
    }

    public Book publisherDate(Instant publisherDate) {
        this.publisherDate = publisherDate;
        return this;
    }

    public void setPublisherDate(Instant publisherDate) {
        this.publisherDate = publisherDate;
    }

    public Author getAuthor() {
        return this.author;
    }

    public Book author(Author author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return id != null && id.equals(((Book) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", publisherName='" + getPublisherName() + "'" +
            ", publisherDate='" + getPublisherDate() + "'" +
            "}";
    }
}
