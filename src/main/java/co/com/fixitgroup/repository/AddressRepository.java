package co.com.fixitgroup.repository;

import co.com.fixitgroup.domain.Address;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Address entity.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {


    @Query("SELECT a FROM Address a JOIN FETCH a.customer c JOIN FETCH c.user u WHERE u.login = ?1")
    List<Address> getCustomerAddresses(String login);

}
