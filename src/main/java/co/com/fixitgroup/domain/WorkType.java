package co.com.fixitgroup.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import co.com.fixitgroup.domain.enumeration.PriceType;

/**
 * A WorkType.
 */
@Entity
@Table(name = "work_type")
public class WorkType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "icon", nullable = false)
    private String icon;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "price_type", nullable = false)
    private PriceType priceType;

    @NotNull
    @Column(name = "price", nullable = false)
    private Long price;

    @NotNull
    @Column(name = "jhi_order", nullable = false)
    private Integer order;

    @NotNull
    @Column(name = "url_name", nullable = false)
    private String urlName;

    @ManyToMany
    @JoinTable(name = "work_type_categories",
               joinColumns = @JoinColumn(name="work_types_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="categories_id", referencedColumnName="id"))
    private Set<WorkTypeCategory> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "work_type_workers",
               joinColumns = @JoinColumn(name="work_types_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="workers_id", referencedColumnName="id"))
    private Set<Worker> workers = new HashSet<>();

    @OneToMany(mappedBy = "worktype")
    @JsonIgnore
    private Set<Work> works = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public WorkType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public WorkType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public WorkType icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public WorkType priceType(PriceType priceType) {
        this.priceType = priceType;
        return this;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public Long getPrice() {
        return price;
    }

    public WorkType price(Long price) {
        this.price = price;
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getOrder() {
        return order;
    }

    public WorkType order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getUrlName() {
        return urlName;
    }

    public WorkType urlName(String urlName) {
        this.urlName = urlName;
        return this;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public Set<WorkTypeCategory> getCategories() {
        return categories;
    }

    public WorkType categories(Set<WorkTypeCategory> workTypeCategories) {
        this.categories = workTypeCategories;
        return this;
    }

    public WorkType addCategories(WorkTypeCategory workTypeCategory) {
        this.categories.add(workTypeCategory);
        workTypeCategory.getWorkTypes().add(this);
        return this;
    }

    public WorkType removeCategories(WorkTypeCategory workTypeCategory) {
        this.categories.remove(workTypeCategory);
        workTypeCategory.getWorkTypes().remove(this);
        return this;
    }

    public void setCategories(Set<WorkTypeCategory> workTypeCategories) {
        this.categories = workTypeCategories;
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    public WorkType workers(Set<Worker> workers) {
        this.workers = workers;
        return this;
    }

    public WorkType addWorkers(Worker worker) {
        this.workers.add(worker);
        worker.getWorkTypes().add(this);
        return this;
    }

    public WorkType removeWorkers(Worker worker) {
        this.workers.remove(worker);
        worker.getWorkTypes().remove(this);
        return this;
    }

    public void setWorkers(Set<Worker> workers) {
        this.workers = workers;
    }

    public Set<Work> getWorks() {
        return works;
    }

    public WorkType works(Set<Work> works) {
        this.works = works;
        return this;
    }

    public WorkType addWorks(Work work) {
        this.works.add(work);
        work.setWorktype(this);
        return this;
    }

    public WorkType removeWorks(Work work) {
        this.works.remove(work);
        work.setWorktype(null);
        return this;
    }

    public void setWorks(Set<Work> works) {
        this.works = works;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkType workType = (WorkType) o;
        if (workType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", icon='" + getIcon() + "'" +
            ", priceType='" + getPriceType() + "'" +
            ", price='" + getPrice() + "'" +
            ", order='" + getOrder() + "'" +
            ", urlName='" + getUrlName() + "'" +
            "}";
    }
}
