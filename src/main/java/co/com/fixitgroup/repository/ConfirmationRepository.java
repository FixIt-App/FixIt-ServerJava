package co.com.fixitgroup.repository;

import co.com.fixitgroup.domain.Confirmation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Confirmation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation,Long> {
    
}
