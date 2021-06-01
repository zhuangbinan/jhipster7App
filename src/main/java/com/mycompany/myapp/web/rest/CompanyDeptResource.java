package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CompanyDept;
import com.mycompany.myapp.repository.CompanyDeptRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.CompanyDeptQueryService;
import com.mycompany.myapp.service.CompanyDeptService;
import com.mycompany.myapp.service.criteria.CompanyDeptCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CompanyDept}.
 */
@RestController
@RequestMapping("/api")
public class CompanyDeptResource {

    private final Logger log = LoggerFactory.getLogger(CompanyDeptResource.class);

    private static final String ENTITY_NAME = "companyDept";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyDeptService companyDeptService;

    private final CompanyDeptRepository companyDeptRepository;

    private final CompanyDeptQueryService companyDeptQueryService;

    public CompanyDeptResource(
        CompanyDeptService companyDeptService,
        CompanyDeptRepository companyDeptRepository,
        CompanyDeptQueryService companyDeptQueryService
    ) {
        this.companyDeptService = companyDeptService;
        this.companyDeptRepository = companyDeptRepository;
        this.companyDeptQueryService = companyDeptQueryService;
    }

    /**
     * {@code POST  /company-depts} : Create a new companyDept.
     *
     * @param companyDept the companyDept to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyDept, or with status {@code 400 (Bad Request)} if the companyDept has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-depts")
    public ResponseEntity<CompanyDept> createCompanyDept(@RequestBody CompanyDept companyDept) throws URISyntaxException {
        log.debug("REST request to save CompanyDept : {}", companyDept);
        if (companyDept.getId() != null) {
            throw new BadRequestAlertException("A new companyDept cannot already have an ID", ENTITY_NAME, "idexists");
        }
        //新增部门时添加创建人和时间
        companyDept.setCreateBy(SecurityUtils.getCurrentUserLogin().get());
        companyDept.setCreateDate(Instant.now());
        CompanyDept result = companyDeptService.save(companyDept);
        return ResponseEntity
            .created(new URI("/api/company-depts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-depts/:id} : Updates an existing companyDept.
     *
     * @param id the id of the companyDept to save.
     * @param companyDept the companyDept to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDept,
     * or with status {@code 400 (Bad Request)} if the companyDept is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyDept couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-depts/{id}")
    public ResponseEntity<CompanyDept> updateCompanyDept(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyDept companyDept
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyDept : {}, {}", id, companyDept);
        if (companyDept.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyDept.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyDeptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        companyDept.setLastModifyDate(Instant.now());
        companyDept.setLastModifyBy(SecurityUtils.getCurrentUserLogin().get());
        CompanyDept result = companyDeptService.save(companyDept);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyDept.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-depts/:id} : Partial updates given fields of an existing companyDept, field will ignore if it is null
     *
     * @param id the id of the companyDept to save.
     * @param companyDept the companyDept to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDept,
     * or with status {@code 400 (Bad Request)} if the companyDept is not valid,
     * or with status {@code 404 (Not Found)} if the companyDept is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyDept couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/company-depts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CompanyDept> partialUpdateCompanyDept(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyDept companyDept
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyDept partially : {}, {}", id, companyDept);
        if (companyDept.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyDept.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyDeptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyDept> result = companyDeptService.partialUpdate(companyDept);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyDept.getId().toString())
        );
    }

    /**
     * {@code GET  /company-depts} : get all the companyDepts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyDepts in body.
     */
    @GetMapping("/company-depts")
    public ResponseEntity<List<CompanyDept>> getAllCompanyDepts(CompanyDeptCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CompanyDepts by criteria: {}", criteria);
        Page<CompanyDept> page = companyDeptQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /company-depts/count} : count all the companyDepts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/company-depts/count")
    public ResponseEntity<Long> countCompanyDepts(CompanyDeptCriteria criteria) {
        log.debug("REST request to count CompanyDepts by criteria: {}", criteria);
        return ResponseEntity.ok().body(companyDeptQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /company-depts/:id} : get the "id" companyDept.
     *
     * @param id the id of the companyDept to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyDept, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-depts/{id}")
    public ResponseEntity<CompanyDept> getCompanyDept(@PathVariable Long id) {
        log.debug("REST request to get CompanyDept : {}", id);
        Optional<CompanyDept> companyDept = companyDeptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyDept);
    }

    /**
     * {@code DELETE  /company-depts/:id} : delete the "id" companyDept.
     *
     * @param id the id of the companyDept to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/company-depts/{id}")
    public ResponseEntity<Void> deleteCompanyDept(@PathVariable Long id) {
        log.debug("REST request to delete CompanyDept : {}", id);
        companyDeptService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
