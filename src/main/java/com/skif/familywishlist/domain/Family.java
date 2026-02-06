package com.skif.familywishlist.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Family")
public class Family {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "Name",  unique = true, nullable = false)
    private String name;

    @OneToOne(mappedBy = "family")
    private User user;

    @OneToMany(mappedBy = "family",  cascade = CascadeType.ALL,  orphanRemoval = true)
    @JsonManagedReference("family-members")
    private List<Person> members = new ArrayList<>();

    public Family() {}

    public Family(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getMembers() {
        return members;
    }

    public void addMember(Person person) {
        members.add(person);
        person.setFamily(this);
    }

    public void removeMember(Person person) {
        members.remove(person);
        person.setFamily(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Family)) return false;
        Family family = (Family) o;
        return Objects.equals(id, family.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
