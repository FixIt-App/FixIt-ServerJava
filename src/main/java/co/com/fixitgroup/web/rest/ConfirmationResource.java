package co.com.fixitgroup.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.com.fixitgroup.domain.Confirmation;

import co.com.fixitgroup.repository.ConfirmationRepository;
import co.com.fixitgroup.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Confirmation.
 */
@RestController
@RequestMapping("/api")
public class ConfirmationResource {

    private final Logger log = LoggerFactory.getLogger(ConfirmationResource.class);

    private static final String ENTITY_NAME = "confirmation";

    private final ConfirmationRepository confirmationRepository;

    public ConfirmationResource(ConfirmationRepository confirmationRepository) {
        this.confirmationRepository = confirmationRepository;
    }

    /**
     * POST  /confirmations : Create a new confirmation.
     *
     * @param confirmation the confirmation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new confirmation, or with status 400 (Bad Request) if the confirmation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/confirmations")
    @Timed
    public ResponseEntity<Confirmation> createConfirmation(@Valid @RequestBody Confirmation confirmation) throws URISyntaxException {
        log.debug("REST request to save Confirmation : {}", confirmation);
        if (confirmation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new confirmation cannot already have an ID")).body(null);
        }
        Confirmation result = confirmationRepository.save(confirmation);
        return ResponseEntity.created(new URI("/api/confirmations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /confirmations : Updates an existing confirmation.
     *
     * @param confirmation the confirmation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated confirmation,
     * or with status 400 (Bad Request) if the confirmation is not valid,
     * or with status 500 (Internal Server Error) if the confirmation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/confirmations")
    @Timed
    public ResponseEntity<Confirmation> updateConfirmation(@Valid @RequestBody Confirmation confirmation) throws URISyntaxException {
        log.debug("REST request to update Confirmation : {}", confirmation);
        if (confirmation.getId() == null) {
            return createConfirmation(confirmation);
        }
        Confirmation result = confirmationRepository.save(confirmation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, confirmation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /confirmations : get all the confirmations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of confirmations in body
     */
    @GetMapping("/confirmations")
    @Timed
    public List<Confirmation> getAllConfirmations() {
        log.debug("REST request to get all Confirmations");
        return confirmationRepository.findAll();
    }

    /**
     * GET  /confirmations/:id : get the "id" confirmation.
     *
     * @param id the id of the confirmation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the confirmation, or with status 404 (Not Found)
     */
    @GetMapping("/confirmations/{id}")
    @Timed
    public ResponseEntity<Confirmation> getConfirmation(@PathVariable Long id) {
        log.debug("REST request to get Confirmation : {}", id);
        Confirmation confirmation = confirmationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(confirmation));
    }

    /**
     * DELETE  /confirmations/:id : delete the "id" confirmation.
     *
     * @param id the id of the confirmation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/confirmations/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfirmation(@PathVariable Long id) {
        log.debug("REST request to delete Confirmation : {}", id);
        confirmationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
