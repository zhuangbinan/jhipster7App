package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CompanyPost;
import com.mycompany.myapp.repository.CompanyPostRepository;
import com.mycompany.myapp.service.CompanyPostQueryService;
import com.mycompany.myapp.service.CompanyPostService;
import com.mycompany.myapp.service.criteria.CompanyPostCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CompanyPost}.
 */
@RestController
@RequestMapping("/api")
public class CompanyPostResource {

    private final Logger log = LoggerFactory.getLogger(CompanyPostResource.class);

    private static final String ENTITY_NAME = "companyPost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyPostService companyPostService;

    private final CompanyPostRepository companyPostRepository;

    private final CompanyPostQueryService companyPostQueryService;

    public CompanyPostResource(
        CompanyPostService companyPostService,
        CompanyPostRepository companyPostRepository,
        CompanyPostQueryService companyPostQueryService
    ) {
        this.companyPostService = companyPostService;
        this.companyPostRepository = companyPostRepository;
        this.companyPostQueryService = companyPostQueryService;
    }

    /**
     * {@code POST  /company-posts} : Create a new companyPost.
     *
     * @param companyPost the companyPost to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyPost, or with status {@code 400 (Bad Request)} if the companyPost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-posts")
    public ResponseEntity<CompanyPost> createCompanyPost(@RequestBody CompanyPost companyPost) throws URISyntaxException {
        log.debug("REST request to save CompanyPost : {}", companyPost);
        if (companyPost.getId() != null) {
            throw new BadRequestAlertException("A new companyPost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyPost result = companyPostService.save(companyPost);
        return ResponseEntity
            .created(new URI("/api/company-posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-posts/:id} : Updates an existing companyPost.
     *
     * @param id the id of the companyPost to save.
     * @param companyPost the companyPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyPost,
     * or with status {@code 400 (Bad Request)} if the companyPost is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-posts/{id}")
    public ResponseEntity<CompanyPost> updateCompanyPost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyPost companyPost
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyPost : {}, {}", id, companyPost);
        if (companyPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyPost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyPostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyPost result = companyPostService.save(companyPost);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyPost.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-posts/:id} : Partial updates given fields of an existing companyPost, field will ignore if it is null
     *
     * @param id the id of the companyPost to save.
     * @param companyPost the companyPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyPost,
     * or with status {@code 400 (Bad Request)} if the companyPost is not valid,
     * or with status {@code 404 (Not Found)} if the companyPost is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/company-posts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CompanyPost> partialUpdateCompanyPost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyPost companyPost
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyPost partially : {}, {}", id, companyPost);
        if (companyPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyPost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyPostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyPost> result = companyPostService.partialUpdate(companyPost);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyPost.getId().toString())
        );
    }

    /**
     * {@code GET  /company-posts} : get all the companyPosts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyPosts in body.
     */
    @GetMapping("/company-posts")
    public ResponseEntity<List<CompanyPost>> getAllCompanyPosts(CompanyPostCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CompanyPosts by criteria: {}", criteria);
        Page<CompanyPost> page = companyPostQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /company-posts/count} : count all the companyPosts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/company-posts/count")
    public ResponseEntity<Long> countCompanyPosts(CompanyPostCriteria criteria) {
        log.debug("REST request to count CompanyPosts by criteria: {}", criteria);
        return ResponseEntity.ok().body(companyPostQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /company-posts/:id} : get the "id" companyPost.
     *
     * @param id the id of the companyPost to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyPost, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-posts/{id}")
    public ResponseEntity<CompanyPost> getCompanyPost(@PathVariable Long id) {
        log.debug("REST request to get CompanyPost : {}", id);
        Optional<CompanyPost> companyPost = companyPostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyPost);
    }

    /**
     * {@code DELETE  /company-posts/:id} : delete the "id" companyPost.
     *
     * @param id the id of the companyPost to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/company-posts/{id}")
    public ResponseEntity<Void> deleteCompanyPost(@PathVariable Long id) {
        log.debug("REST request to delete CompanyPost : {}", id);
        companyPostService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
