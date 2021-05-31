package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.HomelandStation;
import com.mycompany.myapp.repository.HomelandStationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HomelandStation}.
 */
@Service
@Transactional
public class HomelandStationService {

    private final Logger log = LoggerFactory.getLogger(HomelandStationService.class);

    private final HomelandStationRepository homelandStationRepository;

    public HomelandStationService(HomelandStationRepository homelandStationRepository) {
        this.homelandStationRepository = homelandStationRepository;
    }

    /**
     * Save a homelandStation.
     *
     * @param homelandStation the entity to save.
     * @return the persisted entity.
     */
    public HomelandStation save(HomelandStation homelandStation) {
        log.debug("Request to save HomelandStation : {}", homelandStation);
        return homelandStationRepository.save(homelandStation);
    }

    /**
     * Partially update a homelandStation.
     *
     * @param homelandStation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HomelandStation> partialUpdate(HomelandStation homelandStation) {
        log.debug("Request to partially update HomelandStation : {}", homelandStation);

        return homelandStationRepository
            .findById(homelandStation.getId())
            .map(
                existingHomelandStation -> {
                    if (homelandStation.getName() != null) {
                        existingHomelandStation.setName(homelandStation.getName());
                    }
                    if (homelandStation.getLongCode() != null) {
                        existingHomelandStation.setLongCode(homelandStation.getLongCode());
                    }
                    if (homelandStation.getAddress() != null) {
                        existingHomelandStation.setAddress(homelandStation.getAddress());
                    }
                    if (homelandStation.getLivingPopulation() != null) {
                        existingHomelandStation.setLivingPopulation(homelandStation.getLivingPopulation());
                    }
                    if (homelandStation.getBuildingCount() != null) {
                        existingHomelandStation.setBuildingCount(homelandStation.getBuildingCount());
                    }
                    if (homelandStation.getEntranceCount() != null) {
                        existingHomelandStation.setEntranceCount(homelandStation.getEntranceCount());
                    }
                    if (homelandStation.getLogo() != null) {
                        existingHomelandStation.setLogo(homelandStation.getLogo());
                    }
                    if (homelandStation.getLogoContentType() != null) {
                        existingHomelandStation.setLogoContentType(homelandStation.getLogoContentType());
                    }
                    if (homelandStation.getLongitude() != null) {
                        existingHomelandStation.setLongitude(homelandStation.getLongitude());
                    }
                    if (homelandStation.getLatitude() != null) {
                        existingHomelandStation.setLatitude(homelandStation.getLatitude());
                    }

                    return existingHomelandStation;
                }
            )
            .map(homelandStationRepository::save);
    }

    /**
     * Get all the homelandStations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HomelandStation> findAll(Pageable pageable) {
        log.debug("Request to get all HomelandStations");
        return homelandStationRepository.findAll(pageable);
    }

    /**
     * Get one homelandStation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HomelandStation> findOne(Long id) {
        log.debug("Request to get HomelandStation : {}", id);
        return homelandStationRepository.findById(id);
    }

    /**
     * Delete the homelandStation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HomelandStation : {}", id);
        homelandStationRepository.deleteById(id);
    }
}
