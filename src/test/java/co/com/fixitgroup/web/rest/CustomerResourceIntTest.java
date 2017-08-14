package co.com.fixitgroup.web.rest;

import co.com.fixitgroup.FixItApp;

import co.com.fixitgroup.domain.Customer;
import co.com.fixitgroup.domain.User;
import co.com.fixitgroup.repository.CustomerRepository;
import co.com.fixitgroup.repository.UserRepository;
import co.com.fixitgroup.security.AuthoritiesConstants;
import co.com.fixitgroup.security.jwt.JWTConfigurer;
import co.com.fixitgroup.security.jwt.TokenProvider;
import co.com.fixitgroup.service.CustomerService;
import co.com.fixitgroup.service.dto.CustomerDTO;
import co.com.fixitgroup.web.rest.errors.ExceptionTranslator;

import co.com.fixitgroup.web.rest.vm.ManagedUserVM;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FixItApp.class)
public class CustomerResourceIntTest {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";
    private static final String DEFAULT_LOGIN = "example@mail.com";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private EntityManager em;

    @Autowired
    private CustomerService customerService;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    private User user;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityContextHolder.getContext().setAuthentication(null);
        CustomerResource customerResource = new CustomerResource(customerRepository, customerService, userRepository);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .phone(DEFAULT_PHONE);
        return customer;
    }

    @Before
    public void initTest() {
        user = UserResourceIntTest.createEntity(em);
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void getCurrentCustomerTest() throws Exception {
        user = userRepository.saveAndFlush(user);
        customer.setUser(user);
        customerRepository.saveAndFlush(customer);


        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            user.getLogin(), user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);
        restCustomerMockMvc
            .perform(get("/api/customer/authenticated")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .header(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer ".concat(jwt)))
            .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void isEmailAvailableTest() throws Exception {
        userRepository.saveAndFlush(user);
        MvcResult result = restCustomerMockMvc
            .perform(get("/api/customer/email/blabla/available")
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn();

        Assert.assertEquals(result.getResponse().getContentAsString(), "true");
    }

    @Test
    @Transactional
    public void isEmailNotAvailableTest() throws Exception {
        userRepository.saveAndFlush(user);
        MvcResult result = restCustomerMockMvc
            .perform(get("/api/customer/email/johndoe/available")
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn();

        Assert.assertEquals(result.getResponse().getContentAsString(), "false");
    }

    @Test
    @Transactional
    public void getCurrentCustomerNoAuth() throws Exception {
        userRepository.saveAndFlush(user);
        customer.setUser(user);
        customerRepository.saveAndFlush(customer);


        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            "randomUnknown", user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);
        restCustomerMockMvc
            .perform(get("/api/customer/authenticated")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .header(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer ".concat(jwt)))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void createCustomerV1() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setPhone(customer.getPhone());
        User user = UserResourceIntTest.createEntity(em);
        ManagedUserVM userDTO = new ManagedUserVM(user);
        userDTO.setPassword("1233444213");
        customerDTO.setUser(userDTO);
        // Create the Customer
        restCustomerMockMvc.perform(post("/api/v2/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setPhone(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        customer.setUser(user);
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findOne(customer.getId());
        updatedCustomer
            .phone(UPDATED_PHONE);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = new Customer();
        customer1.setId(1L);
        Customer customer2 = new Customer();
        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);
        customer2.setId(2L);
        assertThat(customer1).isNotEqualTo(customer2);
        customer1.setId(null);
        assertThat(customer1).isNotEqualTo(customer2);
    }
}
