package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.WamoliUserLocation;
import com.mycompany.myapp.repository.WamoliUserLocationRepository;
import com.mycompany.myapp.service.WamoliUserLocationService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WamoliUserLocation}.
 */
@RestController
@RequestMapping("/api")
public class WamoliUserLocationResource {

    private final Logger log = LoggerFactory.getLogger(WamoliUserLocationResource.class);

    private static final String ENTITY_NAME = "wamoliUserLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WamoliUserLocationService wamoliUserLocationService;

    private final WamoliUserLocationRepository wamoliUserLocationRepository;

    public WamoliUserLocationResource(
        WamoliUserLocationService wamoliUserLocationService,
        WamoliUserLocationRepository wamoliUserLocationRepository
    ) {
        this.wamoliUserLocationService = wamoliUserLocationService;
        this.wamoliUserLocationRepository = wamoliUserLocationRepository;
    }

    /**
     * {@code POST  /wamoli-user-locations} : Create a new wamoliUserLocation.
     *
     * @param wamoliUserLocation the wamoliUserLocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wamoliUserLocation, or with status {@code 400 (Bad Request)} if the wamoliUserLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wamoli-user-locations")
    public ResponseEntity<WamoliUserLocation> createWamoliUserLocation(@Valid @RequestBody WamoliUserLocation wamoliUserLocation)
        throws URISyntaxException {
        log.debug("REST request to save WamoliUserLocation : {}", wamoliUserLocation);
        if (wamoliUserLocation.getId() != null) {
            throw new BadRequestAlertException("A new wamoliUserLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WamoliUserLocation result = wamoliUserLocationService.save(wamoliUserLocation);
        return ResponseEntity
            .created(new URI("/api/wamoli-user-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wamoli-user-locations/:id} : Updates an existing wamoliUserLocation.
     *
     * @param id the id of the wamoliUserLocation to save.
     * @param wamoliUserLocation the wamoliUserLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wamoliUserLocation,
     * or with status {@code 400 (Bad Request)} if the wamoliUserLocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wamoliUserLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wamoli-user-locations/{id}")
    public ResponseEntity<WamoliUserLocation> updateWamoliUserLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WamoliUserLocation wamoliUserLocation
    ) throws URISyntaxException {
        log.debug("REST request to update WamoliUserLocation : {}, {}", id, wamoliUserLocation);
        if (wamoliUserLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wamoliUserLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wamoliUserLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WamoliUserLocation result = wamoliUserLocationService.save(wamoliUserLocation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wamoliUserLocation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wamoli-user-locations/:id} : Partial updates given fields of an existing wamoliUserLocation, field will ignore if it is null
     *
     * @param id the id of the wamoliUserLocation to save.
     * @param wamoliUserLocation the wamoliUserLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wamoliUserLocation,
     * or with status {@code 400 (Bad Request)} if the wamoliUserLocation is not valid,
     * or with status {@code 404 (Not Found)} if the wamoliUserLocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the wamoliUserLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wamoli-user-locations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WamoliUserLocation> partialUpdateWamoliUserLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WamoliUserLocation wamoliUserLocation
    ) throws URISyntaxException {
        log.debug("REST request to partial update WamoliUserLocation partially : {}, {}", id, wamoliUserLocation);
        if (wamoliUserLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wamoliUserLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wamoliUserLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WamoliUserLocation> result = wamoliUserLocationService.partialUpdate(wamoliUserLocation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wamoliUserLocation.getId().toString())
        );
    }

    /**
     * {@code GET  /wamoli-user-locations} : get all the wamoliUserLocations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wamoliUserLocations in body.
     */
    @GetMapping("/wamoli-user-locations")
    public ResponseEntity<List<WamoliUserLocation>> getAllWamoliUserLocations(Pageable pageable) {
        log.debug("REST request to get a page of WamoliUserLocations");
        Page<WamoliUserLocation> page = wamoliUserLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wamoli-user-locations/:id} : get the "id" wamoliUserLocation.
     *
     * @param id the id of the wamoliUserLocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wamoliUserLocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wamoli-user-locations/{id}")
    public ResponseEntity<WamoliUserLocation> getWamoliUserLocation(@PathVariable Long id) {
        log.debug("REST request to get WamoliUserLocation : {}", id);
        Optional<WamoliUserLocation> wamoliUserLocation = wamoliUserLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wamoliUserLocation);
    }

    /**
     * {@code DELETE  /wamoli-user-locations/:id} : delete the "id" wamoliUserLocation.
     *
     * @param id the id of the wamoliUserLocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wamoli-user-locations/{id}")
    public ResponseEntity<Void> deleteWamoliUserLocation(@PathVariable Long id) {
        log.debug("REST request to delete WamoliUserLocation : {}", id);
        wamoliUserLocationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
