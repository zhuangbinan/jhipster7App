package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Buildings;
import com.mycompany.myapp.repository.BuildingsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Buildings}.
 */
@Service
@Transactional
public class BuildingsService {

    private final Logger log = LoggerFactory.getLogger(BuildingsService.class);

    private final BuildingsRepository buildingsRepository;

    public BuildingsService(BuildingsRepository buildingsRepository) {
        this.buildingsRepository = buildingsRepository;
    }

    /**
     * Save a buildings.
     *
     * @param buildings the entity to save.
     * @return the persisted entity.
     */
    public Buildings save(Buildings buildings) {
        log.debug("Request to save Buildings : {}", buildings);
        return buildingsRepository.save(buildings);
    }

    /**
     * Partially update a buildings.
     *
     * @param buildings the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Buildings> partialUpdate(Buildings buildings) {
        log.debug("Request to partially update Buildings : {}", buildings);

        return buildingsRepository
            .findById(buildings.getId())
            .map(
                existingBuildings -> {
                    if (buildings.getName() != null) {
                        existingBuildings.setName(buildings.getName());
                    }
                    if (buildings.getLongCode() != null) {
                        existingBuildings.setLongCode(buildings.getLongCode());
                    }
                    if (buildings.getFloorCount() != null) {
                        existingBuildings.setFloorCount(buildings.getFloorCount());
                    }
                    if (buildings.getUnites() != null) {
                        existingBuildings.setUnites(buildings.getUnites());
                    }
                    if (buildings.getLongitude() != null) {
                        existingBuildings.setLongitude(buildings.getLongitude());
                    }
                    if (buildings.getLatitude() != null) {
                        existingBuildings.setLatitude(buildings.getLatitude());
                    }
                    if (buildings.getEnable() != null) {
                        existingBuildings.setEnable(buildings.getEnable());
                    }

                    return existingBuildings;
                }
            )
            .map(buildingsRepository::save);
    }

    /**
     * Get all the buildings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Buildings> findAll(Pageable pageable) {
        log.debug("Request to get all Buildings");
        return buildingsRepository.findAll(pageable);
    }

    /**
     * Get one buildings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Buildings> findOne(Long id) {
        log.debug("Request to get Buildings : {}", id);
        return buildingsRepository.findById(id);
    }

    /**
     * Delete the buildings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Buildings : {}", id);
        buildingsRepository.deleteById(id);
    }
}
