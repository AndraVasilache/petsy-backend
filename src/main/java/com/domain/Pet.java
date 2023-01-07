package com.domain;

import com.domain.enumeration.Category;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Pet.
 */
@Entity
@Table(name = "pet")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "for_sale")
    private Boolean forSale;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "race")
    private String race;

    @Column(name = "name")
    private String name;

    @Column(name = "owner")
    private Long owner;

    @Column(name = "picture")
    private String picture;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getForSale() {
        return this.forSale;
    }

    public Pet forSale(Boolean forSale) {
        this.setForSale(forSale);
        return this;
    }

    public void setForSale(Boolean forSale) {
        this.forSale = forSale;
    }

    public Category getCategory() {
        return this.category;
    }

    public Pet category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getRace() {
        return this.race;
    }

    public Pet race(String race) {
        this.setRace(race);
        return this;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getName() {
        return this.name;
    }

    public Pet name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwner() {
        return this.owner;
    }

    public Pet owner(Long owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public String getPicture() {
        return this.picture;
    }

    public Pet picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pet)) {
            return false;
        }
        return id != null && id.equals(((Pet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pet{" +
            "id=" + getId() +
            ", forSale='" + getForSale() + "'" +
            ", category='" + getCategory() + "'" +
            ", race='" + getRace() + "'" +
            ", name='" + getName() + "'" +
            ", owner=" + getOwner() +
            ", picture='" + getPicture() + "'" +
            "}";
    }
}
