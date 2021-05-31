package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CommunityLeader;
import com.mycompany.myapp.repository.CommunityLeaderRepository;
import com.mycompany.myapp.service.CommunityLeaderQueryService;
import com.mycompany.myapp.service.CommunityLeaderService;
import com.mycompany.myapp.service.criteria.CommunityLeaderCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CommunityLeader}.
 */
@RestController
@RequestMapping("/api")
public class CommunityLeaderResource {

    private final Logger log = LoggerFactory.getLogger(CommunityLeaderResource.class);

    private static final String ENTITY_NAME = "communityLeader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunityLeaderService communityLeaderService;

    private final CommunityLeaderRepository communityLeaderRepository;

    private final CommunityLeaderQueryService communityLeaderQueryService;

    public CommunityLeaderResource(
        CommunityLeaderService communityLeaderService,
        CommunityLeaderRepository communityLeaderRepository,
        CommunityLeaderQueryService communityLeaderQueryService
    ) {
        this.communityLeaderService = communityLeaderService;
        this.communityLeaderRepository = communityLeaderRepository;
        this.communityLeaderQueryService = communityLeaderQueryService;
    }

    /**
     * {@code POST  /community-leaders} : Create a new communityLeader.
     *
     * @param communityLeader the communityLeader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communityLeader, or with status {@code 400 (Bad Request)} if the communityLeader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/community-leaders")
    public ResponseEntity<CommunityLeader> createCommunityLeader(@RequestBody CommunityLeader communityLeader) throws URISyntaxException {
        log.debug("REST request to save CommunityLeader : {}", communityLeader);
        if (communityLeader.getId() != null) {
            throw new BadRequestAlertException("A new communityLeader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommunityLeader result = communityLeaderService.save(communityLeader);
        return ResponseEntity
            .created(new URI("/api/community-leaders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /community-leaders/:id} : Updates an existing communityLeader.
     *
     * @param id the id of the communityLeader to save.
     * @param communityLeader the communityLeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityLeader,
     * or with status {@code 400 (Bad Request)} if the communityLeader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communityLeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/community-leaders/{id}")
    public ResponseEntity<CommunityLeader> updateCommunityLeader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityLeader communityLeader
    ) throws URISyntaxException {
        log.debug("REST request to update CommunityLeader : {}, {}", id, communityLeader);
        if (communityLeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityLeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityLeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommunityLeader result = communityLeaderService.save(communityLeader);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityLeader.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /community-leaders/:id} : Partial updates given fields of an existing communityLeader, field will ignore if it is null
     *
     * @param id the id of the communityLeader to save.
     * @param communityLeader the communityLeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityLeader,
     * or with status {@code 400 (Bad Request)} if the communityLeader is not valid,
     * or with status {@code 404 (Not Found)} if the communityLeader is not found,
     * or with status {@code 500 (Internal Server Error)} if the communityLeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/community-leaders/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommunityLeader> partialUpdateCommunityLeader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityLeader communityLeader
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommunityLeader partially : {}, {}", id, communityLeader);
        if (communityLeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityLeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityLeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunityLeader> result = communityLeaderService.partialUpdate(communityLeader);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityLeader.getId().toString())
        );
    }

    /**
     * {@code GET  /community-leaders} : get all the communityLeaders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communityLeaders in body.
     */
    @GetMapping("/community-leaders")
    public ResponseEntity<List<CommunityLeader>> getAllCommunityLeaders(CommunityLeaderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CommunityLeaders by criteria: {}", criteria);
        Page<CommunityLeader> page = communityLeaderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /community-leaders/count} : count all the communityLeaders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/community-leaders/count")
    public ResponseEntity<Long> countCommunityLeaders(CommunityLeaderCriteria criteria) {
        log.debug("REST request to count CommunityLeaders by criteria: {}", criteria);
        return ResponseEntity.ok().body(communityLeaderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /community-leaders/:id} : get the "id" communityLeader.
     *
     * @param id the id of the communityLeader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communityLeader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/community-leaders/{id}")
    public ResponseEntity<CommunityLeader> getCommunityLeader(@PathVariable Long id) {
        log.debug("REST request to get CommunityLeader : {}", id);
        Optional<CommunityLeader> communityLeader = communityLeaderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(communityLeader);
    }

    /**
     * {@code DELETE  /community-leaders/:id} : delete the "id" communityLeader.
     *
     * @param id the id of the communityLeader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/community-leaders/{id}")
    public ResponseEntity<Void> deleteCommunityLeader(@PathVariable Long id) {
        log.debug("REST request to delete CommunityLeader : {}", id);
        communityLeaderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
