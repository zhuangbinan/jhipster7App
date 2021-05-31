package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WamoliFaceLibrary;
import com.mycompany.myapp.repository.WamoliFaceLibraryRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WamoliFaceLibrary}.
 */
@Service
@Transactional
public class WamoliFaceLibraryService {

    private final Logger log = LoggerFactory.getLogger(WamoliFaceLibraryService.class);

    private final WamoliFaceLibraryRepository wamoliFaceLibraryRepository;

    public WamoliFaceLibraryService(WamoliFaceLibraryRepository wamoliFaceLibraryRepository) {
        this.wamoliFaceLibraryRepository = wamoliFaceLibraryRepository;
    }

    /**
     * Save a wamoliFaceLibrary.
     *
     * @param wamoliFaceLibrary the entity to save.
     * @return the persisted entity.
     */
    public WamoliFaceLibrary save(WamoliFaceLibrary wamoliFaceLibrary) {
        log.debug("Request to save WamoliFaceLibrary : {}", wamoliFaceLibrary);
        return wamoliFaceLibraryRepository.save(wamoliFaceLibrary);
    }

    /**
     * Partially update a wamoliFaceLibrary.
     *
     * @param wamoliFaceLibrary the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WamoliFaceLibrary> partialUpdate(WamoliFaceLibrary wamoliFaceLibrary) {
        log.debug("Request to partially update WamoliFaceLibrary : {}", wamoliFaceLibrary);

        return wamoliFaceLibraryRepository
            .findById(wamoliFaceLibrary.getId())
            .map(
                existingWamoliFaceLibrary -> {
                    if (wamoliFaceLibrary.getContent() != null) {
                        existingWamoliFaceLibrary.setContent(wamoliFaceLibrary.getContent());
                    }

                    return existingWamoliFaceLibrary;
                }
            )
            .map(wamoliFaceLibraryRepository::save);
    }

    /**
     * Get all the wamoliFaceLibraries.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WamoliFaceLibrary> findAll() {
        log.debug("Request to get all WamoliFaceLibraries");
        return wamoliFaceLibraryRepository.findAll();
    }

    /**
     * Get one wamoliFaceLibrary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WamoliFaceLibrary> findOne(Long id) {
        log.debug("Request to get WamoliFaceLibrary : {}", id);
        return wamoliFaceLibraryRepository.findById(id);
    }

    /**
     * Delete the wamoliFaceLibrary by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WamoliFaceLibrary : {}", id);
        wamoliFaceLibraryRepository.deleteById(id);
    }
}
