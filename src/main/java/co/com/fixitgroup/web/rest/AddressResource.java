package co.com.fixitgroup.web.rest;

import co.com.fixitgroup.domain.Customer;
import co.com.fixitgroup.repository.CustomerRepository;
import co.com.fixitgroup.security.SecurityUtils;
import com.codahale.metrics.annotation.Timed;
import co.com.fixitgroup.domain.Address;

import co.com.fixitgroup.repository.AddressRepository;
import co.com.fixitgroup.web.rest.util.HeaderUtil;
import co.com.fixitgroup.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing Address.
 */
@RestController
@RequestMapping("/api")
public class AddressResource {

    private final Logger log = LoggerFactory.getLogger(AddressResource.class);

    private static final String ENTITY_NAME = "address";

    private final AddressRepository addressRepository;

    private final CustomerRepository customerRepository;

    public AddressResource(AddressRepository addressRepository, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * POST  /addresses : Create a new address.
     *
     * @param address the address to create
     * @return the ResponseEntity with status 201 (Created) and with body the new address, or with status 400 (Bad Request) if the address has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/addresses")
    @Timed
    public ResponseEntity<Address> createAddress(@Valid @RequestBody Address address) throws URISyntaxException {
        log.debug("REST request to save Address : {}", address);
        if (address.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new address cannot already have an ID")).body(null);
        }
        Address result = addressRepository.save(address);
        return ResponseEntity.created(new URI("/api/addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * POST  /v2/addresses : Create a new address.
     *
     * @param address the address to create
     * @return the ResponseEntity with status 201 (Created) and with body the new address, or with status 400 (Bad Request) if the address has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/v2/addresses")
    @Timed
    public ResponseEntity<Address> createAddressV2(@Valid @RequestBody Address address) throws URISyntaxException {
        log.debug("REST request to save Address : {}", address);

        if(Objects.nonNull(address.getCustomer())) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Customer> customerOptional = customerRepository.getCustomerByUser(
            SecurityUtils.getCurrentUserLogin()
        );
        if(!customerOptional.isPresent()) {
            return new ResponseEntity<Address>(HttpStatus.UNAUTHORIZED);
        }
        address.setCustomer(customerOptional.get());
        if (address.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new address cannot already have an ID")).body(null);
        }
        Address result = addressRepository.save(address);
        return ResponseEntity.created(new URI("/api/addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /addresses : Updates an existing address.
     *
     * @param address the address to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated address,
     * or with status 400 (Bad Request) if the address is not valid,
     * or with status 500 (Internal Server Error) if the address couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/v2/addresses")
    @Timed
    public ResponseEntity<Address> updateAddressV2(@Valid @RequestBody Address address) throws URISyntaxException {
        log.debug("REST request to update Address : {}", address);
        if (Objects.isNull(address.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Address current = addressRepository.getOne(address.getId());
        String user = SecurityUtils.getCurrentUserLogin();
        if((Objects.nonNull(current.getCustomer()) && !user.equals(current.getCustomer().getUser().getLogin()))
            || Objects.isNull(current.getCustomer())) {
            return new ResponseEntity<Address>(HttpStatus.FORBIDDEN);
        }

        Address result = addressRepository.save(address);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, address.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /addresses : Updates an existing address.
     *
     * @param address the address to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated address,
     * or with status 400 (Bad Request) if the address is not valid,
     * or with status 500 (Internal Server Error) if the address couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/addresses")
    @Timed
    public ResponseEntity<Address> updateAddress(@Valid @RequestBody Address address) throws URISyntaxException {
        log.debug("REST request to update Address : {}", address);
        if (address.getId() == null) {
            return createAddress(address);
        }
        Address result = addressRepository.save(address);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, address.getId().toString()))
            .body(result);
    }

    /**
     * GET  /addresses : get all the addresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body
     */
    @GetMapping("/addresses")
    @Timed
    public ResponseEntity<List<Address>> getAllAddresses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Addresses");
        Page<Address> page = addressRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/addresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /addresses : get all the addresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body
     */
    @GetMapping("/myaddresses")
    @Timed
    public ResponseEntity<List<Address>> getMYAddresses() {
        log.debug("REST request to get a page of MyAddresses");
        String login = SecurityUtils.getCurrentUserLogin();
        if(login == null) {
            return new ResponseEntity<List<Address>>(HttpStatus.UNAUTHORIZED);
        }
        List<Address> addresses = addressRepository.getCustomerAddresses(login);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    /**
     * GET  /addresses/:id : get the "id" address.
     *
     * @param id the id of the address to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the address, or with status 404 (Not Found)
     */
    @GetMapping("/addresses/{id}")
    @Timed
    public ResponseEntity<Address> getAddress(@PathVariable Long id) {
        log.debug("REST request to get Address : {}", id);
        Address address = addressRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(address));
    }

    /**
     * DELETE  /addresses/:id : delete the "id" address.
     *
     * @param id the id of the address to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/addresses/{id}")
    @Timed
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        log.debug("REST request to delete Address : {}", id);
        addressRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
