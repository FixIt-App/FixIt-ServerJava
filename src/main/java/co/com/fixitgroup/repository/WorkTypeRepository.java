package co.com.fixitgroup.repository;

import co.com.fixitgroup.domain.WorkType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the WorkType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkTypeRepository extends JpaRepository<WorkType,Long> {
    
    @Query("select distinct work_type from WorkType work_type left join fetch work_type.categories left join fetch work_type.workers")
    List<WorkType> findAllWithEagerRelationships();

    @Query("select work_type from WorkType work_type left join fetch work_type.categories left join fetch work_type.workers where work_type.id =:id")
    WorkType findOneWithEagerRelationships(@Param("id") Long id);
    
}
