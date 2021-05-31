package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TuYaCmd;
import com.mycompany.myapp.repository.TuYaCmdRepository;
import com.mycompany.myapp.service.TuYaCmdQueryService;
import com.mycompany.myapp.service.TuYaCmdService;
import com.mycompany.myapp.service.criteria.TuYaCmdCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TuYaCmd}.
 */
@RestController
@RequestMapping("/api")
public class TuYaCmdResource {

    private final Logger log = LoggerFactory.getLogger(TuYaCmdResource.class);

    private static final String ENTITY_NAME = "tuYaCmd";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TuYaCmdService tuYaCmdService;

    private final TuYaCmdRepository tuYaCmdRepository;

    private final TuYaCmdQueryService tuYaCmdQueryService;

    public TuYaCmdResource(TuYaCmdService tuYaCmdService, TuYaCmdRepository tuYaCmdRepository, TuYaCmdQueryService tuYaCmdQueryService) {
        this.tuYaCmdService = tuYaCmdService;
        this.tuYaCmdRepository = tuYaCmdRepository;
        this.tuYaCmdQueryService = tuYaCmdQueryService;
    }

    /**
     * {@code POST  /tu-ya-cmds} : Create a new tuYaCmd.
     *
     * @param tuYaCmd the tuYaCmd to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tuYaCmd, or with status {@code 400 (Bad Request)} if the tuYaCmd has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tu-ya-cmds")
    public ResponseEntity<TuYaCmd> createTuYaCmd(@RequestBody TuYaCmd tuYaCmd) throws URISyntaxException {
        log.debug("REST request to save TuYaCmd : {}", tuYaCmd);
        if (tuYaCmd.getId() != null) {
            throw new BadRequestAlertException("A new tuYaCmd cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TuYaCmd result = tuYaCmdService.save(tuYaCmd);
        return ResponseEntity
            .created(new URI("/api/tu-ya-cmds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tu-ya-cmds/:id} : Updates an existing tuYaCmd.
     *
     * @param id the id of the tuYaCmd to save.
     * @param tuYaCmd the tuYaCmd to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tuYaCmd,
     * or with status {@code 400 (Bad Request)} if the tuYaCmd is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tuYaCmd couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tu-ya-cmds/{id}")
    public ResponseEntity<TuYaCmd> updateTuYaCmd(@PathVariable(value = "id", required = false) final Long id, @RequestBody TuYaCmd tuYaCmd)
        throws URISyntaxException {
        log.debug("REST request to update TuYaCmd : {}, {}", id, tuYaCmd);
        if (tuYaCmd.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tuYaCmd.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tuYaCmdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TuYaCmd result = tuYaCmdService.save(tuYaCmd);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tuYaCmd.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tu-ya-cmds/:id} : Partial updates given fields of an existing tuYaCmd, field will ignore if it is null
     *
     * @param id the id of the tuYaCmd to save.
     * @param tuYaCmd the tuYaCmd to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tuYaCmd,
     * or with status {@code 400 (Bad Request)} if the tuYaCmd is not valid,
     * or with status {@code 404 (Not Found)} if the tuYaCmd is not found,
     * or with status {@code 500 (Internal Server Error)} if the tuYaCmd couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tu-ya-cmds/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TuYaCmd> partialUpdateTuYaCmd(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TuYaCmd tuYaCmd
    ) throws URISyntaxException {
        log.debug("REST request to partial update TuYaCmd partially : {}, {}", id, tuYaCmd);
        if (tuYaCmd.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tuYaCmd.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tuYaCmdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TuYaCmd> result = tuYaCmdService.partialUpdate(tuYaCmd);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tuYaCmd.getId().toString())
        );
    }

    /**
     * {@code GET  /tu-ya-cmds} : get all the tuYaCmds.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tuYaCmds in body.
     */
    @GetMapping("/tu-ya-cmds")
    public ResponseEntity<List<TuYaCmd>> getAllTuYaCmds(TuYaCmdCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TuYaCmds by criteria: {}", criteria);
        Page<TuYaCmd> page = tuYaCmdQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tu-ya-cmds/count} : count all the tuYaCmds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tu-ya-cmds/count")
    public ResponseEntity<Long> countTuYaCmds(TuYaCmdCriteria criteria) {
        log.debug("REST request to count TuYaCmds by criteria: {}", criteria);
        return ResponseEntity.ok().body(tuYaCmdQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tu-ya-cmds/:id} : get the "id" tuYaCmd.
     *
     * @param id the id of the tuYaCmd to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tuYaCmd, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tu-ya-cmds/{id}")
    public ResponseEntity<TuYaCmd> getTuYaCmd(@PathVariable Long id) {
        log.debug("REST request to get TuYaCmd : {}", id);
        Optional<TuYaCmd> tuYaCmd = tuYaCmdService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tuYaCmd);
    }

    /**
     * {@code DELETE  /tu-ya-cmds/:id} : delete the "id" tuYaCmd.
     *
     * @param id the id of the tuYaCmd to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tu-ya-cmds/{id}")
    public ResponseEntity<Void> deleteTuYaCmd(@PathVariable Long id) {
        log.debug("REST request to delete TuYaCmd : {}", id);
        tuYaCmdService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
