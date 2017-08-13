package co.com.fixitgroup.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A WorkTypeCategory.
 */
@Entity
@Table(name = "work_type_category")
public class WorkTypeCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "jhi_order", nullable = false)
    private Integer order;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private Set<WorkType> workTypes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public WorkTypeCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public WorkTypeCategory order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Set<WorkType> getWorkTypes() {
        return workTypes;
    }

    public WorkTypeCategory workTypes(Set<WorkType> workTypes) {
        this.workTypes = workTypes;
        return this;
    }

    public WorkTypeCategory addWorkTypes(WorkType workType) {
        this.workTypes.add(workType);
        workType.getCategories().add(this);
        return this;
    }

    public WorkTypeCategory removeWorkTypes(WorkType workType) {
        this.workTypes.remove(workType);
        workType.getCategories().remove(this);
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
        WorkTypeCategory workTypeCategory = (WorkTypeCategory) o;
        if (workTypeCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workTypeCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkTypeCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", order='" + getOrder() + "'" +
            "}";
    }
}
