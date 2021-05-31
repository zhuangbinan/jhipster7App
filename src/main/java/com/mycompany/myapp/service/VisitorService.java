package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Visitor;
import com.mycompany.myapp.repository.VisitorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Visitor}.
 */
@Service
@Transactional
public class VisitorService {

    private final Logger log = LoggerFactory.getLogger(VisitorService.class);

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    /**
     * Save a visitor.
     *
     * @param visitor the entity to save.
     * @return the persisted entity.
     */
    public Visitor save(Visitor visitor) {
        log.debug("Request to save Visitor : {}", visitor);
        return visitorRepository.save(visitor);
    }

    /**
     * Partially update a visitor.
     *
     * @param visitor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Visitor> partialUpdate(Visitor visitor) {
        log.debug("Request to partially update Visitor : {}", visitor);

        return visitorRepository
            .findById(visitor.getId())
            .map(
                existingVisitor -> {
                    if (visitor.getName() != null) {
                        existingVisitor.setName(visitor.getName());
                    }
                    if (visitor.getPhoneNum() != null) {
                        existingVisitor.setPhoneNum(visitor.getPhoneNum());
                    }
                    if (visitor.getCarPlateNum() != null) {
                        existingVisitor.setCarPlateNum(visitor.getCarPlateNum());
                    }
                    if (visitor.getStartTime() != null) {
                        existingVisitor.setStartTime(visitor.getStartTime());
                    }
                    if (visitor.getEndTime() != null) {
                        existingVisitor.setEndTime(visitor.getEndTime());
                    }
                    if (visitor.getPasswd() != null) {
                        existingVisitor.setPasswd(visitor.getPasswd());
                    }
                    if (visitor.getFaceImage() != null) {
                        existingVisitor.setFaceImage(visitor.getFaceImage());
                    }
                    if (visitor.getFaceImageContentType() != null) {
                        existingVisitor.setFaceImageContentType(visitor.getFaceImageContentType());
                    }
                    if (visitor.getWhichEntrance() != null) {
                        existingVisitor.setWhichEntrance(visitor.getWhichEntrance());
                    }

                    return existingVisitor;
                }
            )
            .map(visitorRepository::save);
    }

    /**
     * Get all the visitors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Visitor> findAll(Pageable pageable) {
        log.debug("Request to get all Visitors");
        return visitorRepository.findAll(pageable);
    }

    /**
     * Get one visitor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Visitor> findOne(Long id) {
        log.debug("Request to get Visitor : {}", id);
        return visitorRepository.findById(id);
    }

    /**
     * Delete the visitor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Visitor : {}", id);
        visitorRepository.deleteById(id);
    }
}
