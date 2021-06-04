package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.service.AuthorityService;
import com.mycompany.myapp.service.DataJdbcService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthorityResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);

    private static final String ENTITY_NAME = "authority";

    private final AuthorityService authorityService;

    private final DataJdbcService dataJdbcService;

    public AuthorityResource(AuthorityService authorityService, DataJdbcService dataJdbcService) {
        this.authorityService = authorityService;
        this.dataJdbcService = dataJdbcService;
    }

    @PostMapping("/authority")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority authority) {
        log.debug("REST request to save Authority : {}", authority);
        if (authority.getName() == null) {
            throw new BadRequestAlertException("参数有误", ENTITY_NAME, "未设置正确的参数值");
        }
        if (authorityService.findOneByIdReturnBoolean(authority.getName())){
            throw new BadRequestAlertException("参数有误", ENTITY_NAME, "这个角色已经存在了");
        }
        Authority save = authorityService.save(authority);
        return ResponseEntity.ok().body(save);
    }

    @PutMapping("/authority/{name}")
    public ResponseEntity<Authority> updateAuthority(@PathVariable(value = "name", required = false) final String name,@RequestBody Authority authority)
    {
        log.debug("REST request to update Authority : {}, {}", name , authority);
        if (name == null || authority.getName() == null) {
            throw new BadRequestAlertException("Invalid name", ENTITY_NAME, "name is null");
        }

        if (dataJdbcService.updateAuthorityById(name, authority.getName() ) == 1){
            return ResponseEntity.ok(authority);
        }
        throw new IllegalArgumentException("更新失败!");
    }

    @GetMapping("/authority")
    public ResponseEntity<List<Authority>> getAllAuthority(){
        List<Authority> all = authorityService.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/authority/{name}")
    public ResponseEntity<Authority> getAuthorityById(@PathVariable(name = "name") String name){
        Authority one = authorityService.findOneById(name);
        return ResponseEntity.ok(one);
    }

    @DeleteMapping("/authority/{name}")
    public ResponseEntity<Void> delAuthorityById(@PathVariable(name = "name") String name){
        authorityService.delete(name);
        return ResponseEntity.ok().body(null);
    }

}
