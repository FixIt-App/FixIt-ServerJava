package co.com.fixitgroup.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import co.com.fixitgroup.domain.enumeration.WorkState;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private WorkState state = WorkState.ORDERED;

    @ManyToOne
    private Worker worker;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Address address;

    @ManyToOne
    private WorkType worktype;

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

    public WorkState getState() {
        return state;
    }

    public Work state(WorkState state) {
        this.state = state;
        return this;
    }

    public void setState(WorkState state) {
        this.state = state;
    }

    public Worker getWorker() {
        return worker;
    }

    public Work worker(Worker worker) {
        this.worker = worker;
        return this;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Work customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }

    public Work address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public WorkType getWorktype() {
        return worktype;
    }

    public Work worktype(WorkType workType) {
        this.worktype = workType;
        return this;
    }

    public void setWorktype(WorkType workType) {
        this.worktype = workType;
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
            ", state='" + getState() + "'" +
            "}";
    }
}
