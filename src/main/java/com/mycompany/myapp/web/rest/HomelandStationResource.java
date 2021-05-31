package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.HomelandStation;
import com.mycompany.myapp.repository.HomelandStationRepository;
import com.mycompany.myapp.service.HomelandStationQueryService;
import com.mycompany.myapp.service.HomelandStationService;
import com.mycompany.myapp.service.criteria.HomelandStationCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.HomelandStation}.
 */
@RestController
@RequestMapping("/api")
public class HomelandStationResource {

    private final Logger log = LoggerFactory.getLogger(HomelandStationResource.class);

    private static final String ENTITY_NAME = "homelandStation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HomelandStationService homelandStationService;

    private final HomelandStationRepository homelandStationRepository;

    private final HomelandStationQueryService homelandStationQueryService;

    public HomelandStationResource(
        HomelandStationService homelandStationService,
        HomelandStationRepository homelandStationRepository,
        HomelandStationQueryService homelandStationQueryService
    ) {
        this.homelandStationService = homelandStationService;
        this.homelandStationRepository = homelandStationRepository;
        this.homelandStationQueryService = homelandStationQueryService;
    }

    /**
     * {@code POST  /homeland-stations} : Create a new homelandStation.
     *
     * @param homelandStation the homelandStation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new homelandStation, or with status {@code 400 (Bad Request)} if the homelandStation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/homeland-stations")
    public ResponseEntity<HomelandStation> createHomelandStation(@Valid @RequestBody HomelandStation homelandStation)
        throws URISyntaxException {
        log.debug("REST request to save HomelandStation : {}", homelandStation);
        if (homelandStation.getId() != null) {
            throw new BadRequestAlertException("A new homelandStation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HomelandStation result = homelandStationService.save(homelandStation);
        return ResponseEntity
            .created(new URI("/api/homeland-stations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /homeland-stations/:id} : Updates an existing homelandStation.
     *
     * @param id the id of the homelandStation to save.
     * @param homelandStation the homelandStation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated homelandStation,
     * or with status {@code 400 (Bad Request)} if the homelandStation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the homelandStation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/homeland-stations/{id}")
    public ResponseEntity<HomelandStation> updateHomelandStation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HomelandStation homelandStation
    ) throws URISyntaxException {
        log.debug("REST request to update HomelandStation : {}, {}", id, homelandStation);
        if (homelandStation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, homelandStation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!homelandStationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HomelandStation result = homelandStationService.save(homelandStation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, homelandStation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /homeland-stations/:id} : Partial updates given fields of an existing homelandStation, field will ignore if it is null
     *
     * @param id the id of the homelandStation to save.
     * @param homelandStation the homelandStation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated homelandStation,
     * or with status {@code 400 (Bad Request)} if the homelandStation is not valid,
     * or with status {@code 404 (Not Found)} if the homelandStation is not found,
     * or with status {@code 500 (Internal Server Error)} if the homelandStation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/homeland-stations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HomelandStation> partialUpdateHomelandStation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HomelandStation homelandStation
    ) throws URISyntaxException {
        log.debug("REST request to partial update HomelandStation partially : {}, {}", id, homelandStation);
        if (homelandStation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, homelandStation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!homelandStationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HomelandStation> result = homelandStationService.partialUpdate(homelandStation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, homelandStation.getId().toString())
        );
    }

    /**
     * {@code GET  /homeland-stations} : get all the homelandStations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of homelandStations in body.
     */
    @GetMapping("/homeland-stations")
    public ResponseEntity<List<HomelandStation>> getAllHomelandStations(HomelandStationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HomelandStations by criteria: {}", criteria);
        Page<HomelandStation> page = homelandStationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /homeland-stations/count} : count all the homelandStations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/homeland-stations/count")
    public ResponseEntity<Long> countHomelandStations(HomelandStationCriteria criteria) {
        log.debug("REST request to count HomelandStations by criteria: {}", criteria);
        return ResponseEntity.ok().body(homelandStationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /homeland-stations/:id} : get the "id" homelandStation.
     *
     * @param id the id of the homelandStation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the homelandStation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/homeland-stations/{id}")
    public ResponseEntity<HomelandStation> getHomelandStation(@PathVariable Long id) {
        log.debug("REST request to get HomelandStation : {}", id);
        Optional<HomelandStation> homelandStation = homelandStationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(homelandStation);
    }

    /**
     * {@code DELETE  /homeland-stations/:id} : delete the "id" homelandStation.
     *
     * @param id the id of the homelandStation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/homeland-stations/{id}")
    public ResponseEntity<Void> deleteHomelandStation(@PathVariable Long id) {
        log.debug("REST request to delete HomelandStation : {}", id);
        homelandStationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
