package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.WamoliUser;
import com.mycompany.myapp.repository.WamoliUserRepository;
import com.mycompany.myapp.service.WamoliUserQueryService;
import com.mycompany.myapp.service.WamoliUserService;
import com.mycompany.myapp.service.criteria.WamoliUserCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.WamoliUser}.
 */
@RestController
@RequestMapping("/api")
public class WamoliUserResource {

    private final Logger log = LoggerFactory.getLogger(WamoliUserResource.class);

    private static final String ENTITY_NAME = "wamoliUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WamoliUserService wamoliUserService;

    private final WamoliUserRepository wamoliUserRepository;

    private final WamoliUserQueryService wamoliUserQueryService;

    public WamoliUserResource(
        WamoliUserService wamoliUserService,
        WamoliUserRepository wamoliUserRepository,
        WamoliUserQueryService wamoliUserQueryService
    ) {
        this.wamoliUserService = wamoliUserService;
        this.wamoliUserRepository = wamoliUserRepository;
        this.wamoliUserQueryService = wamoliUserQueryService;
    }

    /**
     * {@code POST  /wamoli-users} : Create a new wamoliUser.
     *
     * @param wamoliUser the wamoliUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wamoliUser, or with status {@code 400 (Bad Request)} if the wamoliUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wamoli-users")
    public ResponseEntity<WamoliUser> createWamoliUser(@Valid @RequestBody WamoliUser wamoliUser) throws URISyntaxException {
        log.debug("REST request to save WamoliUser : {}", wamoliUser);
        if (wamoliUser.getId() != null) {
            throw new BadRequestAlertException("A new wamoliUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WamoliUser result = wamoliUserService.save(wamoliUser);
        return ResponseEntity
            .created(new URI("/api/wamoli-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wamoli-users/:id} : Updates an existing wamoliUser.
     *
     * @param id the id of the wamoliUser to save.
     * @param wamoliUser the wamoliUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wamoliUser,
     * or with status {@code 400 (Bad Request)} if the wamoliUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wamoliUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wamoli-users/{id}")
    public ResponseEntity<WamoliUser> updateWamoliUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WamoliUser wamoliUser
    ) throws URISyntaxException {
        log.debug("REST request to update WamoliUser : {}, {}", id, wamoliUser);
        if (wamoliUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wamoliUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wamoliUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WamoliUser result = wamoliUserService.save(wamoliUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wamoliUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wamoli-users/:id} : Partial updates given fields of an existing wamoliUser, field will ignore if it is null
     *
     * @param id the id of the wamoliUser to save.
     * @param wamoliUser the wamoliUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wamoliUser,
     * or with status {@code 400 (Bad Request)} if the wamoliUser is not valid,
     * or with status {@code 404 (Not Found)} if the wamoliUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the wamoliUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wamoli-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WamoliUser> partialUpdateWamoliUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WamoliUser wamoliUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update WamoliUser partially : {}, {}", id, wamoliUser);
        if (wamoliUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wamoliUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wamoliUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WamoliUser> result = wamoliUserService.partialUpdate(wamoliUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wamoliUser.getId().toString())
        );
    }

    /**
     * {@code GET  /wamoli-users} : get all the wamoliUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wamoliUsers in body.
     */
    @GetMapping("/wamoli-users")
    public ResponseEntity<List<WamoliUser>> getAllWamoliUsers(WamoliUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WamoliUsers by criteria: {}", criteria);
        Page<WamoliUser> page = wamoliUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wamoli-users/count} : count all the wamoliUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/wamoli-users/count")
    public ResponseEntity<Long> countWamoliUsers(WamoliUserCriteria criteria) {
        log.debug("REST request to count WamoliUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(wamoliUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /wamoli-users/:id} : get the "id" wamoliUser.
     *
     * @param id the id of the wamoliUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wamoliUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wamoli-users/{id}")
    public ResponseEntity<WamoliUser> getWamoliUser(@PathVariable Long id) {
        log.debug("REST request to get WamoliUser : {}", id);
        Optional<WamoliUser> wamoliUser = wamoliUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wamoliUser);
    }

    /**
     * {@code DELETE  /wamoli-users/:id} : delete the "id" wamoliUser.
     *
     * @param id the id of the wamoliUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wamoli-users/{id}")
    public ResponseEntity<Void> deleteWamoliUser(@PathVariable Long id) {
        log.debug("REST request to delete WamoliUser : {}", id);
        wamoliUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
