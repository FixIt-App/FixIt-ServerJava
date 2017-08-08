package co.com.fixitgroup.web.rest;

import co.com.fixitgroup.FixItApp;

import co.com.fixitgroup.domain.WorkTypeCategory;
import co.com.fixitgroup.repository.WorkTypeCategoryRepository;
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

/**
 * Test class for the WorkTypeCategoryResource REST controller.
 *
 * @see WorkTypeCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FixItApp.class)
public class WorkTypeCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    @Autowired
    private WorkTypeCategoryRepository workTypeCategoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkTypeCategoryMockMvc;

    private WorkTypeCategory workTypeCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkTypeCategoryResource workTypeCategoryResource = new WorkTypeCategoryResource(workTypeCategoryRepository);
        this.restWorkTypeCategoryMockMvc = MockMvcBuilders.standaloneSetup(workTypeCategoryResource)
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
    public static WorkTypeCategory createEntity(EntityManager em) {
        WorkTypeCategory workTypeCategory = new WorkTypeCategory()
            .name(DEFAULT_NAME)
            .order(DEFAULT_ORDER);
        return workTypeCategory;
    }

    @Before
    public void initTest() {
        workTypeCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkTypeCategory() throws Exception {
        int databaseSizeBeforeCreate = workTypeCategoryRepository.findAll().size();

        // Create the WorkTypeCategory
        restWorkTypeCategoryMockMvc.perform(post("/api/work-type-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workTypeCategory)))
            .andExpect(status().isCreated());

        // Validate the WorkTypeCategory in the database
        List<WorkTypeCategory> workTypeCategoryList = workTypeCategoryRepository.findAll();
        assertThat(workTypeCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        WorkTypeCategory testWorkTypeCategory = workTypeCategoryList.get(workTypeCategoryList.size() - 1);
        assertThat(testWorkTypeCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkTypeCategory.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    public void createWorkTypeCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workTypeCategoryRepository.findAll().size();

        // Create the WorkTypeCategory with an existing ID
        workTypeCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkTypeCategoryMockMvc.perform(post("/api/work-type-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workTypeCategory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WorkTypeCategory> workTypeCategoryList = workTypeCategoryRepository.findAll();
        assertThat(workTypeCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeCategoryRepository.findAll().size();
        // set the field null
        workTypeCategory.setName(null);

        // Create the WorkTypeCategory, which fails.

        restWorkTypeCategoryMockMvc.perform(post("/api/work-type-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workTypeCategory)))
            .andExpect(status().isBadRequest());

        List<WorkTypeCategory> workTypeCategoryList = workTypeCategoryRepository.findAll();
        assertThat(workTypeCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTypeCategoryRepository.findAll().size();
        // set the field null
        workTypeCategory.setOrder(null);

        // Create the WorkTypeCategory, which fails.

        restWorkTypeCategoryMockMvc.perform(post("/api/work-type-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workTypeCategory)))
            .andExpect(status().isBadRequest());

        List<WorkTypeCategory> workTypeCategoryList = workTypeCategoryRepository.findAll();
        assertThat(workTypeCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkTypeCategories() throws Exception {
        // Initialize the database
        workTypeCategoryRepository.saveAndFlush(workTypeCategory);

        // Get all the workTypeCategoryList
        restWorkTypeCategoryMockMvc.perform(get("/api/work-type-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workTypeCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    @Transactional
    public void getWorkTypeCategory() throws Exception {
        // Initialize the database
        workTypeCategoryRepository.saveAndFlush(workTypeCategory);

        // Get the workTypeCategory
        restWorkTypeCategoryMockMvc.perform(get("/api/work-type-categories/{id}", workTypeCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workTypeCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    @Transactional
    public void getNonExistingWorkTypeCategory() throws Exception {
        // Get the workTypeCategory
        restWorkTypeCategoryMockMvc.perform(get("/api/work-type-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkTypeCategory() throws Exception {
        // Initialize the database
        workTypeCategoryRepository.saveAndFlush(workTypeCategory);
        int databaseSizeBeforeUpdate = workTypeCategoryRepository.findAll().size();

        // Update the workTypeCategory
        WorkTypeCategory updatedWorkTypeCategory = workTypeCategoryRepository.findOne(workTypeCategory.getId());
        updatedWorkTypeCategory
            .name(UPDATED_NAME)
            .order(UPDATED_ORDER);

        restWorkTypeCategoryMockMvc.perform(put("/api/work-type-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkTypeCategory)))
            .andExpect(status().isOk());

        // Validate the WorkTypeCategory in the database
        List<WorkTypeCategory> workTypeCategoryList = workTypeCategoryRepository.findAll();
        assertThat(workTypeCategoryList).hasSize(databaseSizeBeforeUpdate);
        WorkTypeCategory testWorkTypeCategory = workTypeCategoryList.get(workTypeCategoryList.size() - 1);
        assertThat(testWorkTypeCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkTypeCategory.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkTypeCategory() throws Exception {
        int databaseSizeBeforeUpdate = workTypeCategoryRepository.findAll().size();

        // Create the WorkTypeCategory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWorkTypeCategoryMockMvc.perform(put("/api/work-type-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workTypeCategory)))
            .andExpect(status().isCreated());

        // Validate the WorkTypeCategory in the database
        List<WorkTypeCategory> workTypeCategoryList = workTypeCategoryRepository.findAll();
        assertThat(workTypeCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWorkTypeCategory() throws Exception {
        // Initialize the database
        workTypeCategoryRepository.saveAndFlush(workTypeCategory);
        int databaseSizeBeforeDelete = workTypeCategoryRepository.findAll().size();

        // Get the workTypeCategory
        restWorkTypeCategoryMockMvc.perform(delete("/api/work-type-categories/{id}", workTypeCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkTypeCategory> workTypeCategoryList = workTypeCategoryRepository.findAll();
        assertThat(workTypeCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkTypeCategory.class);
        WorkTypeCategory workTypeCategory1 = new WorkTypeCategory();
        workTypeCategory1.setId(1L);
        WorkTypeCategory workTypeCategory2 = new WorkTypeCategory();
        workTypeCategory2.setId(workTypeCategory1.getId());
        assertThat(workTypeCategory1).isEqualTo(workTypeCategory2);
        workTypeCategory2.setId(2L);
        assertThat(workTypeCategory1).isNotEqualTo(workTypeCategory2);
        workTypeCategory1.setId(null);
        assertThat(workTypeCategory1).isNotEqualTo(workTypeCategory2);
    }
}
