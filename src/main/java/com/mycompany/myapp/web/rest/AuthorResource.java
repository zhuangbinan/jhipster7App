package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Author;
import com.mycompany.myapp.repository.AuthorRepository;
import com.mycompany.myapp.service.AuthorQueryService;
import com.mycompany.myapp.service.AuthorService;
import com.mycompany.myapp.service.criteria.AuthorCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Author}.
 */
@RestController
@RequestMapping("/api")
public class AuthorResource {

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    private static final String ENTITY_NAME = "author";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorService authorService;

    private final AuthorRepository authorRepository;

    private final AuthorQueryService authorQueryService;

    public AuthorResource(AuthorService authorService, AuthorRepository authorRepository, AuthorQueryService authorQueryService) {
        this.authorService = authorService;
        this.authorRepository = authorRepository;
        this.authorQueryService = authorQueryService;
    }

    /**
     * {@code POST  /authors} : Create a new author.
     *
     * @param author the author to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new author, or with status {@code 400 (Bad Request)} if the author has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) throws URISyntaxException {
        log.debug("REST request to save Author : {}", author);
        if (author.getId() != null) {
            throw new BadRequestAlertException("A new author cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Author result = authorService.save(author);
        return ResponseEntity
            .created(new URI("/api/authors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /authors/:id} : Updates an existing author.
     *
     * @param id the id of the author to save.
     * @param author the author to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated author,
     * or with status {@code 400 (Bad Request)} if the author is not valid,
     * or with status {@code 500 (Internal Server Error)} if the author couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable(value = "id", required = false) final Long id, @RequestBody Author author)
        throws URISyntaxException {
        log.debug("REST request to update Author : {}, {}", id, author);
        if (author.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, author.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Author result = authorService.save(author);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, author.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /authors/:id} : Partial updates given fields of an existing author, field will ignore if it is null
     *
     * @param id the id of the author to save.
     * @param author the author to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated author,
     * or with status {@code 400 (Bad Request)} if the author is not valid,
     * or with status {@code 404 (Not Found)} if the author is not found,
     * or with status {@code 500 (Internal Server Error)} if the author couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/authors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Author> partialUpdateAuthor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Author author
    ) throws URISyntaxException {
        log.debug("REST request to partial update Author partially : {}, {}", id, author);
        if (author.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, author.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Author> result = authorService.partialUpdate(author);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, author.getId().toString())
        );
    }

    /**
     * {@code GET  /authors} : get all the authors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authors in body.
     */
    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAllAuthors(AuthorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Authors by criteria: {}", criteria);
        Page<Author> page = authorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authors/count} : count all the authors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/authors/count")
    public ResponseEntity<Long> countAuthors(AuthorCriteria criteria) {
        log.debug("REST request to count Authors by criteria: {}", criteria);
        return ResponseEntity.ok().body(authorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /authors/:id} : get the "id" author.
     *
     * @param id the id of the author to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the author, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/authors/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable Long id) {
        log.debug("REST request to get Author : {}", id);
        Optional<Author> author = authorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(author);
    }

    /**
     * {@code DELETE  /authors/:id} : delete the "id" author.
     *
     * @param id the id of the author to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.debug("REST request to delete Author : {}", id);
        authorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
