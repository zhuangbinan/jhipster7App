package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Test01;
import com.mycompany.myapp.repository.Test01Repository;
import com.mycompany.myapp.service.dto.Test01DTO;
import com.mycompany.myapp.service.mapper.Test01Mapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Test01}.
 */
@Service
@Transactional
public class Test01Service {

    private final Logger log = LoggerFactory.getLogger(Test01Service.class);

    private final Test01Repository test01Repository;

    private final Test01Mapper test01Mapper;

    public Test01Service(Test01Repository test01Repository, Test01Mapper test01Mapper) {
        this.test01Repository = test01Repository;
        this.test01Mapper = test01Mapper;
    }

    /**
     * Save a test01.
     *
     * @param test01DTO the entity to save.
     * @return the persisted entity.
     */
    public Test01DTO save(Test01DTO test01DTO) {
        log.debug("Request to save Test01 : {}", test01DTO);
        Test01 test01 = test01Mapper.toEntity(test01DTO);
        test01 = test01Repository.save(test01);
        return test01Mapper.toDto(test01);
    }

    /**
     * Partially update a test01.
     *
     * @param test01DTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Test01DTO> partialUpdate(Test01DTO test01DTO) {
        log.debug("Request to partially update Test01 : {}", test01DTO);

        return test01Repository
            .findById(test01DTO.getId())
            .map(
                existingTest01 -> {
                    test01Mapper.partialUpdate(existingTest01, test01DTO);
                    return existingTest01;
                }
            )
            .map(test01Repository::save)
            .map(test01Mapper::toDto);
    }

    /**
     * Get all the test01s.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Test01DTO> findAll(Pageable pageable) {
        log.debug("Request to get all Test01s");
        return test01Repository.findAll(pageable).map(test01Mapper::toDto);
    }

    /**
     * Get one test01 by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Test01DTO> findOne(Long id) {
        log.debug("Request to get Test01 : {}", id);
        return test01Repository.findById(id).map(test01Mapper::toDto);
    }

    /**
     * Delete the test01 by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Test01 : {}", id);
        test01Repository.deleteById(id);
    }
}
