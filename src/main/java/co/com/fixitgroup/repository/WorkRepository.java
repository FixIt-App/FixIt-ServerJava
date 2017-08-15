package co.com.fixitgroup.repository;

import co.com.fixitgroup.domain.Work;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Work entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkRepository extends JpaRepository<Work,Long> {
    
}
