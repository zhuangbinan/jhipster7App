package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CommunityImages;
import com.mycompany.myapp.repository.CommunityImagesRepository;
import com.mycompany.myapp.service.CommunityImagesQueryService;
import com.mycompany.myapp.service.CommunityImagesService;
import com.mycompany.myapp.service.criteria.CommunityImagesCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CommunityImages}.
 */
@RestController
@RequestMapping("/api")
public class CommunityImagesResource {

    private final Logger log = LoggerFactory.getLogger(CommunityImagesResource.class);

    private static final String ENTITY_NAME = "communityImages";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunityImagesService communityImagesService;

    private final CommunityImagesRepository communityImagesRepository;

    private final CommunityImagesQueryService communityImagesQueryService;

    public CommunityImagesResource(
        CommunityImagesService communityImagesService,
        CommunityImagesRepository communityImagesRepository,
        CommunityImagesQueryService communityImagesQueryService
    ) {
        this.communityImagesService = communityImagesService;
        this.communityImagesRepository = communityImagesRepository;
        this.communityImagesQueryService = communityImagesQueryService;
    }

    /**
     * {@code POST  /community-images} : Create a new communityImages.
     *
     * @param communityImages the communityImages to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communityImages, or with status {@code 400 (Bad Request)} if the communityImages has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/community-images")
    public ResponseEntity<CommunityImages> createCommunityImages(@RequestBody CommunityImages communityImages) throws URISyntaxException {
        log.debug("REST request to save CommunityImages : {}", communityImages);
        if (communityImages.getId() != null) {
            throw new BadRequestAlertException("A new communityImages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommunityImages result = communityImagesService.save(communityImages);
        return ResponseEntity
            .created(new URI("/api/community-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /community-images/:id} : Updates an existing communityImages.
     *
     * @param id the id of the communityImages to save.
     * @param communityImages the communityImages to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityImages,
     * or with status {@code 400 (Bad Request)} if the communityImages is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communityImages couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/community-images/{id}")
    public ResponseEntity<CommunityImages> updateCommunityImages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityImages communityImages
    ) throws URISyntaxException {
        log.debug("REST request to update CommunityImages : {}, {}", id, communityImages);
        if (communityImages.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityImages.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityImagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommunityImages result = communityImagesService.save(communityImages);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityImages.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /community-images/:id} : Partial updates given fields of an existing communityImages, field will ignore if it is null
     *
     * @param id the id of the communityImages to save.
     * @param communityImages the communityImages to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityImages,
     * or with status {@code 400 (Bad Request)} if the communityImages is not valid,
     * or with status {@code 404 (Not Found)} if the communityImages is not found,
     * or with status {@code 500 (Internal Server Error)} if the communityImages couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/community-images/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommunityImages> partialUpdateCommunityImages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityImages communityImages
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommunityImages partially : {}, {}", id, communityImages);
        if (communityImages.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityImages.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityImagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunityImages> result = communityImagesService.partialUpdate(communityImages);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityImages.getId().toString())
        );
    }

    /**
     * {@code GET  /community-images} : get all the communityImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communityImages in body.
     */
    @GetMapping("/community-images")
    public ResponseEntity<List<CommunityImages>> getAllCommunityImages(CommunityImagesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CommunityImages by criteria: {}", criteria);
        Page<CommunityImages> page = communityImagesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /community-images/count} : count all the communityImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/community-images/count")
    public ResponseEntity<Long> countCommunityImages(CommunityImagesCriteria criteria) {
        log.debug("REST request to count CommunityImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(communityImagesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /community-images/:id} : get the "id" communityImages.
     *
     * @param id the id of the communityImages to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communityImages, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/community-images/{id}")
    public ResponseEntity<CommunityImages> getCommunityImages(@PathVariable Long id) {
        log.debug("REST request to get CommunityImages : {}", id);
        Optional<CommunityImages> communityImages = communityImagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(communityImages);
    }

    /**
     * {@code DELETE  /community-images/:id} : delete the "id" communityImages.
     *
     * @param id the id of the communityImages to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/community-images/{id}")
    public ResponseEntity<Void> deleteCommunityImages(@PathVariable Long id) {
        log.debug("REST request to delete CommunityImages : {}", id);
        communityImagesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
