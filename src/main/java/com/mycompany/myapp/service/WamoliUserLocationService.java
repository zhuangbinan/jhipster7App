package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WamoliUserLocation;
import com.mycompany.myapp.repository.WamoliUserLocationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WamoliUserLocation}.
 */
@Service
@Transactional
public class WamoliUserLocationService {

    private final Logger log = LoggerFactory.getLogger(WamoliUserLocationService.class);

    private final WamoliUserLocationRepository wamoliUserLocationRepository;

    public WamoliUserLocationService(WamoliUserLocationRepository wamoliUserLocationRepository) {
        this.wamoliUserLocationRepository = wamoliUserLocationRepository;
    }

    /**
     * Save a wamoliUserLocation.
     *
     * @param wamoliUserLocation the entity to save.
     * @return the persisted entity.
     */
    public WamoliUserLocation save(WamoliUserLocation wamoliUserLocation) {
        log.debug("Request to save WamoliUserLocation : {}", wamoliUserLocation);
        return wamoliUserLocationRepository.save(wamoliUserLocation);
    }

    /**
     * Partially update a wamoliUserLocation.
     *
     * @param wamoliUserLocation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WamoliUserLocation> partialUpdate(WamoliUserLocation wamoliUserLocation) {
        log.debug("Request to partially update WamoliUserLocation : {}", wamoliUserLocation);

        return wamoliUserLocationRepository
            .findById(wamoliUserLocation.getId())
            .map(
                existingWamoliUserLocation -> {
                    if (wamoliUserLocation.getState() != null) {
                        existingWamoliUserLocation.setState(wamoliUserLocation.getState());
                    }
                    if (wamoliUserLocation.getCardNum() != null) {
                        existingWamoliUserLocation.setCardNum(wamoliUserLocation.getCardNum());
                    }
                    if (wamoliUserLocation.getExpireTime() != null) {
                        existingWamoliUserLocation.setExpireTime(wamoliUserLocation.getExpireTime());
                    }
                    if (wamoliUserLocation.getDelayTime() != null) {
                        existingWamoliUserLocation.setDelayTime(wamoliUserLocation.getDelayTime());
                    }
                    if (wamoliUserLocation.getLastModifiedBy() != null) {
                        existingWamoliUserLocation.setLastModifiedBy(wamoliUserLocation.getLastModifiedBy());
                    }
                    if (wamoliUserLocation.getLastModifiedDate() != null) {
                        existingWamoliUserLocation.setLastModifiedDate(wamoliUserLocation.getLastModifiedDate());
                    }

                    return existingWamoliUserLocation;
                }
            )
            .map(wamoliUserLocationRepository::save);
    }

    /**
     * Get all the wamoliUserLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WamoliUserLocation> findAll(Pageable pageable) {
        log.debug("Request to get all WamoliUserLocations");
        return wamoliUserLocationRepository.findAll(pageable);
    }

    /**
     * Get one wamoliUserLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WamoliUserLocation> findOne(Long id) {
        log.debug("Request to get WamoliUserLocation : {}", id);
        return wamoliUserLocationRepository.findById(id);
    }

    /**
     * Delete the wamoliUserLocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WamoliUserLocation : {}", id);
        wamoliUserLocationRepository.deleteById(id);
    }
}
