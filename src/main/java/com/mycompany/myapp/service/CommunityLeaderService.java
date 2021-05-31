package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CommunityLeader;
import com.mycompany.myapp.repository.CommunityLeaderRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommunityLeader}.
 */
@Service
@Transactional
public class CommunityLeaderService {

    private final Logger log = LoggerFactory.getLogger(CommunityLeaderService.class);

    private final CommunityLeaderRepository communityLeaderRepository;

    public CommunityLeaderService(CommunityLeaderRepository communityLeaderRepository) {
        this.communityLeaderRepository = communityLeaderRepository;
    }

    /**
     * Save a communityLeader.
     *
     * @param communityLeader the entity to save.
     * @return the persisted entity.
     */
    public CommunityLeader save(CommunityLeader communityLeader) {
        log.debug("Request to save CommunityLeader : {}", communityLeader);
        return communityLeaderRepository.save(communityLeader);
    }

    /**
     * Partially update a communityLeader.
     *
     * @param communityLeader the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommunityLeader> partialUpdate(CommunityLeader communityLeader) {
        log.debug("Request to partially update CommunityLeader : {}", communityLeader);

        return communityLeaderRepository
            .findById(communityLeader.getId())
            .map(
                existingCommunityLeader -> {
                    if (communityLeader.getAvatar() != null) {
                        existingCommunityLeader.setAvatar(communityLeader.getAvatar());
                    }
                    if (communityLeader.getAvatarContentType() != null) {
                        existingCommunityLeader.setAvatarContentType(communityLeader.getAvatarContentType());
                    }
                    if (communityLeader.getRealName() != null) {
                        existingCommunityLeader.setRealName(communityLeader.getRealName());
                    }
                    if (communityLeader.getTel() != null) {
                        existingCommunityLeader.setTel(communityLeader.getTel());
                    }
                    if (communityLeader.getJobTitle() != null) {
                        existingCommunityLeader.setJobTitle(communityLeader.getJobTitle());
                    }
                    if (communityLeader.getJobDesc() != null) {
                        existingCommunityLeader.setJobDesc(communityLeader.getJobDesc());
                    }
                    if (communityLeader.getJobCareerDesc() != null) {
                        existingCommunityLeader.setJobCareerDesc(communityLeader.getJobCareerDesc());
                    }
                    if (communityLeader.getEnable() != null) {
                        existingCommunityLeader.setEnable(communityLeader.getEnable());
                    }
                    if (communityLeader.getDelFlag() != null) {
                        existingCommunityLeader.setDelFlag(communityLeader.getDelFlag());
                    }
                    if (communityLeader.getOrderNum() != null) {
                        existingCommunityLeader.setOrderNum(communityLeader.getOrderNum());
                    }
                    if (communityLeader.getLastModifyDate() != null) {
                        existingCommunityLeader.setLastModifyDate(communityLeader.getLastModifyDate());
                    }
                    if (communityLeader.getLastModifyBy() != null) {
                        existingCommunityLeader.setLastModifyBy(communityLeader.getLastModifyBy());
                    }

                    return existingCommunityLeader;
                }
            )
            .map(communityLeaderRepository::save);
    }

    /**
     * Get all the communityLeaders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunityLeader> findAll(Pageable pageable) {
        log.debug("Request to get all CommunityLeaders");
        return communityLeaderRepository.findAll(pageable);
    }

    /**
     * Get one communityLeader by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommunityLeader> findOne(Long id) {
        log.debug("Request to get CommunityLeader : {}", id);
        return communityLeaderRepository.findById(id);
    }

    /**
     * Delete the communityLeader by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommunityLeader : {}", id);
        communityLeaderRepository.deleteById(id);
    }
}
