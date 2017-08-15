package co.com.fixitgroup.repository;

import co.com.fixitgroup.domain.WorkTypeCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WorkTypeCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkTypeCategoryRepository extends JpaRepository<WorkTypeCategory,Long> {
    
}
