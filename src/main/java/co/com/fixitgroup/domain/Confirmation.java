package co.com.fixitgroup.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import co.com.fixitgroup.domain.enumeration.CONFIRMATION_TYPE;

/**
 * A Confirmation.
 */
@Entity
@Table(name = "confirmation")
public class Confirmation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private CONFIRMATION_TYPE type;

    @NotNull
    @Column(name = "expires", nullable = false)
    private ZonedDateTime expires;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "state", nullable = false)
    private Boolean state;

    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CONFIRMATION_TYPE getType() {
        return type;
    }

    public Confirmation type(CONFIRMATION_TYPE type) {
        this.type = type;
        return this;
    }

    public void setType(CONFIRMATION_TYPE type) {
        this.type = type;
    }

    public ZonedDateTime getExpires() {
        return expires;
    }

    public Confirmation expires(ZonedDateTime expires) {
        this.expires = expires;
        return this;
    }

    public void setExpires(ZonedDateTime expires) {
        this.expires = expires;
    }

    public String getCode() {
        return code;
    }

    public Confirmation code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isState() {
        return state;
    }

    public Confirmation state(Boolean state) {
        this.state = state;
        return this;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Confirmation customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Confirmation confirmation = (Confirmation) o;
        if (confirmation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), confirmation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Confirmation{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", expires='" + getExpires() + "'" +
            ", code='" + getCode() + "'" +
            ", state='" + isState() + "'" +
            "}";
    }
}
