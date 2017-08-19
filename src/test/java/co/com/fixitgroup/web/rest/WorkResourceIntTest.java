package co.com.fixitgroup.web.rest;

import co.com.fixitgroup.FixItApp;

import co.com.fixitgroup.domain.Customer;
import co.com.fixitgroup.domain.User;
import co.com.fixitgroup.domain.Work;
import co.com.fixitgroup.repository.CustomerRepository;
import co.com.fixitgroup.repository.UserRepository;
import co.com.fixitgroup.repository.WorkRepository;
import co.com.fixitgroup.security.AuthoritiesConstants;
import co.com.fixitgroup.security.jwt.JWTConfigurer;
import co.com.fixitgroup.security.jwt.TokenProvider;
import co.com.fixitgroup.web.rest.errors.ExceptionTranslator;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import javax.validation.constraints.AssertTrue;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static co.com.fixitgroup.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WorkResource REST controller.
 *
 * @see WorkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FixItApp.class)
public class WorkResourceIntTest {

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ASAP = false;
    private static final Boolean UPDATED_ASAP = true;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private  ObjectMapper objectMapper;

    private MockMvc restWorkMockMvc;

    private Work work;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkResource workResource = new WorkResource(workRepository);
        this.restWorkMockMvc = MockMvcBuilders.standaloneSetup(workResource)
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
    public static Work createEntity(EntityManager em) {
        Work work = new Work()
            .time(DEFAULT_TIME)
            .description(DEFAULT_DESCRIPTION)
            .asap(DEFAULT_ASAP);
        return work;
    }

    @Before
    public void initTest() {
        work = createEntity(em);
    }

    @Test
    @Transactional
    public void createWork() throws Exception {
        int databaseSizeBeforeCreate = workRepository.findAll().size();

        // Create the Work
        restWorkMockMvc.perform(post("/api/works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(work)))
            .andExpect(status().isCreated());

        // Validate the Work in the database
        List<Work> workList = workRepository.findAll();
        assertThat(workList).hasSize(databaseSizeBeforeCreate + 1);
        Work testWork = workList.get(workList.size() - 1);
        assertThat(testWork.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testWork.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWork.isAsap()).isEqualTo(DEFAULT_ASAP);
    }

    @Test
    @Transactional
    public void createWorkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workRepository.findAll().size();

        // Create the Work with an existing ID
        work.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkMockMvc.perform(post("/api/works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(work)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Work> workList = workRepository.findAll();
        assertThat(workList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workRepository.findAll().size();
        // set the field null
        work.setTime(null);

        // Create the Work, which fails.

        restWorkMockMvc.perform(post("/api/works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(work)))
            .andExpect(status().isBadRequest());

        List<Work> workList = workRepository.findAll();
        assertThat(workList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = workRepository.findAll().size();
        // set the field null
        work.setDescription(null);

        // Create the Work, which fails.

        restWorkMockMvc.perform(post("/api/works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(work)))
            .andExpect(status().isBadRequest());

        List<Work> workList = workRepository.findAll();
        assertThat(workList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAsapIsRequired() throws Exception {
        int databaseSizeBeforeTest = workRepository.findAll().size();
        // set the field null
        work.setAsap(null);

        // Create the Work, which fails.

        restWorkMockMvc.perform(post("/api/works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(work)))
            .andExpect(status().isBadRequest());

        List<Work> workList = workRepository.findAll();
        assertThat(workList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorks() throws Exception {
        // Initialize the database
        workRepository.saveAndFlush(work);

        // Get all the workList
        restWorkMockMvc.perform(get("/api/works?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(work.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].asap").value(hasItem(DEFAULT_ASAP.booleanValue())));
    }

    @Test
    @Transactional
    public void getMyWorksSucessfulTest() throws  Exception {
        User user = UserResourceIntTest.createEntity(em);
        Customer customer = CustomerResourceIntTest.createEntity(em);
        user = userRepository.saveAndFlush(user);
        customer.setUser(user);
        customer = customerRepository.saveAndFlush(customer);
        work.setCustomer(customer);
        work = workRepository.saveAndFlush(work);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            user.getLogin(), user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication, false);
        MvcResult result = restWorkMockMvc.perform(
            get("/api/myWorks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .header(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer ".concat(jwt)))
            .andExpect(status().isOk())

            .andReturn();
        Work[] resultWorks = objectMapper.readValue(result.getResponse().getContentAsString(), Work[].class);
        Assert.assertEquals(1, resultWorks.length);
    }

    @Test
    @Transactional
    public void getMyWorksUnAuthenticatedShouldFail() throws Exception {
        // Initialize the database
        workRepository.saveAndFlush(work);

        // Get all the workList With no permission header
        restWorkMockMvc.perform(get("/api/myWorks"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void getWork() throws Exception {
        // Initialize the database
        workRepository.saveAndFlush(work);

        // Get the work
        restWorkMockMvc.perform(get("/api/works/{id}", work.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(work.getId().intValue()))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.asap").value(DEFAULT_ASAP.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWork() throws Exception {
        // Get the work
        restWorkMockMvc.perform(get("/api/works/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWork() throws Exception {
        // Initialize the database
        workRepository.saveAndFlush(work);
        int databaseSizeBeforeUpdate = workRepository.findAll().size();

        // Update the work
        Work updatedWork = workRepository.findOne(work.getId());
        updatedWork
            .time(UPDATED_TIME)
            .description(UPDATED_DESCRIPTION)
            .asap(UPDATED_ASAP);

        restWorkMockMvc.perform(put("/api/works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWork)))
            .andExpect(status().isOk());

        // Validate the Work in the database
        List<Work> workList = workRepository.findAll();
        assertThat(workList).hasSize(databaseSizeBeforeUpdate);
        Work testWork = workList.get(workList.size() - 1);
        assertThat(testWork.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testWork.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWork.isAsap()).isEqualTo(UPDATED_ASAP);
    }

    @Test
    @Transactional
    public void updateNonExistingWork() throws Exception {
        int databaseSizeBeforeUpdate = workRepository.findAll().size();

        // Create the Work

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWorkMockMvc.perform(put("/api/works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(work)))
            .andExpect(status().isCreated());

        // Validate the Work in the database
        List<Work> workList = workRepository.findAll();
        assertThat(workList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWork() throws Exception {
        // Initialize the database
        workRepository.saveAndFlush(work);
        int databaseSizeBeforeDelete = workRepository.findAll().size();

        // Get the work
        restWorkMockMvc.perform(delete("/api/works/{id}", work.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Work> workList = workRepository.findAll();
        assertThat(workList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Work.class);
        Work work1 = new Work();
        work1.setId(1L);
        Work work2 = new Work();
        work2.setId(work1.getId());
        assertThat(work1).isEqualTo(work2);
        work2.setId(2L);
        assertThat(work1).isNotEqualTo(work2);
        work1.setId(null);
        assertThat(work1).isNotEqualTo(work2);
    }
}
