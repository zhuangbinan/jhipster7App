package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CompanyDept;
import com.mycompany.myapp.domain.CompanyUser;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.CompanyUserService;
import com.mycompany.myapp.service.DataJdbcService;
import com.mycompany.myapp.service.dto.AdminUserDTO;
import com.mycompany.myapp.service.dto.CompanyUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 用来测试的
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private DataJdbcService dataJdbcService;

    @Autowired
    private CompanyUserService companyUserService;

    @PostMapping("/t3")
    public void t3(@Valid @RequestBody CompanyUserDTO dto){
        CompanyUser add = companyUserService.add(dto);
        System.out.println(dto);
        System.out.println(add);
    }

    @GetMapping("/testRole")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEST + "\")")
    public ResponseEntity<User> createUser()  {
        log.error("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        return null;
    }

    @GetMapping("/t2")
    public void t2(){
        log.error(".... t2 来了 ....");
    }

    @GetMapping("/t3/{id}")
    public void t3(@PathVariable(value = "id", required = false) Long id){
        List<CompanyDept> companyDepts = dataJdbcService.selectChildrenDeptById(id);

    }



}
