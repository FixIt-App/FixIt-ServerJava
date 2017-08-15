package co.com.fixitgroup.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Worker.
 */
@Entity
@Table(name = "worker")
public class Worker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private String documentId;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Size(min = 2)
    @Column(name = "rh", nullable = false)
    private String rh;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "worker")
    @JsonIgnore
    private Set<Work> works = new HashSet<>();

    @ManyToMany(mappedBy = "workers")
    @JsonIgnore
    private Set<WorkType> workTypes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public Worker documentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPhone() {
        return phone;
    }

    public Worker phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRh() {
        return rh;
    }

    public Worker rh(String rh) {
        this.rh = rh;
        return this;
    }

    public void setRh(String rh) {
        this.rh = rh;
    }

    public User getUser() {
        return user;
    }

    public Worker user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Work> getWorks() {
        return works;
    }

    public Worker works(Set<Work> works) {
        this.works = works;
        return this;
    }

    public Worker addWorks(Work work) {
        this.works.add(work);
        work.setWorker(this);
        return this;
    }

    public Worker removeWorks(Work work) {
        this.works.remove(work);
        work.setWorker(null);
        return this;
    }

    public void setWorks(Set<Work> works) {
        this.works = works;
    }

    public Set<WorkType> getWorkTypes() {
        return workTypes;
    }

    public Worker workTypes(Set<WorkType> workTypes) {
        this.workTypes = workTypes;
        return this;
    }

    public Worker addWorkTypes(WorkType workType) {
        this.workTypes.add(workType);
        workType.getWorkers().add(this);
        return this;
    }

    public Worker removeWorkTypes(WorkType workType) {
        this.workTypes.remove(workType);
        workType.getWorkers().remove(this);
        return this;
    }

    public void setWorkTypes(Set<WorkType> workTypes) {
        this.workTypes = workTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Worker worker = (Worker) o;
        if (worker.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), worker.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Worker{" +
            "id=" + getId() +
            ", documentId='" + getDocumentId() + "'" +
            ", phone='" + getPhone() + "'" +
            ", rh='" + getRh() + "'" +
            "}";
    }
}
