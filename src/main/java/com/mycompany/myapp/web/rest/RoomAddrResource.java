package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.RoomAddr;
import com.mycompany.myapp.repository.RoomAddrRepository;
import com.mycompany.myapp.service.RoomAddrQueryService;
import com.mycompany.myapp.service.RoomAddrService;
import com.mycompany.myapp.service.criteria.RoomAddrCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RoomAddr}.
 */
@RestController
@RequestMapping("/api")
public class RoomAddrResource {

    private final Logger log = LoggerFactory.getLogger(RoomAddrResource.class);

    private static final String ENTITY_NAME = "roomAddr";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomAddrService roomAddrService;

    private final RoomAddrRepository roomAddrRepository;

    private final RoomAddrQueryService roomAddrQueryService;

    public RoomAddrResource(
        RoomAddrService roomAddrService,
        RoomAddrRepository roomAddrRepository,
        RoomAddrQueryService roomAddrQueryService
    ) {
        this.roomAddrService = roomAddrService;
        this.roomAddrRepository = roomAddrRepository;
        this.roomAddrQueryService = roomAddrQueryService;
    }

    /**
     * {@code POST  /room-addrs} : Create a new roomAddr.
     *
     * @param roomAddr the roomAddr to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomAddr, or with status {@code 400 (Bad Request)} if the roomAddr has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/room-addrs")
    public ResponseEntity<RoomAddr> createRoomAddr(@Valid @RequestBody RoomAddr roomAddr) throws URISyntaxException {
        log.debug("REST request to save RoomAddr : {}", roomAddr);
        if (roomAddr.getId() != null) {
            throw new BadRequestAlertException("A new roomAddr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomAddr result = roomAddrService.save(roomAddr);
        return ResponseEntity
            .created(new URI("/api/room-addrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /room-addrs/:id} : Updates an existing roomAddr.
     *
     * @param id the id of the roomAddr to save.
     * @param roomAddr the roomAddr to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomAddr,
     * or with status {@code 400 (Bad Request)} if the roomAddr is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomAddr couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/room-addrs/{id}")
    public ResponseEntity<RoomAddr> updateRoomAddr(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RoomAddr roomAddr
    ) throws URISyntaxException {
        log.debug("REST request to update RoomAddr : {}, {}", id, roomAddr);
        if (roomAddr.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomAddr.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomAddrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoomAddr result = roomAddrService.save(roomAddr);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomAddr.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /room-addrs/:id} : Partial updates given fields of an existing roomAddr, field will ignore if it is null
     *
     * @param id the id of the roomAddr to save.
     * @param roomAddr the roomAddr to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomAddr,
     * or with status {@code 400 (Bad Request)} if the roomAddr is not valid,
     * or with status {@code 404 (Not Found)} if the roomAddr is not found,
     * or with status {@code 500 (Internal Server Error)} if the roomAddr couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/room-addrs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RoomAddr> partialUpdateRoomAddr(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RoomAddr roomAddr
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoomAddr partially : {}, {}", id, roomAddr);
        if (roomAddr.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomAddr.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomAddrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoomAddr> result = roomAddrService.partialUpdate(roomAddr);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomAddr.getId().toString())
        );
    }

    /**
     * {@code GET  /room-addrs} : get all the roomAddrs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomAddrs in body.
     */
    @GetMapping("/room-addrs")
    public ResponseEntity<List<RoomAddr>> getAllRoomAddrs(RoomAddrCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RoomAddrs by criteria: {}", criteria);
        Page<RoomAddr> page = roomAddrQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /room-addrs/count} : count all the roomAddrs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/room-addrs/count")
    public ResponseEntity<Long> countRoomAddrs(RoomAddrCriteria criteria) {
        log.debug("REST request to count RoomAddrs by criteria: {}", criteria);
        return ResponseEntity.ok().body(roomAddrQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /room-addrs/:id} : get the "id" roomAddr.
     *
     * @param id the id of the roomAddr to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomAddr, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/room-addrs/{id}")
    public ResponseEntity<RoomAddr> getRoomAddr(@PathVariable Long id) {
        log.debug("REST request to get RoomAddr : {}", id);
        Optional<RoomAddr> roomAddr = roomAddrService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomAddr);
    }

    /**
     * {@code DELETE  /room-addrs/:id} : delete the "id" roomAddr.
     *
     * @param id the id of the roomAddr to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/room-addrs/{id}")
    public ResponseEntity<Void> deleteRoomAddr(@PathVariable Long id) {
        log.debug("REST request to delete RoomAddr : {}", id);
        roomAddrService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
