package co.com.fixitgroup.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.fixitgroup.domain.Work;

import co.com.fixitgroup.repository.WorkRepository;
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
 * REST controller for managing Work.
 */
@RestController
@RequestMapping("/api")
public class WorkResource {

    private final Logger log = LoggerFactory.getLogger(WorkResource.class);

    private static final String ENTITY_NAME = "work";

    private final WorkRepository workRepository;

    public WorkResource(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    /**
     * POST  /works : Create a new work.
     *
     * @param work the work to create
     * @return the ResponseEntity with status 201 (Created) and with body the new work, or with status 400 (Bad Request) if the work has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/works")
    @Timed
    public ResponseEntity<Work> createWork(@Valid @RequestBody Work work) throws URISyntaxException {
        log.debug("REST request to save Work : {}", work);
        if (work.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new work cannot already have an ID")).body(null);
        }
        Work result = workRepository.save(work);
        return ResponseEntity.created(new URI("/api/works/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /works : Updates an existing work.
     *
     * @param work the work to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated work,
     * or with status 400 (Bad Request) if the work is not valid,
     * or with status 500 (Internal Server Error) if the work couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/works")
    @Timed
    public ResponseEntity<Work> updateWork(@Valid @RequestBody Work work) throws URISyntaxException {
        log.debug("REST request to update Work : {}", work);
        if (work.getId() == null) {
            return createWork(work);
        }
        Work result = workRepository.save(work);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, work.getId().toString()))
            .body(result);
    }

    /**
     * GET  /works : get all the works.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of works in body
     */
    @GetMapping("/works")
    @Timed
    public ResponseEntity<List<Work>> getAllWorks(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Works");
        Page<Work> page = workRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/works");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /works/:id : get the "id" work.
     *
     * @param id the id of the work to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the work, or with status 404 (Not Found)
     */
    @GetMapping("/works/{id}")
    @Timed
    public ResponseEntity<Work> getWork(@PathVariable Long id) {
        log.debug("REST request to get Work : {}", id);
        Work work = workRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(work));
    }

    /**
     * DELETE  /works/:id : delete the "id" work.
     *
     * @param id the id of the work to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/works/{id}")
    @Timed
    public ResponseEntity<Void> deleteWork(@PathVariable Long id) {
        log.debug("REST request to delete Work : {}", id);
        workRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
