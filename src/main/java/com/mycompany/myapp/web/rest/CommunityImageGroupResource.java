package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CommunityImageGroup;
import com.mycompany.myapp.repository.CommunityImageGroupRepository;
import com.mycompany.myapp.service.CommunityImageGroupQueryService;
import com.mycompany.myapp.service.CommunityImageGroupService;
import com.mycompany.myapp.service.criteria.CommunityImageGroupCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CommunityImageGroup}.
 */
@RestController
@RequestMapping("/api")
public class CommunityImageGroupResource {

    private final Logger log = LoggerFactory.getLogger(CommunityImageGroupResource.class);

    private static final String ENTITY_NAME = "communityImageGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunityImageGroupService communityImageGroupService;

    private final CommunityImageGroupRepository communityImageGroupRepository;

    private final CommunityImageGroupQueryService communityImageGroupQueryService;

    public CommunityImageGroupResource(
        CommunityImageGroupService communityImageGroupService,
        CommunityImageGroupRepository communityImageGroupRepository,
        CommunityImageGroupQueryService communityImageGroupQueryService
    ) {
        this.communityImageGroupService = communityImageGroupService;
        this.communityImageGroupRepository = communityImageGroupRepository;
        this.communityImageGroupQueryService = communityImageGroupQueryService;
    }

    /**
     * {@code POST  /community-image-groups} : Create a new communityImageGroup.
     *
     * @param communityImageGroup the communityImageGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communityImageGroup, or with status {@code 400 (Bad Request)} if the communityImageGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/community-image-groups")
    public ResponseEntity<CommunityImageGroup> createCommunityImageGroup(@Valid @RequestBody CommunityImageGroup communityImageGroup)
        throws URISyntaxException {
        log.debug("REST request to save CommunityImageGroup : {}", communityImageGroup);
        if (communityImageGroup.getId() != null) {
            throw new BadRequestAlertException("A new communityImageGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommunityImageGroup result = communityImageGroupService.save(communityImageGroup);
        return ResponseEntity
            .created(new URI("/api/community-image-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /community-image-groups/:id} : Updates an existing communityImageGroup.
     *
     * @param id the id of the communityImageGroup to save.
     * @param communityImageGroup the communityImageGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityImageGroup,
     * or with status {@code 400 (Bad Request)} if the communityImageGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communityImageGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/community-image-groups/{id}")
    public ResponseEntity<CommunityImageGroup> updateCommunityImageGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommunityImageGroup communityImageGroup
    ) throws URISyntaxException {
        log.debug("REST request to update CommunityImageGroup : {}, {}", id, communityImageGroup);
        if (communityImageGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityImageGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityImageGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommunityImageGroup result = communityImageGroupService.save(communityImageGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityImageGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /community-image-groups/:id} : Partial updates given fields of an existing communityImageGroup, field will ignore if it is null
     *
     * @param id the id of the communityImageGroup to save.
     * @param communityImageGroup the communityImageGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityImageGroup,
     * or with status {@code 400 (Bad Request)} if the communityImageGroup is not valid,
     * or with status {@code 404 (Not Found)} if the communityImageGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the communityImageGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/community-image-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommunityImageGroup> partialUpdateCommunityImageGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommunityImageGroup communityImageGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommunityImageGroup partially : {}, {}", id, communityImageGroup);
        if (communityImageGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityImageGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityImageGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunityImageGroup> result = communityImageGroupService.partialUpdate(communityImageGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityImageGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /community-image-groups} : get all the communityImageGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communityImageGroups in body.
     */
    @GetMapping("/community-image-groups")
    public ResponseEntity<List<CommunityImageGroup>> getAllCommunityImageGroups(CommunityImageGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CommunityImageGroups by criteria: {}", criteria);
        Page<CommunityImageGroup> page = communityImageGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /community-image-groups/count} : count all the communityImageGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/community-image-groups/count")
    public ResponseEntity<Long> countCommunityImageGroups(CommunityImageGroupCriteria criteria) {
        log.debug("REST request to count CommunityImageGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(communityImageGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /community-image-groups/:id} : get the "id" communityImageGroup.
     *
     * @param id the id of the communityImageGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communityImageGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/community-image-groups/{id}")
    public ResponseEntity<CommunityImageGroup> getCommunityImageGroup(@PathVariable Long id) {
        log.debug("REST request to get CommunityImageGroup : {}", id);
        Optional<CommunityImageGroup> communityImageGroup = communityImageGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(communityImageGroup);
    }

    /**
     * {@code DELETE  /community-image-groups/:id} : delete the "id" communityImageGroup.
     *
     * @param id the id of the communityImageGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/community-image-groups/{id}")
    public ResponseEntity<Void> deleteCommunityImageGroup(@PathVariable Long id) {
        log.debug("REST request to delete CommunityImageGroup : {}", id);
        communityImageGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
