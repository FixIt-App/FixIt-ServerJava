package co.com.fixitgroup.web.rest;

import co.com.fixitgroup.FixItApp;

import co.com.fixitgroup.domain.WorkType;
import co.com.fixitgroup.repository.WorkTypeRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.fixitgroup.domain.enumeration.PriceType;
/**
 * Test class for the WorkTypeResource REST controller.
 *
 * @see WorkTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FixItApp.class)
public class WorkTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final PriceType DEFAULT_PRICE_TYPE = PriceType.STANDARIZED;
    private static final PriceType UPDATED_PRICE_TYPE = PriceType.NOT_STANDARIZED;

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String DEFAULT_URL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_URL_NAME = "BBBBBBBBBB";

    @Autowired
    private WorkTypeRepository workTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkTypeMockMvc;

    private WorkType workType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkTypeResource workTypeResource = new WorkTypeResource(workTypeRepository);
        this.restWorkTypeMockMvc = MockMvcBuilders.standaloneSetup(workTypeResource)
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
    public static WorkType createEntity(EntityManager em) {
        WorkType workType = new WorkType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .icon(DEFAULT_ICON)
            .priceType(DEFAULT_PRICE_TYPE)
            .price(DEFAULT_PRICE)
            .order(DEFAULT_ORDER)
            .urlName(DEFAULT_URL_NAME);
        return workType;
    }

    @Before
    public void initTest() {
        workType = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkType() throws Exception {
        int databaseSizeBeforeCreate = workTypeRepository.findAll().size();

        // Create the WorkType
        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isCreated());

        // Validate the WorkType in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeCreate + 1);
        WorkType testWorkType = workTypeList.get(workTypeList.size() - 1);
        assertThat(testWorkType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkType.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testWorkType.getPriceType()).isEqualTo(DEFAULT_PRICE_TYPE);
        assertThat(testWorkType.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testWorkType.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testWorkType.getUrlName()).isEqualTo(DEFAULT_URL_NAME);
    }

    @Test
    @Transactional
    public void createWorkTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workTypeRepository.findAll().size();

        // Create the WorkType with an existing ID
        workType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setName(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setDescription(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIconIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setIcon(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setPriceType(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setPrice(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setOrder(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeRepository.findAll().size();
        // set the field null
        workType.setUrlName(null);

        // Create the WorkType, which fails.

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isBadRequest());

        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkTypes() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);

        // Get all the workTypeList
        restWorkTypeMockMvc.perform(get("/api/work-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
            .andExpect(jsonPath("$.[*].priceType").value(hasItem(DEFAULT_PRICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].urlName").value(hasItem(DEFAULT_URL_NAME.toString())));
    }

    @Test
    @Transactional
    public void getWorkType() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);

        // Get the workType
        restWorkTypeMockMvc.perform(get("/api/work-types/{id}", workType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()))
            .andExpect(jsonPath("$.priceType").value(DEFAULT_PRICE_TYPE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.urlName").value(DEFAULT_URL_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkType() throws Exception {
        // Get the workType
        restWorkTypeMockMvc.perform(get("/api/work-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkType() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);
        int databaseSizeBeforeUpdate = workTypeRepository.findAll().size();

        // Update the workType
        WorkType updatedWorkType = workTypeRepository.findOne(workType.getId());
        updatedWorkType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .icon(UPDATED_ICON)
            .priceType(UPDATED_PRICE_TYPE)
            .price(UPDATED_PRICE)
            .order(UPDATED_ORDER)
            .urlName(UPDATED_URL_NAME);

        restWorkTypeMockMvc.perform(put("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkType)))
            .andExpect(status().isOk());

        // Validate the WorkType in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeUpdate);
        WorkType testWorkType = workTypeList.get(workTypeList.size() - 1);
        assertThat(testWorkType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkType.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testWorkType.getPriceType()).isEqualTo(UPDATED_PRICE_TYPE);
        assertThat(testWorkType.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testWorkType.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testWorkType.getUrlName()).isEqualTo(UPDATED_URL_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkType() throws Exception {
        int databaseSizeBeforeUpdate = workTypeRepository.findAll().size();

        // Create the WorkType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWorkTypeMockMvc.perform(put("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workType)))
            .andExpect(status().isCreated());

        // Validate the WorkType in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWorkType() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);
        int databaseSizeBeforeDelete = workTypeRepository.findAll().size();

        // Get the workType
        restWorkTypeMockMvc.perform(delete("/api/work-types/{id}", workType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkType.class);
        WorkType workType1 = new WorkType();
        workType1.setId(1L);
        WorkType workType2 = new WorkType();
        workType2.setId(workType1.getId());
        assertThat(workType1).isEqualTo(workType2);
        workType2.setId(2L);
        assertThat(workType1).isNotEqualTo(workType2);
        workType1.setId(null);
        assertThat(workType1).isNotEqualTo(workType2);
    }
}
