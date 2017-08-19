package co.com.fixitgroup.repository;

import co.com.fixitgroup.domain.Work;
import co.com.fixitgroup.domain.enumeration.WorkState;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the Work entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkRepository extends JpaRepository<Work,Long> {

    @Query("SELECT w " +
        "FROM Work w " +
        "LEFT JOIN FETCH w.customer c " +
        "LEFT JOIN FETCH c.user u " +
        "LEFT JOIN FETCH w.worker worker " +
        "LEFT JOIN FETCH w.address a " +
        "LEFT JOIN FETCH w.worktype worktype " +
        "WHERE u.login = ?1 AND w.state IN ?2")
    List<Work> getMyWorks(String login, Set<WorkState> states);

}
