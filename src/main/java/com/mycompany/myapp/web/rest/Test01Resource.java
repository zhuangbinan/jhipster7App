package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.Test01Repository;
import com.mycompany.myapp.service.Test01QueryService;
import com.mycompany.myapp.service.Test01Service;
import com.mycompany.myapp.service.criteria.Test01Criteria;
import com.mycompany.myapp.service.dto.Test01DTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Test01}.
 */
@RestController
@RequestMapping("/api")
public class Test01Resource {

    private final Logger log = LoggerFactory.getLogger(Test01Resource.class);

    private static final String ENTITY_NAME = "test01";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Test01Service test01Service;

    private final Test01Repository test01Repository;

    private final Test01QueryService test01QueryService;

    public Test01Resource(Test01Service test01Service, Test01Repository test01Repository, Test01QueryService test01QueryService) {
        this.test01Service = test01Service;
        this.test01Repository = test01Repository;
        this.test01QueryService = test01QueryService;
    }

    /**
     * {@code POST  /test-01-s} : Create a new test01.
     *
     * @param test01DTO the test01DTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new test01DTO, or with status {@code 400 (Bad Request)} if the test01 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-01-s")
    public ResponseEntity<Test01DTO> createTest01(@RequestBody Test01DTO test01DTO) throws URISyntaxException {
        log.debug("REST request to save Test01 : {}", test01DTO);
        if (test01DTO.getId() != null) {
            throw new BadRequestAlertException("A new test01 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Test01DTO result = test01Service.save(test01DTO);
        return ResponseEntity
            .created(new URI("/api/test-01-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-01-s/:id} : Updates an existing test01.
     *
     * @param id the id of the test01DTO to save.
     * @param test01DTO the test01DTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated test01DTO,
     * or with status {@code 400 (Bad Request)} if the test01DTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the test01DTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-01-s/{id}")
    public ResponseEntity<Test01DTO> updateTest01(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Test01DTO test01DTO
    ) throws URISyntaxException {
        log.debug("REST request to update Test01 : {}, {}", id, test01DTO);
        if (test01DTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, test01DTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!test01Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Test01DTO result = test01Service.save(test01DTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, test01DTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-01-s/:id} : Partial updates given fields of an existing test01, field will ignore if it is null
     *
     * @param id the id of the test01DTO to save.
     * @param test01DTO the test01DTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated test01DTO,
     * or with status {@code 400 (Bad Request)} if the test01DTO is not valid,
     * or with status {@code 404 (Not Found)} if the test01DTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the test01DTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-01-s/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Test01DTO> partialUpdateTest01(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Test01DTO test01DTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Test01 partially : {}, {}", id, test01DTO);
        if (test01DTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, test01DTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!test01Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Test01DTO> result = test01Service.partialUpdate(test01DTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, test01DTO.getId().toString())
        );
    }

    /**
     * {@code GET  /test-01-s} : get all the test01s.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of test01s in body.
     */
    @GetMapping("/test-01-s")
    public ResponseEntity<List<Test01DTO>> getAllTest01s(Test01Criteria criteria, Pageable pageable) {
        log.debug("REST request to get Test01s by criteria: {}", criteria);
        Page<Test01DTO> page = test01QueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-01-s/count} : count all the test01s.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-01-s/count")
    public ResponseEntity<Long> countTest01s(Test01Criteria criteria) {
        log.debug("REST request to count Test01s by criteria: {}", criteria);
        return ResponseEntity.ok().body(test01QueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-01-s/:id} : get the "id" test01.
     *
     * @param id the id of the test01DTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the test01DTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-01-s/{id}")
    public ResponseEntity<Test01DTO> getTest01(@PathVariable Long id) {
        log.debug("REST request to get Test01 : {}", id);
        Optional<Test01DTO> test01DTO = test01Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(test01DTO);
    }

    /**
     * {@code DELETE  /test-01-s/:id} : delete the "id" test01.
     *
     * @param id the id of the test01DTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-01-s/{id}")
    public ResponseEntity<Void> deleteTest01(@PathVariable Long id) {
        log.debug("REST request to delete Test01 : {}", id);
        test01Service.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
