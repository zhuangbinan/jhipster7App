package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CommunityNotice;
import com.mycompany.myapp.repository.CommunityNoticeRepository;
import com.mycompany.myapp.service.CommunityNoticeQueryService;
import com.mycompany.myapp.service.CommunityNoticeService;
import com.mycompany.myapp.service.criteria.CommunityNoticeCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CommunityNotice}.
 */
@RestController
@RequestMapping("/api")
public class CommunityNoticeResource {

    private final Logger log = LoggerFactory.getLogger(CommunityNoticeResource.class);

    private static final String ENTITY_NAME = "communityNotice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunityNoticeService communityNoticeService;

    private final CommunityNoticeRepository communityNoticeRepository;

    private final CommunityNoticeQueryService communityNoticeQueryService;

    public CommunityNoticeResource(
        CommunityNoticeService communityNoticeService,
        CommunityNoticeRepository communityNoticeRepository,
        CommunityNoticeQueryService communityNoticeQueryService
    ) {
        this.communityNoticeService = communityNoticeService;
        this.communityNoticeRepository = communityNoticeRepository;
        this.communityNoticeQueryService = communityNoticeQueryService;
    }

    /**
     * {@code POST  /community-notices} : Create a new communityNotice.
     *
     * @param communityNotice the communityNotice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communityNotice, or with status {@code 400 (Bad Request)} if the communityNotice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/community-notices")
    public ResponseEntity<CommunityNotice> createCommunityNotice(@RequestBody CommunityNotice communityNotice) throws URISyntaxException {
        log.debug("REST request to save CommunityNotice : {}", communityNotice);
        if (communityNotice.getId() != null) {
            throw new BadRequestAlertException("A new communityNotice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommunityNotice result = communityNoticeService.save(communityNotice);
        return ResponseEntity
            .created(new URI("/api/community-notices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /community-notices/:id} : Updates an existing communityNotice.
     *
     * @param id the id of the communityNotice to save.
     * @param communityNotice the communityNotice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityNotice,
     * or with status {@code 400 (Bad Request)} if the communityNotice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communityNotice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/community-notices/{id}")
    public ResponseEntity<CommunityNotice> updateCommunityNotice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityNotice communityNotice
    ) throws URISyntaxException {
        log.debug("REST request to update CommunityNotice : {}, {}", id, communityNotice);
        if (communityNotice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityNotice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityNoticeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommunityNotice result = communityNoticeService.save(communityNotice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityNotice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /community-notices/:id} : Partial updates given fields of an existing communityNotice, field will ignore if it is null
     *
     * @param id the id of the communityNotice to save.
     * @param communityNotice the communityNotice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityNotice,
     * or with status {@code 400 (Bad Request)} if the communityNotice is not valid,
     * or with status {@code 404 (Not Found)} if the communityNotice is not found,
     * or with status {@code 500 (Internal Server Error)} if the communityNotice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/community-notices/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommunityNotice> partialUpdateCommunityNotice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityNotice communityNotice
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommunityNotice partially : {}, {}", id, communityNotice);
        if (communityNotice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityNotice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityNoticeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunityNotice> result = communityNoticeService.partialUpdate(communityNotice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityNotice.getId().toString())
        );
    }

    /**
     * {@code GET  /community-notices} : get all the communityNotices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communityNotices in body.
     */
    @GetMapping("/community-notices")
    public ResponseEntity<List<CommunityNotice>> getAllCommunityNotices(CommunityNoticeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CommunityNotices by criteria: {}", criteria);
        Page<CommunityNotice> page = communityNoticeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /community-notices/count} : count all the communityNotices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/community-notices/count")
    public ResponseEntity<Long> countCommunityNotices(CommunityNoticeCriteria criteria) {
        log.debug("REST request to count CommunityNotices by criteria: {}", criteria);
        return ResponseEntity.ok().body(communityNoticeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /community-notices/:id} : get the "id" communityNotice.
     *
     * @param id the id of the communityNotice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communityNotice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/community-notices/{id}")
    public ResponseEntity<CommunityNotice> getCommunityNotice(@PathVariable Long id) {
        log.debug("REST request to get CommunityNotice : {}", id);
        Optional<CommunityNotice> communityNotice = communityNoticeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(communityNotice);
    }

    /**
     * {@code DELETE  /community-notices/:id} : delete the "id" communityNotice.
     *
     * @param id the id of the communityNotice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/community-notices/{id}")
    public ResponseEntity<Void> deleteCommunityNotice(@PathVariable Long id) {
        log.debug("REST request to delete CommunityNotice : {}", id);
        communityNoticeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
