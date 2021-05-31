package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TuYaDevice;
import com.mycompany.myapp.repository.TuYaDeviceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TuYaDevice}.
 */
@Service
@Transactional
public class TuYaDeviceService {

    private final Logger log = LoggerFactory.getLogger(TuYaDeviceService.class);

    private final TuYaDeviceRepository tuYaDeviceRepository;

    public TuYaDeviceService(TuYaDeviceRepository tuYaDeviceRepository) {
        this.tuYaDeviceRepository = tuYaDeviceRepository;
    }

    /**
     * Save a tuYaDevice.
     *
     * @param tuYaDevice the entity to save.
     * @return the persisted entity.
     */
    public TuYaDevice save(TuYaDevice tuYaDevice) {
        log.debug("Request to save TuYaDevice : {}", tuYaDevice);
        return tuYaDeviceRepository.save(tuYaDevice);
    }

    /**
     * Partially update a tuYaDevice.
     *
     * @param tuYaDevice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TuYaDevice> partialUpdate(TuYaDevice tuYaDevice) {
        log.debug("Request to partially update TuYaDevice : {}", tuYaDevice);

        return tuYaDeviceRepository
            .findById(tuYaDevice.getId())
            .map(
                existingTuYaDevice -> {
                    if (tuYaDevice.getDeviceName() != null) {
                        existingTuYaDevice.setDeviceName(tuYaDevice.getDeviceName());
                    }
                    if (tuYaDevice.getLongCode() != null) {
                        existingTuYaDevice.setLongCode(tuYaDevice.getLongCode());
                    }
                    if (tuYaDevice.getTyDeviceId() != null) {
                        existingTuYaDevice.setTyDeviceId(tuYaDevice.getTyDeviceId());
                    }
                    if (tuYaDevice.getDeviceType() != null) {
                        existingTuYaDevice.setDeviceType(tuYaDevice.getDeviceType());
                    }
                    if (tuYaDevice.getCmdCode() != null) {
                        existingTuYaDevice.setCmdCode(tuYaDevice.getCmdCode());
                    }
                    if (tuYaDevice.getCreateTime() != null) {
                        existingTuYaDevice.setCreateTime(tuYaDevice.getCreateTime());
                    }
                    if (tuYaDevice.getEnable() != null) {
                        existingTuYaDevice.setEnable(tuYaDevice.getEnable());
                    }

                    return existingTuYaDevice;
                }
            )
            .map(tuYaDeviceRepository::save);
    }

    /**
     * Get all the tuYaDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TuYaDevice> findAll(Pageable pageable) {
        log.debug("Request to get all TuYaDevices");
        return tuYaDeviceRepository.findAll(pageable);
    }

    /**
     * Get one tuYaDevice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TuYaDevice> findOne(Long id) {
        log.debug("Request to get TuYaDevice : {}", id);
        return tuYaDeviceRepository.findById(id);
    }

    /**
     * Delete the tuYaDevice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TuYaDevice : {}", id);
        tuYaDeviceRepository.deleteById(id);
    }
}
