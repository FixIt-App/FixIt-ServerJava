package co.com.fixitgroup.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.fixitgroup.domain.WorkTypeCategory;

import co.com.fixitgroup.repository.WorkTypeCategoryRepository;
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
 * REST controller for managing WorkTypeCategory.
 */
@RestController
@RequestMapping("/api")
public class WorkTypeCategoryResource {

    private final Logger log = LoggerFactory.getLogger(WorkTypeCategoryResource.class);

    private static final String ENTITY_NAME = "workTypeCategory";

    private final WorkTypeCategoryRepository workTypeCategoryRepository;

    public WorkTypeCategoryResource(WorkTypeCategoryRepository workTypeCategoryRepository) {
        this.workTypeCategoryRepository = workTypeCategoryRepository;
    }

    /**
     * POST  /work-type-categories : Create a new workTypeCategory.
     *
     * @param workTypeCategory the workTypeCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workTypeCategory, or with status 400 (Bad Request) if the workTypeCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/work-type-categories")
    @Timed
    public ResponseEntity<WorkTypeCategory> createWorkTypeCategory(@Valid @RequestBody WorkTypeCategory workTypeCategory) throws URISyntaxException {
        log.debug("REST request to save WorkTypeCategory : {}", workTypeCategory);
        if (workTypeCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new workTypeCategory cannot already have an ID")).body(null);
        }
        WorkTypeCategory result = workTypeCategoryRepository.save(workTypeCategory);
        return ResponseEntity.created(new URI("/api/work-type-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-type-categories : Updates an existing workTypeCategory.
     *
     * @param workTypeCategory the workTypeCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workTypeCategory,
     * or with status 400 (Bad Request) if the workTypeCategory is not valid,
     * or with status 500 (Internal Server Error) if the workTypeCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/work-type-categories")
    @Timed
    public ResponseEntity<WorkTypeCategory> updateWorkTypeCategory(@Valid @RequestBody WorkTypeCategory workTypeCategory) throws URISyntaxException {
        log.debug("REST request to update WorkTypeCategory : {}", workTypeCategory);
        if (workTypeCategory.getId() == null) {
            return createWorkTypeCategory(workTypeCategory);
        }
        WorkTypeCategory result = workTypeCategoryRepository.save(workTypeCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workTypeCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-type-categories : get all the workTypeCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workTypeCategories in body
     */
    @GetMapping("/work-type-categories")
    @Timed
    public ResponseEntity<List<WorkTypeCategory>> getAllWorkTypeCategories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WorkTypeCategories");
        Page<WorkTypeCategory> page = workTypeCategoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-type-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /work-type-categories/:id : get the "id" workTypeCategory.
     *
     * @param id the id of the workTypeCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workTypeCategory, or with status 404 (Not Found)
     */
    @GetMapping("/work-type-categories/{id}")
    @Timed
    public ResponseEntity<WorkTypeCategory> getWorkTypeCategory(@PathVariable Long id) {
        log.debug("REST request to get WorkTypeCategory : {}", id);
        WorkTypeCategory workTypeCategory = workTypeCategoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(workTypeCategory));
    }

    /**
     * DELETE  /work-type-categories/:id : delete the "id" workTypeCategory.
     *
     * @param id the id of the workTypeCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/work-type-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkTypeCategory(@PathVariable Long id) {
        log.debug("REST request to delete WorkTypeCategory : {}", id);
        workTypeCategoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
