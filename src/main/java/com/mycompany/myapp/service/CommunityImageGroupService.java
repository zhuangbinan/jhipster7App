package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CommunityImageGroup;
import com.mycompany.myapp.repository.CommunityImageGroupRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommunityImageGroup}.
 */
@Service
@Transactional
public class CommunityImageGroupService {

    private final Logger log = LoggerFactory.getLogger(CommunityImageGroupService.class);

    private final CommunityImageGroupRepository communityImageGroupRepository;

    public CommunityImageGroupService(CommunityImageGroupRepository communityImageGroupRepository) {
        this.communityImageGroupRepository = communityImageGroupRepository;
    }

    /**
     * Save a communityImageGroup.
     *
     * @param communityImageGroup the entity to save.
     * @return the persisted entity.
     */
    public CommunityImageGroup save(CommunityImageGroup communityImageGroup) {
        log.debug("Request to save CommunityImageGroup : {}", communityImageGroup);
        return communityImageGroupRepository.save(communityImageGroup);
    }

    /**
     * Partially update a communityImageGroup.
     *
     * @param communityImageGroup the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommunityImageGroup> partialUpdate(CommunityImageGroup communityImageGroup) {
        log.debug("Request to partially update CommunityImageGroup : {}", communityImageGroup);

        return communityImageGroupRepository
            .findById(communityImageGroup.getId())
            .map(
                existingCommunityImageGroup -> {
                    if (communityImageGroup.getImgGroupName() != null) {
                        existingCommunityImageGroup.setImgGroupName(communityImageGroup.getImgGroupName());
                    }
                    if (communityImageGroup.getOrderNum() != null) {
                        existingCommunityImageGroup.setOrderNum(communityImageGroup.getOrderNum());
                    }
                    if (communityImageGroup.getLastModifyDate() != null) {
                        existingCommunityImageGroup.setLastModifyDate(communityImageGroup.getLastModifyDate());
                    }
                    if (communityImageGroup.getLastModifyBy() != null) {
                        existingCommunityImageGroup.setLastModifyBy(communityImageGroup.getLastModifyBy());
                    }

                    return existingCommunityImageGroup;
                }
            )
            .map(communityImageGroupRepository::save);
    }

    /**
     * Get all the communityImageGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunityImageGroup> findAll(Pageable pageable) {
        log.debug("Request to get all CommunityImageGroups");
        return communityImageGroupRepository.findAll(pageable);
    }

    /**
     * Get one communityImageGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommunityImageGroup> findOne(Long id) {
        log.debug("Request to get CommunityImageGroup : {}", id);
        return communityImageGroupRepository.findById(id);
    }

    /**
     * Delete the communityImageGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommunityImageGroup : {}", id);
        communityImageGroupRepository.deleteById(id);
    }
}
