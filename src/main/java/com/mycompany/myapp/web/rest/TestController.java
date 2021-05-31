package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.dto.AdminUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

/**
 * 用来测试的
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

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


}
