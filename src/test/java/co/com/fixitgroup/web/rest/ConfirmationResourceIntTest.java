package co.com.fixitgroup.web.rest;

import co.com.fixitgroup.FixItApp;

import co.com.fixitgroup.domain.Confirmation;
import co.com.fixitgroup.repository.ConfirmationRepository;
import co.com.fixitgroup.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static co.com.fixitgroup.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.fixitgroup.domain.enumeration.CONFIRMATION_TYPE;
/**
 * Test class for the ConfirmationResource REST controller.
 *
 * @see ConfirmationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FixItApp.class)
public class ConfirmationResourceIntTest {

    private static final CONFIRMATION_TYPE DEFAULT_TYPE = CONFIRMATION_TYPE.EMAIL;
    private static final CONFIRMATION_TYPE UPDATED_TYPE = CONFIRMATION_TYPE.SMS;

    private static final ZonedDateTime DEFAULT_EXPIRES = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRES = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATE = false;
    private static final Boolean UPDATED_STATE = true;

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConfirmationMockMvc;

    private Confirmation confirmation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConfirmationResource confirmationResource = new ConfirmationResource(confirmationRepository);
        this.restConfirmationMockMvc = MockMvcBuilders.standaloneSetup(confirmationResource)
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
    public static Confirmation createEntity(EntityManager em) {
        Confirmation confirmation = new Confirmation()
            .type(DEFAULT_TYPE)
            .expires(DEFAULT_EXPIRES)
            .code(DEFAULT_CODE)
            .state(DEFAULT_STATE);
        return confirmation;
    }

    @Before
    public void initTest() {
        confirmation = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfirmation() throws Exception {
        int databaseSizeBeforeCreate = confirmationRepository.findAll().size();

        // Create the Confirmation
        restConfirmationMockMvc.perform(post("/api/confirmations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confirmation)))
            .andExpect(status().isCreated());

        // Validate the Confirmation in the database
        List<Confirmation> confirmationList = confirmationRepository.findAll();
        assertThat(confirmationList).hasSize(databaseSizeBeforeCreate + 1);
        Confirmation testConfirmation = confirmationList.get(confirmationList.size() - 1);
        assertThat(testConfirmation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testConfirmation.getExpires()).isEqualTo(DEFAULT_EXPIRES);
        assertThat(testConfirmation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testConfirmation.isState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createConfirmationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = confirmationRepository.findAll().size();

        // Create the Confirmation with an existing ID
        confirmation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfirmationMockMvc.perform(post("/api/confirmations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confirmation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Confirmation> confirmationList = confirmationRepository.findAll();
        assertThat(confirmationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = confirmationRepository.findAll().size();
        // set the field null
        confirmation.setType(null);

        // Create the Confirmation, which fails.

        restConfirmationMockMvc.perform(post("/api/confirmations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confirmation)))
            .andExpect(status().isBadRequest());

        List<Confirmation> confirmationList = confirmationRepository.findAll();
        assertThat(confirmationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpiresIsRequired() throws Exception {
        int databaseSizeBeforeTest = confirmationRepository.findAll().size();
        // set the field null
        confirmation.setExpires(null);

        // Create the Confirmation, which fails.

        restConfirmationMockMvc.perform(post("/api/confirmations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confirmation)))
            .andExpect(status().isBadRequest());

        List<Confirmation> confirmationList = confirmationRepository.findAll();
        assertThat(confirmationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = confirmationRepository.findAll().size();
        // set the field null
        confirmation.setCode(null);

        // Create the Confirmation, which fails.

        restConfirmationMockMvc.perform(post("/api/confirmations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confirmation)))
            .andExpect(status().isBadRequest());

        List<Confirmation> confirmationList = confirmationRepository.findAll();
        assertThat(confirmationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = confirmationRepository.findAll().size();
        // set the field null
        confirmation.setState(null);

        // Create the Confirmation, which fails.

        restConfirmationMockMvc.perform(post("/api/confirmations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confirmation)))
            .andExpect(status().isBadRequest());

        List<Confirmation> confirmationList = confirmationRepository.findAll();
        assertThat(confirmationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfirmations() throws Exception {
        // Initialize the database
        confirmationRepository.saveAndFlush(confirmation);

        // Get all the confirmationList
        restConfirmationMockMvc.perform(get("/api/confirmations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confirmation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expires").value(hasItem(sameInstant(DEFAULT_EXPIRES))))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.booleanValue())));
    }

    @Test
    @Transactional
    public void getConfirmation() throws Exception {
        // Initialize the database
        confirmationRepository.saveAndFlush(confirmation);

        // Get the confirmation
        restConfirmationMockMvc.perform(get("/api/confirmations/{id}", confirmation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(confirmation.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.expires").value(sameInstant(DEFAULT_EXPIRES)))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConfirmation() throws Exception {
        // Get the confirmation
        restConfirmationMockMvc.perform(get("/api/confirmations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfirmation() throws Exception {
        // Initialize the database
        confirmationRepository.saveAndFlush(confirmation);
        int databaseSizeBeforeUpdate = confirmationRepository.findAll().size();

        // Update the confirmation
        Confirmation updatedConfirmation = confirmationRepository.findOne(confirmation.getId());
        updatedConfirmation
            .type(UPDATED_TYPE)
            .expires(UPDATED_EXPIRES)
            .code(UPDATED_CODE)
            .state(UPDATED_STATE);

        restConfirmationMockMvc.perform(put("/api/confirmations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfirmation)))
            .andExpect(status().isOk());

        // Validate the Confirmation in the database
        List<Confirmation> confirmationList = confirmationRepository.findAll();
        assertThat(confirmationList).hasSize(databaseSizeBeforeUpdate);
        Confirmation testConfirmation = confirmationList.get(confirmationList.size() - 1);
        assertThat(testConfirmation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testConfirmation.getExpires()).isEqualTo(UPDATED_EXPIRES);
        assertThat(testConfirmation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testConfirmation.isState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingConfirmation() throws Exception {
        int databaseSizeBeforeUpdate = confirmationRepository.findAll().size();

        // Create the Confirmation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConfirmationMockMvc.perform(put("/api/confirmations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confirmation)))
            .andExpect(status().isCreated());

        // Validate the Confirmation in the database
        List<Confirmation> confirmationList = confirmationRepository.findAll();
        assertThat(confirmationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConfirmation() throws Exception {
        // Initialize the database
        confirmationRepository.saveAndFlush(confirmation);
        int databaseSizeBeforeDelete = confirmationRepository.findAll().size();

        // Get the confirmation
        restConfirmationMockMvc.perform(delete("/api/confirmations/{id}", confirmation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Confirmation> confirmationList = confirmationRepository.findAll();
        assertThat(confirmationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Confirmation.class);
        Confirmation confirmation1 = new Confirmation();
        confirmation1.setId(1L);
        Confirmation confirmation2 = new Confirmation();
        confirmation2.setId(confirmation1.getId());
        assertThat(confirmation1).isEqualTo(confirmation2);
        confirmation2.setId(2L);
        assertThat(confirmation1).isNotEqualTo(confirmation2);
        confirmation1.setId(null);
        assertThat(confirmation1).isNotEqualTo(confirmation2);
    }
}
