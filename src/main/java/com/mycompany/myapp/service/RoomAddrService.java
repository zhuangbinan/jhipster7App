package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.RoomAddr;
import com.mycompany.myapp.repository.RoomAddrRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RoomAddr}.
 */
@Service
@Transactional
public class RoomAddrService {

    private final Logger log = LoggerFactory.getLogger(RoomAddrService.class);

    private final RoomAddrRepository roomAddrRepository;

    public RoomAddrService(RoomAddrRepository roomAddrRepository) {
        this.roomAddrRepository = roomAddrRepository;
    }

    /**
     * Save a roomAddr.
     *
     * @param roomAddr the entity to save.
     * @return the persisted entity.
     */
    public RoomAddr save(RoomAddr roomAddr) {
        log.debug("Request to save RoomAddr : {}", roomAddr);
        return roomAddrRepository.save(roomAddr);
    }

    /**
     * Partially update a roomAddr.
     *
     * @param roomAddr the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoomAddr> partialUpdate(RoomAddr roomAddr) {
        log.debug("Request to partially update RoomAddr : {}", roomAddr);

        return roomAddrRepository
            .findById(roomAddr.getId())
            .map(
                existingRoomAddr -> {
                    if (roomAddr.getRoomNum() != null) {
                        existingRoomAddr.setRoomNum(roomAddr.getRoomNum());
                    }
                    if (roomAddr.getUnit() != null) {
                        existingRoomAddr.setUnit(roomAddr.getUnit());
                    }
                    if (roomAddr.getRoomType() != null) {
                        existingRoomAddr.setRoomType(roomAddr.getRoomType());
                    }
                    if (roomAddr.getRoomArea() != null) {
                        existingRoomAddr.setRoomArea(roomAddr.getRoomArea());
                    }
                    if (roomAddr.getUsed() != null) {
                        existingRoomAddr.setUsed(roomAddr.getUsed());
                    }
                    if (roomAddr.getAutoControl() != null) {
                        existingRoomAddr.setAutoControl(roomAddr.getAutoControl());
                    }
                    if (roomAddr.getLongCode() != null) {
                        existingRoomAddr.setLongCode(roomAddr.getLongCode());
                    }

                    return existingRoomAddr;
                }
            )
            .map(roomAddrRepository::save);
    }

    /**
     * Get all the roomAddrs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RoomAddr> findAll(Pageable pageable) {
        log.debug("Request to get all RoomAddrs");
        return roomAddrRepository.findAll(pageable);
    }

    /**
     * Get one roomAddr by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoomAddr> findOne(Long id) {
        log.debug("Request to get RoomAddr : {}", id);
        return roomAddrRepository.findById(id);
    }

    /**
     * Delete the roomAddr by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomAddr : {}", id);
        roomAddrRepository.deleteById(id);
    }
}
