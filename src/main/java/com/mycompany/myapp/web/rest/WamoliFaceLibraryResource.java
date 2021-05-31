package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.WamoliFaceLibrary;
import com.mycompany.myapp.repository.WamoliFaceLibraryRepository;
import com.mycompany.myapp.service.WamoliFaceLibraryService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.WamoliFaceLibrary}.
 */
@RestController
@RequestMapping("/api")
public class WamoliFaceLibraryResource {

    private final Logger log = LoggerFactory.getLogger(WamoliFaceLibraryResource.class);

    private static final String ENTITY_NAME = "wamoliFaceLibrary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WamoliFaceLibraryService wamoliFaceLibraryService;

    private final WamoliFaceLibraryRepository wamoliFaceLibraryRepository;

    public WamoliFaceLibraryResource(
        WamoliFaceLibraryService wamoliFaceLibraryService,
        WamoliFaceLibraryRepository wamoliFaceLibraryRepository
    ) {
        this.wamoliFaceLibraryService = wamoliFaceLibraryService;
        this.wamoliFaceLibraryRepository = wamoliFaceLibraryRepository;
    }

    /**
     * {@code POST  /wamoli-face-libraries} : Create a new wamoliFaceLibrary.
     *
     * @param wamoliFaceLibrary the wamoliFaceLibrary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wamoliFaceLibrary, or with status {@code 400 (Bad Request)} if the wamoliFaceLibrary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wamoli-face-libraries")
    public ResponseEntity<WamoliFaceLibrary> createWamoliFaceLibrary(@Valid @RequestBody WamoliFaceLibrary wamoliFaceLibrary)
        throws URISyntaxException {
        log.debug("REST request to save WamoliFaceLibrary : {}", wamoliFaceLibrary);
        if (wamoliFaceLibrary.getId() != null) {
            throw new BadRequestAlertException("A new wamoliFaceLibrary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WamoliFaceLibrary result = wamoliFaceLibraryService.save(wamoliFaceLibrary);
        return ResponseEntity
            .created(new URI("/api/wamoli-face-libraries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wamoli-face-libraries/:id} : Updates an existing wamoliFaceLibrary.
     *
     * @param id the id of the wamoliFaceLibrary to save.
     * @param wamoliFaceLibrary the wamoliFaceLibrary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wamoliFaceLibrary,
     * or with status {@code 400 (Bad Request)} if the wamoliFaceLibrary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wamoliFaceLibrary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wamoli-face-libraries/{id}")
    public ResponseEntity<WamoliFaceLibrary> updateWamoliFaceLibrary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WamoliFaceLibrary wamoliFaceLibrary
    ) throws URISyntaxException {
        log.debug("REST request to update WamoliFaceLibrary : {}, {}", id, wamoliFaceLibrary);
        if (wamoliFaceLibrary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wamoliFaceLibrary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wamoliFaceLibraryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WamoliFaceLibrary result = wamoliFaceLibraryService.save(wamoliFaceLibrary);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wamoliFaceLibrary.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wamoli-face-libraries/:id} : Partial updates given fields of an existing wamoliFaceLibrary, field will ignore if it is null
     *
     * @param id the id of the wamoliFaceLibrary to save.
     * @param wamoliFaceLibrary the wamoliFaceLibrary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wamoliFaceLibrary,
     * or with status {@code 400 (Bad Request)} if the wamoliFaceLibrary is not valid,
     * or with status {@code 404 (Not Found)} if the wamoliFaceLibrary is not found,
     * or with status {@code 500 (Internal Server Error)} if the wamoliFaceLibrary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wamoli-face-libraries/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WamoliFaceLibrary> partialUpdateWamoliFaceLibrary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WamoliFaceLibrary wamoliFaceLibrary
    ) throws URISyntaxException {
        log.debug("REST request to partial update WamoliFaceLibrary partially : {}, {}", id, wamoliFaceLibrary);
        if (wamoliFaceLibrary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wamoliFaceLibrary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wamoliFaceLibraryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WamoliFaceLibrary> result = wamoliFaceLibraryService.partialUpdate(wamoliFaceLibrary);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wamoliFaceLibrary.getId().toString())
        );
    }

    /**
     * {@code GET  /wamoli-face-libraries} : get all the wamoliFaceLibraries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wamoliFaceLibraries in body.
     */
    @GetMapping("/wamoli-face-libraries")
    public List<WamoliFaceLibrary> getAllWamoliFaceLibraries() {
        log.debug("REST request to get all WamoliFaceLibraries");
        return wamoliFaceLibraryService.findAll();
    }

    /**
     * {@code GET  /wamoli-face-libraries/:id} : get the "id" wamoliFaceLibrary.
     *
     * @param id the id of the wamoliFaceLibrary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wamoliFaceLibrary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wamoli-face-libraries/{id}")
    public ResponseEntity<WamoliFaceLibrary> getWamoliFaceLibrary(@PathVariable Long id) {
        log.debug("REST request to get WamoliFaceLibrary : {}", id);
        Optional<WamoliFaceLibrary> wamoliFaceLibrary = wamoliFaceLibraryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wamoliFaceLibrary);
    }

    /**
     * {@code DELETE  /wamoli-face-libraries/:id} : delete the "id" wamoliFaceLibrary.
     *
     * @param id the id of the wamoliFaceLibrary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wamoli-face-libraries/{id}")
    public ResponseEntity<Void> deleteWamoliFaceLibrary(@PathVariable Long id) {
        log.debug("REST request to delete WamoliFaceLibrary : {}", id);
        wamoliFaceLibraryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
