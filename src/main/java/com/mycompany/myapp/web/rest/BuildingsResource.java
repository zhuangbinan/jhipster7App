package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Buildings;
import com.mycompany.myapp.repository.BuildingsRepository;
import com.mycompany.myapp.service.BuildingsQueryService;
import com.mycompany.myapp.service.BuildingsService;
import com.mycompany.myapp.service.criteria.BuildingsCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Buildings}.
 */
@RestController
@RequestMapping("/api")
public class BuildingsResource {

    private final Logger log = LoggerFactory.getLogger(BuildingsResource.class);

    private static final String ENTITY_NAME = "buildings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BuildingsService buildingsService;

    private final BuildingsRepository buildingsRepository;

    private final BuildingsQueryService buildingsQueryService;

    public BuildingsResource(
        BuildingsService buildingsService,
        BuildingsRepository buildingsRepository,
        BuildingsQueryService buildingsQueryService
    ) {
        this.buildingsService = buildingsService;
        this.buildingsRepository = buildingsRepository;
        this.buildingsQueryService = buildingsQueryService;
    }

    /**
     * {@code POST  /buildings} : Create a new buildings.
     *
     * @param buildings the buildings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new buildings, or with status {@code 400 (Bad Request)} if the buildings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/buildings")
    public ResponseEntity<Buildings> createBuildings(@Valid @RequestBody Buildings buildings) throws URISyntaxException {
        log.debug("REST request to save Buildings : {}", buildings);
        if (buildings.getId() != null) {
            throw new BadRequestAlertException("A new buildings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Buildings result = buildingsService.save(buildings);
        return ResponseEntity
            .created(new URI("/api/buildings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /buildings/:id} : Updates an existing buildings.
     *
     * @param id the id of the buildings to save.
     * @param buildings the buildings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated buildings,
     * or with status {@code 400 (Bad Request)} if the buildings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the buildings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/buildings/{id}")
    public ResponseEntity<Buildings> updateBuildings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Buildings buildings
    ) throws URISyntaxException {
        log.debug("REST request to update Buildings : {}, {}", id, buildings);
        if (buildings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, buildings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!buildingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Buildings result = buildingsService.save(buildings);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, buildings.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /buildings/:id} : Partial updates given fields of an existing buildings, field will ignore if it is null
     *
     * @param id the id of the buildings to save.
     * @param buildings the buildings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated buildings,
     * or with status {@code 400 (Bad Request)} if the buildings is not valid,
     * or with status {@code 404 (Not Found)} if the buildings is not found,
     * or with status {@code 500 (Internal Server Error)} if the buildings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/buildings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Buildings> partialUpdateBuildings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Buildings buildings
    ) throws URISyntaxException {
        log.debug("REST request to partial update Buildings partially : {}, {}", id, buildings);
        if (buildings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, buildings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!buildingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Buildings> result = buildingsService.partialUpdate(buildings);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, buildings.getId().toString())
        );
    }

    /**
     * {@code GET  /buildings} : get all the buildings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of buildings in body.
     */
    @GetMapping("/buildings")
    public ResponseEntity<List<Buildings>> getAllBuildings(BuildingsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Buildings by criteria: {}", criteria);
        Page<Buildings> page = buildingsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /buildings/count} : count all the buildings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/buildings/count")
    public ResponseEntity<Long> countBuildings(BuildingsCriteria criteria) {
        log.debug("REST request to count Buildings by criteria: {}", criteria);
        return ResponseEntity.ok().body(buildingsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /buildings/:id} : get the "id" buildings.
     *
     * @param id the id of the buildings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the buildings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/buildings/{id}")
    public ResponseEntity<Buildings> getBuildings(@PathVariable Long id) {
        log.debug("REST request to get Buildings : {}", id);
        Optional<Buildings> buildings = buildingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(buildings);
    }

    /**
     * {@code DELETE  /buildings/:id} : delete the "id" buildings.
     *
     * @param id the id of the buildings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/buildings/{id}")
    public ResponseEntity<Void> deleteBuildings(@PathVariable Long id) {
        log.debug("REST request to delete Buildings : {}", id);
        buildingsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
