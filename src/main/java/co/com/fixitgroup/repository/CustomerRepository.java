package co.com.fixitgroup.repository;

import co.com.fixitgroup.domain.Customer;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Customer entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("SELECT c FROM Customer c JOIN FETCH c.user user  LEFT JOIN FETCH c.confirmations WHERE user.login = ?1")
    Optional<Customer> getCustomerByUser(String login);

}
