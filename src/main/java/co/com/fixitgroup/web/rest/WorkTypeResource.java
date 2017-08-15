package co.com.fixitgroup.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.fixitgroup.domain.WorkType;

import co.com.fixitgroup.repository.WorkTypeRepository;
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
 * REST controller for managing WorkType.
 */
@RestController
@RequestMapping("/api")
public class WorkTypeResource {

    private final Logger log = LoggerFactory.getLogger(WorkTypeResource.class);

    private static final String ENTITY_NAME = "workType";

    private final WorkTypeRepository workTypeRepository;

    public WorkTypeResource(WorkTypeRepository workTypeRepository) {
        this.workTypeRepository = workTypeRepository;
    }

    /**
     * POST  /work-types : Create a new workType.
     *
     * @param workType the workType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workType, or with status 400 (Bad Request) if the workType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/work-types")
    @Timed
    public ResponseEntity<WorkType> createWorkType(@Valid @RequestBody WorkType workType) throws URISyntaxException {
        log.debug("REST request to save WorkType : {}", workType);
        if (workType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new workType cannot already have an ID")).body(null);
        }
        WorkType result = workTypeRepository.save(workType);
        return ResponseEntity.created(new URI("/api/work-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-types : Updates an existing workType.
     *
     * @param workType the workType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workType,
     * or with status 400 (Bad Request) if the workType is not valid,
     * or with status 500 (Internal Server Error) if the workType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/work-types")
    @Timed
    public ResponseEntity<WorkType> updateWorkType(@Valid @RequestBody WorkType workType) throws URISyntaxException {
        log.debug("REST request to update WorkType : {}", workType);
        if (workType.getId() == null) {
            return createWorkType(workType);
        }
        WorkType result = workTypeRepository.save(workType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-types : get all the workTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workTypes in body
     */
    @GetMapping("/work-types")
    @Timed
    public ResponseEntity<List<WorkType>> getAllWorkTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WorkTypes");
        Page<WorkType> page = workTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /work-types/:id : get the "id" workType.
     *
     * @param id the id of the workType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workType, or with status 404 (Not Found)
     */
    @GetMapping("/work-types/{id}")
    @Timed
    public ResponseEntity<WorkType> getWorkType(@PathVariable Long id) {
        log.debug("REST request to get WorkType : {}", id);
        WorkType workType = workTypeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(workType));
    }

    /**
     * DELETE  /work-types/:id : delete the "id" workType.
     *
     * @param id the id of the workType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/work-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkType(@PathVariable Long id) {
        log.debug("REST request to delete WorkType : {}", id);
        workTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
