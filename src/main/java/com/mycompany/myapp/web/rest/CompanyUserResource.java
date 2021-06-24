package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CompanyUser;
import com.mycompany.myapp.repository.CompanyUserRepository;
import com.mycompany.myapp.service.CompanyUserService;
import com.mycompany.myapp.service.DataJdbcService;
import com.mycompany.myapp.service.dto.CompanyUserDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CompanyUser}.
 */
@RestController
@RequestMapping("/api")
public class CompanyUserResource {

    private final Logger log = LoggerFactory.getLogger(CompanyUserResource.class);

    private static final String ENTITY_NAME = "companyUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyUserService companyUserService;

    private final CompanyUserRepository companyUserRepository;

    private final DataJdbcService dataJdbcService;

    public CompanyUserResource(CompanyUserService companyUserService, CompanyUserRepository companyUserRepository, DataJdbcService dataJdbcService) {
        this.companyUserService = companyUserService;
        this.companyUserRepository = companyUserRepository;
        this.dataJdbcService = dataJdbcService;
    }

    @PostMapping("/company-users/add")
    public ResponseEntity<CompanyUser> add(@Valid @RequestBody CompanyUserDTO dto){
        if (dto.getId() != null) {
            throw new IllegalArgumentException("新增时id不为null");
        }
        return ResponseEntity.ok(companyUserService.add(dto));
    }

    /**
     * {@code POST  /company-users} : Create a new companyUser.
     *
     * @param companyUser the companyUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyUser, or with status {@code 400 (Bad Request)} if the companyUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-users")
    public ResponseEntity<CompanyUser> createCompanyUser(@RequestBody CompanyUser companyUser) throws URISyntaxException {
        log.debug("REST request to save CompanyUser : {}", companyUser);
        if (companyUser.getId() != null) {
            throw new BadRequestAlertException("A new companyUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyUser result = companyUserService.save(companyUser);
        return ResponseEntity
            .created(new URI("/api/company-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-users/:id} : Updates an existing companyUser.
     *
     * @param id the id of the companyUser to save.
     * @param companyUser the companyUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyUser,
     * or with status {@code 400 (Bad Request)} if the companyUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-users/{id}")
    public ResponseEntity<CompanyUser> updateCompanyUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyUserDTO companyUser
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyUser : {}, {}", id, companyUser);
        if (companyUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        CompanyUser result = companyUserService.update(id,companyUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-users/:id} : Partial updates given fields of an existing companyUser, field will ignore if it is null
     *
     * @param id the id of the companyUser to save.
     * @param companyUser the companyUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyUser,
     * or with status {@code 400 (Bad Request)} if the companyUser is not valid,
     * or with status {@code 404 (Not Found)} if the companyUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/company-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CompanyUser> partialUpdateCompanyUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyUser companyUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyUser partially : {}, {}", id, companyUser);
        if (companyUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyUser> result = companyUserService.partialUpdate(companyUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyUser.getId().toString())
        );
    }

    /**
     * {@code GET  /company-users} : get all the companyUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyUsers in body.
     */
    @GetMapping("/company-users")
    public ResponseEntity<List<CompanyUser>> getAllCompanyUsers(Pageable pageable) {
        log.debug("REST request to get a page of CompanyUsers");
        Page<CompanyUser> page = companyUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/company-users/withDelFlag")
    public ResponseEntity<List<CompanyUser>> getAllCompanyUsersWithDelFlag(Pageable pageable) {
        log.debug("REST request to getAllCompanyUsersWithDelFlag");
        //写自己的分页 withDelFlag 查询
        List<CompanyUser> all = companyUserService.findAllWithDelFlag(pageable);
        Page page = new PageImpl(all,pageable,all.size());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * 在物业用户管理页面,点击某个部门时,
     * @param deptId 点击的部门ID
     * @param pageable 分页参数
     * @return 该部门ID下面的所有员工信息并分页
     */
    @GetMapping("/company-users/findCompanyUserByDeptId")
    public ResponseEntity<List<CompanyUser>> findCompanyUserByDeptId(Long deptId ,Pageable pageable) {
        log.debug("按部门查询,部门id: {},分页{}",deptId,pageable);
        List<CompanyUser> companyUserByDeptId = dataJdbcService.findCompanyUserByDeptId(deptId, pageable.getPageNumber(), pageable.getPageSize());
        Page page = new PageImpl(companyUserByDeptId,pageable,companyUserByDeptId.size());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /company-users/:id} : get the "id" companyUser.
     *
     * @param id the id of the companyUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-users/{id}")
    public ResponseEntity<CompanyUser> getCompanyUser(@PathVariable Long id) {
        log.debug("REST request to get CompanyUser : {}", id);
        Optional<CompanyUser> companyUser = companyUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyUser);
    }

    /// 保留自动生成的物理删除方法
    /*
    @DeleteMapping("/company-users/{id}")
    public ResponseEntity<Void> deleteCompanyUser(@PathVariable Long id) {
        log.debug("REST request to delete CompanyUser : {}", id);
        companyUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }*/

    /**
     * 逻辑删除
     * @param ids ids
     * @return void
     */
    @DeleteMapping("/company-users/{ids}")
    public ResponseEntity<Void> logicDeleteCompanyUser(@PathVariable Long[] ids) {
        log.debug("REST request to 逻辑删除 CompanyUser : {}", ids);
        companyUserService.logicDelete(ids);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, ids.toString()))
            .build();
    }
}
