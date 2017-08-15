package co.com.fixitgroup.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private Set<Work> works = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private Set<Confirmation> confirmations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public Customer phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public Customer user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public Customer addresses(Set<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    public Customer addAddresses(Address address) {
        this.addresses.add(address);
        address.setCustomer(this);
        return this;
    }

    public Customer removeAddresses(Address address) {
        this.addresses.remove(address);
        address.setCustomer(null);
        return this;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Work> getWorks() {
        return works;
    }

    public Customer works(Set<Work> works) {
        this.works = works;
        return this;
    }

    public Customer addWorks(Work work) {
        this.works.add(work);
        work.setCustomer(this);
        return this;
    }

    public Customer removeWorks(Work work) {
        this.works.remove(work);
        work.setCustomer(null);
        return this;
    }

    public void setWorks(Set<Work> works) {
        this.works = works;
    }

    public Set<Confirmation> getConfirmations() {
        return confirmations;
    }

    public Customer confirmations(Set<Confirmation> confirmations) {
        this.confirmations = confirmations;
        return this;
    }

    public Customer addConfirmations(Confirmation confirmation) {
        this.confirmations.add(confirmation);
        confirmation.setCustomer(this);
        return this;
    }

    public Customer removeConfirmations(Confirmation confirmation) {
        this.confirmations.remove(confirmation);
        confirmation.setCustomer(null);
        return this;
    }

    public void setConfirmations(Set<Confirmation> confirmations) {
        this.confirmations = confirmations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
