package co.com.fixitgroup.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Work.
 */
@Entity
@Table(name = "work")
public class Work implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_time", nullable = false)
    private ZonedDateTime time;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "asap", nullable = false)
    private Boolean asap;

    @OneToMany(mappedBy = "works")
    @JsonIgnore
    private Set<Worker> workers = new HashSet<>();

    @OneToMany(mappedBy = "works")
    @JsonIgnore
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "works")
    @JsonIgnore
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "works")
    @JsonIgnore
    private Set<WorkType> worktypes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public Work time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public Work description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isAsap() {
        return asap;
    }

    public Work asap(Boolean asap) {
        this.asap = asap;
        return this;
    }

    public void setAsap(Boolean asap) {
        this.asap = asap;
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    public Work workers(Set<Worker> workers) {
        this.workers = workers;
        return this;
    }

    public Work addWorker(Worker worker) {
        this.workers.add(worker);
        worker.setWorks(this);
        return this;
    }

    public Work removeWorker(Worker worker) {
        this.workers.remove(worker);
        worker.setWorks(null);
        return this;
    }

    public void setWorkers(Set<Worker> workers) {
        this.workers = workers;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public Work customers(Set<Customer> customers) {
        this.customers = customers;
        return this;
    }

    public Work addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setWorks(this);
        return this;
    }

    public Work removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setWorks(null);
        return this;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public Work addresses(Set<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    public Work addAddress(Address address) {
        this.addresses.add(address);
        address.setWorks(this);
        return this;
    }

    public Work removeAddress(Address address) {
        this.addresses.remove(address);
        address.setWorks(null);
        return this;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<WorkType> getWorktypes() {
        return worktypes;
    }

    public Work worktypes(Set<WorkType> workTypes) {
        this.worktypes = workTypes;
        return this;
    }

    public Work addWorktype(WorkType workType) {
        this.worktypes.add(workType);
        workType.setWorks(this);
        return this;
    }

    public Work removeWorktype(WorkType workType) {
        this.worktypes.remove(workType);
        workType.setWorks(null);
        return this;
    }

    public void setWorktypes(Set<WorkType> workTypes) {
        this.worktypes = workTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Work work = (Work) o;
        if (work.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), work.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Work{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", description='" + getDescription() + "'" +
            ", asap='" + isAsap() + "'" +
            "}";
    }
}
