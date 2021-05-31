package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Community;
import com.mycompany.myapp.repository.CommunityRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Community}.
 */
@Service
@Transactional
public class CommunityService {

    private final Logger log = LoggerFactory.getLogger(CommunityService.class);

    private final CommunityRepository communityRepository;

    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    /**
     * Save a community.
     *
     * @param community the entity to save.
     * @return the persisted entity.
     */
    public Community save(Community community) {
        log.debug("Request to save Community : {}", community);
        return communityRepository.save(community);
    }

    /**
     * Partially update a community.
     *
     * @param community the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Community> partialUpdate(Community community) {
        log.debug("Request to partially update Community : {}", community);

        return communityRepository
            .findById(community.getId())
            .map(
                existingCommunity -> {
                    if (community.getCname() != null) {
                        existingCommunity.setCname(community.getCname());
                    }
                    if (community.getManagerName() != null) {
                        existingCommunity.setManagerName(community.getManagerName());
                    }
                    if (community.getAddress() != null) {
                        existingCommunity.setAddress(community.getAddress());
                    }
                    if (community.getTel() != null) {
                        existingCommunity.setTel(community.getTel());
                    }
                    if (community.getEmail() != null) {
                        existingCommunity.setEmail(community.getEmail());
                    }
                    if (community.getOfficeHours() != null) {
                        existingCommunity.setOfficeHours(community.getOfficeHours());
                    }
                    if (community.getDescription() != null) {
                        existingCommunity.setDescription(community.getDescription());
                    }
                    if (community.getSource() != null) {
                        existingCommunity.setSource(community.getSource());
                    }
                    if (community.getParentId() != null) {
                        existingCommunity.setParentId(community.getParentId());
                    }
                    if (community.getAncestors() != null) {
                        existingCommunity.setAncestors(community.getAncestors());
                    }
                    if (community.getLongCode() != null) {
                        existingCommunity.setLongCode(community.getLongCode());
                    }
                    if (community.getEnable() != null) {
                        existingCommunity.setEnable(community.getEnable());
                    }
                    if (community.getDelFlag() != null) {
                        existingCommunity.setDelFlag(community.getDelFlag());
                    }
                    if (community.getOrderNum() != null) {
                        existingCommunity.setOrderNum(community.getOrderNum());
                    }
                    if (community.getLastModifyDate() != null) {
                        existingCommunity.setLastModifyDate(community.getLastModifyDate());
                    }
                    if (community.getLastModifyBy() != null) {
                        existingCommunity.setLastModifyBy(community.getLastModifyBy());
                    }

                    return existingCommunity;
                }
            )
            .map(communityRepository::save);
    }

    /**
     * Get all the communities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Community> findAll(Pageable pageable) {
        log.debug("Request to get all Communities");
        return communityRepository.findAll(pageable);
    }

    /**
     * Get one community by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Community> findOne(Long id) {
        log.debug("Request to get Community : {}", id);
        return communityRepository.findById(id);
    }

    /**
     * Delete the community by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Community : {}", id);
        communityRepository.deleteById(id);
    }
}
