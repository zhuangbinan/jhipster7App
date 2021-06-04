package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorityService {

    private final Logger log = LoggerFactory.getLogger(AuthorityService.class);

    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority save(Authority authority){
        log.debug("Request to save Authority : {}", authority);
        return authorityRepository.save(authority);
    }

    public void delete(String name) {
        log.debug("Request to delete Authority : {}", name);
        authorityRepository.deleteById(name);
    }

    @Transactional(readOnly = true)
    public List<Authority> findAll() {
        log.debug("Request to get all Authority");
        return authorityRepository.findAll();
    }

    /**
     * 通过角色名(主键) 查找 一个 角色对象 , 如果有
     * @param name 角色名 (主键)
     * @return 是否查到
     */
    public boolean findOneByIdReturnBoolean(String name){
        return authorityRepository.findById(name).isPresent();
    }

    public Authority findOneById(String name){
        Optional<Authority> byId = authorityRepository.findById(name);
        return byId.orElse(null);
    }

}
