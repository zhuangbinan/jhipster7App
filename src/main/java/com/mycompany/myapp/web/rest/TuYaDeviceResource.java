package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TuYaDevice;
import com.mycompany.myapp.repository.TuYaDeviceRepository;
import com.mycompany.myapp.service.TuYaDeviceQueryService;
import com.mycompany.myapp.service.TuYaDeviceService;
import com.mycompany.myapp.service.criteria.TuYaDeviceCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TuYaDevice}.
 */
@RestController
@RequestMapping("/api")
public class TuYaDeviceResource {

    private final Logger log = LoggerFactory.getLogger(TuYaDeviceResource.class);

    private static final String ENTITY_NAME = "tuYaDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TuYaDeviceService tuYaDeviceService;

    private final TuYaDeviceRepository tuYaDeviceRepository;

    private final TuYaDeviceQueryService tuYaDeviceQueryService;

    public TuYaDeviceResource(
        TuYaDeviceService tuYaDeviceService,
        TuYaDeviceRepository tuYaDeviceRepository,
        TuYaDeviceQueryService tuYaDeviceQueryService
    ) {
        this.tuYaDeviceService = tuYaDeviceService;
        this.tuYaDeviceRepository = tuYaDeviceRepository;
        this.tuYaDeviceQueryService = tuYaDeviceQueryService;
    }

    /**
     * {@code POST  /tu-ya-devices} : Create a new tuYaDevice.
     *
     * @param tuYaDevice the tuYaDevice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tuYaDevice, or with status {@code 400 (Bad Request)} if the tuYaDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tu-ya-devices")
    public ResponseEntity<TuYaDevice> createTuYaDevice(@Valid @RequestBody TuYaDevice tuYaDevice) throws URISyntaxException {
        log.debug("REST request to save TuYaDevice : {}", tuYaDevice);
        if (tuYaDevice.getId() != null) {
            throw new BadRequestAlertException("A new tuYaDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TuYaDevice result = tuYaDeviceService.save(tuYaDevice);
        return ResponseEntity
            .created(new URI("/api/tu-ya-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tu-ya-devices/:id} : Updates an existing tuYaDevice.
     *
     * @param id the id of the tuYaDevice to save.
     * @param tuYaDevice the tuYaDevice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tuYaDevice,
     * or with status {@code 400 (Bad Request)} if the tuYaDevice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tuYaDevice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tu-ya-devices/{id}")
    public ResponseEntity<TuYaDevice> updateTuYaDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TuYaDevice tuYaDevice
    ) throws URISyntaxException {
        log.debug("REST request to update TuYaDevice : {}, {}", id, tuYaDevice);
        if (tuYaDevice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tuYaDevice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tuYaDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TuYaDevice result = tuYaDeviceService.save(tuYaDevice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tuYaDevice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tu-ya-devices/:id} : Partial updates given fields of an existing tuYaDevice, field will ignore if it is null
     *
     * @param id the id of the tuYaDevice to save.
     * @param tuYaDevice the tuYaDevice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tuYaDevice,
     * or with status {@code 400 (Bad Request)} if the tuYaDevice is not valid,
     * or with status {@code 404 (Not Found)} if the tuYaDevice is not found,
     * or with status {@code 500 (Internal Server Error)} if the tuYaDevice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tu-ya-devices/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TuYaDevice> partialUpdateTuYaDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TuYaDevice tuYaDevice
    ) throws URISyntaxException {
        log.debug("REST request to partial update TuYaDevice partially : {}, {}", id, tuYaDevice);
        if (tuYaDevice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tuYaDevice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tuYaDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TuYaDevice> result = tuYaDeviceService.partialUpdate(tuYaDevice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tuYaDevice.getId().toString())
        );
    }

    /**
     * {@code GET  /tu-ya-devices} : get all the tuYaDevices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tuYaDevices in body.
     */
    @GetMapping("/tu-ya-devices")
    public ResponseEntity<List<TuYaDevice>> getAllTuYaDevices(TuYaDeviceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TuYaDevices by criteria: {}", criteria);
        Page<TuYaDevice> page = tuYaDeviceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tu-ya-devices/count} : count all the tuYaDevices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tu-ya-devices/count")
    public ResponseEntity<Long> countTuYaDevices(TuYaDeviceCriteria criteria) {
        log.debug("REST request to count TuYaDevices by criteria: {}", criteria);
        return ResponseEntity.ok().body(tuYaDeviceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tu-ya-devices/:id} : get the "id" tuYaDevice.
     *
     * @param id the id of the tuYaDevice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tuYaDevice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tu-ya-devices/{id}")
    public ResponseEntity<TuYaDevice> getTuYaDevice(@PathVariable Long id) {
        log.debug("REST request to get TuYaDevice : {}", id);
        Optional<TuYaDevice> tuYaDevice = tuYaDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tuYaDevice);
    }

    /**
     * {@code DELETE  /tu-ya-devices/:id} : delete the "id" tuYaDevice.
     *
     * @param id the id of the tuYaDevice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tu-ya-devices/{id}")
    public ResponseEntity<Void> deleteTuYaDevice(@PathVariable Long id) {
        log.debug("REST request to delete TuYaDevice : {}", id);
        tuYaDeviceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
