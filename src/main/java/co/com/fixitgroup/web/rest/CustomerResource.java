package co.com.fixitgroup.web.rest;

import co.com.fixitgroup.domain.User;
import co.com.fixitgroup.repository.UserRepository;
import co.com.fixitgroup.security.SecurityUtils;
import co.com.fixitgroup.service.CustomerService;
import co.com.fixitgroup.service.dto.CustomerDTO;
import com.codahale.metrics.annotation.Timed;
import co.com.fixitgroup.domain.Customer;

import co.com.fixitgroup.repository.CustomerRepository;
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
import java.util.Optional;

/**
 * REST controller for managing Customer.
 */
@RestController
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "customer";

    private final CustomerRepository customerRepository;

    private final CustomerService customerService;

    private final UserRepository userRepository;

    public CustomerResource(CustomerRepository customerRepository, CustomerService customerService, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.userRepository = userRepository;
    }

    /**
     * POST  /customers : Create a new customer.
     *
     * @param customer the customer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customer, or with status 400 (Bad Request) if the customer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/v2/customers")
    @Timed
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerDTO customer) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customer);
        Customer result = customerService.createCustomer(customer);
        return ResponseEntity.created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /customers : Create a new customer.
     *
     * @param customer the customer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customer, or with status 400 (Bad Request) if the customer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/customers")
    @Timed
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customer);
        if (customer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new customer cannot already have an ID")).body(null);
        }
        Customer result = customerRepository.save(customer);
        return ResponseEntity.created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customers : Updates an existing customer.
     *
     * @param customer the customer to update
     * @return the ReºsponseEntity with status 200 (OK) and with body the updated customer,
     * or with status 400 (Bad Request) if the customer is not valid,
     * or with status 500 (Internal Server Error) if the customer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/customers")
    @Timed
    public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody Customer customer) throws URISyntaxException {
        log.debug("REST request to update Customer : {}", customer);
        if (customer.getId() == null) {
            return new ResponseEntity<Customer>(HttpStatus.BAD_REQUEST);
        }
        Customer result = customerRepository.save(customer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customer.getId().toString()))
            .body(result);
    }

    @GetMapping("/api/customer/email/{email}/available")
    @Timed
    public ResponseEntity<Boolean> isEmailAvalable(@PathVariable String email) {
        Optional<User> userOptional = userRepository.findOneByLogin(email);
        if(userOptional.isPresent()){
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }


    @GetMapping("/api/customer/phone/{phone}/available")
    @Timed
    public ResponseEntity<Boolean> isPhoneAvalable(@PathVariable String phone) {
        Optional<Customer> customerOptional = customerRepository.findOneByPhoneEquals(phone);
        if(customerOptional.isPresent()){
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @GetMapping("/api/customer/authenticated")
    @Timed
    public ResponseEntity<Customer> getCurrentCustomer() {
        String login = SecurityUtils.getCurrentUserLogin();
        Optional<Customer> customerOptional = customerRepository.getCustomerByUser(login);
        if (customerOptional.isPresent()) {
            return new ResponseEntity<Customer>(
                customerOptional.get(), HttpStatus.OK
            );
        }
        return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
    }

    /**
     * GET  /customers : get all the customers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     */
    @GetMapping("/api/customers")
    @Timed
    public ResponseEntity<List<Customer>> getAllCustomers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Customers");
        Page<Customer> page = customerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customers/:id : get the "id" customer.
     *
     * @param id the id of the customer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer, or with status 404 (Not Found)
     */
    @GetMapping("/api/customers/{id}")
    @Timed
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);
        Customer customer = customerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(customer));
    }

    /**
     * DELETE  /customers/:id : delete the "id" customer.
     *
     * @param id the id of the customer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/customers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.debug("REST request to delete Customer : {}", id);
        customerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
