package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TuYaCmd;
import com.mycompany.myapp.repository.TuYaCmdRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TuYaCmd}.
 */
@Service
@Transactional
public class TuYaCmdService {

    private final Logger log = LoggerFactory.getLogger(TuYaCmdService.class);

    private final TuYaCmdRepository tuYaCmdRepository;

    public TuYaCmdService(TuYaCmdRepository tuYaCmdRepository) {
        this.tuYaCmdRepository = tuYaCmdRepository;
    }

    /**
     * Save a tuYaCmd.
     *
     * @param tuYaCmd the entity to save.
     * @return the persisted entity.
     */
    public TuYaCmd save(TuYaCmd tuYaCmd) {
        log.debug("Request to save TuYaCmd : {}", tuYaCmd);
        return tuYaCmdRepository.save(tuYaCmd);
    }

    /**
     * Partially update a tuYaCmd.
     *
     * @param tuYaCmd the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TuYaCmd> partialUpdate(TuYaCmd tuYaCmd) {
        log.debug("Request to partially update TuYaCmd : {}", tuYaCmd);

        return tuYaCmdRepository
            .findById(tuYaCmd.getId())
            .map(
                existingTuYaCmd -> {
                    if (tuYaCmd.getCmdName() != null) {
                        existingTuYaCmd.setCmdName(tuYaCmd.getCmdName());
                    }
                    if (tuYaCmd.getCmdCode() != null) {
                        existingTuYaCmd.setCmdCode(tuYaCmd.getCmdCode());
                    }
                    if (tuYaCmd.getValue() != null) {
                        existingTuYaCmd.setValue(tuYaCmd.getValue());
                    }
                    if (tuYaCmd.getCmdType() != null) {
                        existingTuYaCmd.setCmdType(tuYaCmd.getCmdType());
                    }
                    if (tuYaCmd.getCreateTime() != null) {
                        existingTuYaCmd.setCreateTime(tuYaCmd.getCreateTime());
                    }
                    if (tuYaCmd.getEnable() != null) {
                        existingTuYaCmd.setEnable(tuYaCmd.getEnable());
                    }

                    return existingTuYaCmd;
                }
            )
            .map(tuYaCmdRepository::save);
    }

    /**
     * Get all the tuYaCmds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TuYaCmd> findAll(Pageable pageable) {
        log.debug("Request to get all TuYaCmds");
        return tuYaCmdRepository.findAll(pageable);
    }

    /**
     * Get one tuYaCmd by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TuYaCmd> findOne(Long id) {
        log.debug("Request to get TuYaCmd : {}", id);
        return tuYaCmdRepository.findById(id);
    }

    /**
     * Delete the tuYaCmd by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TuYaCmd : {}", id);
        tuYaCmdRepository.deleteById(id);
    }
}
