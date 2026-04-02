package com.skif.familywishlist.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Wish")
public class Wish {

    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "Title",  nullable = false, length = 50)
    private String title;

    @Column(name = "Description",   nullable = false, length = 500)
    private String description;

    @Column(name = "Created_Date")
    private OffsetDateTime createdAt;

    @Column(name = "Fulfilled")
    private OffsetDateTime fulfilledAt;

    @Column(name = "Fulfilled_Flag", nullable = false)
    private boolean fulfilled;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    @JsonBackReference("person-wishes")
    private Person owner;

    public Wish() {}

    public Wish(String title, String description, Person owner) {
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.fulfilledAt = null;
        this.fulfilled = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getFulfilledAt() {
        return fulfilledAt;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public void markAsFulfilled() {
        this.fulfilled = true;
        this.fulfilledAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void markAsUnfulfilled() {
        this.fulfilled = false;
        this.fulfilledAt = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wish)) return false;
        Wish wish = (Wish) o;
        return Objects.equals(id, wish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
