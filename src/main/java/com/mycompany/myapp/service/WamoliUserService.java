package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.WamoliUser;
import com.mycompany.myapp.repository.WamoliUserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WamoliUser}.
 */
@Service
@Transactional
public class WamoliUserService {

    private final Logger log = LoggerFactory.getLogger(WamoliUserService.class);

    private final WamoliUserRepository wamoliUserRepository;

    public WamoliUserService(WamoliUserRepository wamoliUserRepository) {
        this.wamoliUserRepository = wamoliUserRepository;
    }

    /**
     * Save a wamoliUser.
     *
     * @param wamoliUser the entity to save.
     * @return the persisted entity.
     */
    public WamoliUser save(WamoliUser wamoliUser) {
        log.debug("Request to save WamoliUser : {}", wamoliUser);
        return wamoliUserRepository.save(wamoliUser);
    }

    /**
     * Partially update a wamoliUser.
     *
     * @param wamoliUser the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WamoliUser> partialUpdate(WamoliUser wamoliUser) {
        log.debug("Request to partially update WamoliUser : {}", wamoliUser);

        return wamoliUserRepository
            .findById(wamoliUser.getId())
            .map(
                existingWamoliUser -> {
                    if (wamoliUser.getUserName() != null) {
                        existingWamoliUser.setUserName(wamoliUser.getUserName());
                    }
                    if (wamoliUser.getGender() != null) {
                        existingWamoliUser.setGender(wamoliUser.getGender());
                    }
                    if (wamoliUser.getPhoneNum() != null) {
                        existingWamoliUser.setPhoneNum(wamoliUser.getPhoneNum());
                    }
                    if (wamoliUser.getEmail() != null) {
                        existingWamoliUser.setEmail(wamoliUser.getEmail());
                    }
                    if (wamoliUser.getUnitAddr() != null) {
                        existingWamoliUser.setUnitAddr(wamoliUser.getUnitAddr());
                    }
                    if (wamoliUser.getRoomAddr() != null) {
                        existingWamoliUser.setRoomAddr(wamoliUser.getRoomAddr());
                    }
                    if (wamoliUser.getIdCardNum() != null) {
                        existingWamoliUser.setIdCardNum(wamoliUser.getIdCardNum());
                    }
                    if (wamoliUser.getIdCardType() != null) {
                        existingWamoliUser.setIdCardType(wamoliUser.getIdCardType());
                    }
                    if (wamoliUser.getUserType() != null) {
                        existingWamoliUser.setUserType(wamoliUser.getUserType());
                    }
                    if (wamoliUser.getEnable() != null) {
                        existingWamoliUser.setEnable(wamoliUser.getEnable());
                    }
                    if (wamoliUser.getIsManager() != null) {
                        existingWamoliUser.setIsManager(wamoliUser.getIsManager());
                    }
                    if (wamoliUser.getLastModifiedBy() != null) {
                        existingWamoliUser.setLastModifiedBy(wamoliUser.getLastModifiedBy());
                    }
                    if (wamoliUser.getLastModifiedDate() != null) {
                        existingWamoliUser.setLastModifiedDate(wamoliUser.getLastModifiedDate());
                    }

                    return existingWamoliUser;
                }
            )
            .map(wamoliUserRepository::save);
    }

    /**
     * Get all the wamoliUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WamoliUser> findAll(Pageable pageable) {
        log.debug("Request to get all WamoliUsers");
        return wamoliUserRepository.findAll(pageable);
    }

    /**
     * Get all the wamoliUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WamoliUser> findAllWithEagerRelationships(Pageable pageable) {
        return wamoliUserRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one wamoliUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WamoliUser> findOne(Long id) {
        log.debug("Request to get WamoliUser : {}", id);
        return wamoliUserRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the wamoliUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WamoliUser : {}", id);
        wamoliUserRepository.deleteById(id);
    }

    public Optional<WamoliUser> bindJhiUser(WamoliUser wamoliUser) {
        return wamoliUserRepository
            .findOneByEmailAndPhoneNumAndUserIsNotNullAndEnableIsFalse(
                wamoliUser.getEmail(),
                wamoliUser.getPhoneNum()
            )
            .map(newWamoliUser -> {
                newWamoliUser.setEmail(wamoliUser.getEmail());
                newWamoliUser.setPhoneNum(wamoliUser.getPhoneNum());
                newWamoliUser.setEnable(wamoliUser.getEnable());
                newWamoliUser.setIsManager(wamoliUser.getIsManager());
                newWamoliUser.setUser(wamoliUser.getUser());
                return newWamoliUser;
            });
    }
}
