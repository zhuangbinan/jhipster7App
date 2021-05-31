package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CommunityImages;
import com.mycompany.myapp.repository.CommunityImagesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommunityImages}.
 */
@Service
@Transactional
public class CommunityImagesService {

    private final Logger log = LoggerFactory.getLogger(CommunityImagesService.class);

    private final CommunityImagesRepository communityImagesRepository;

    public CommunityImagesService(CommunityImagesRepository communityImagesRepository) {
        this.communityImagesRepository = communityImagesRepository;
    }

    /**
     * Save a communityImages.
     *
     * @param communityImages the entity to save.
     * @return the persisted entity.
     */
    public CommunityImages save(CommunityImages communityImages) {
        log.debug("Request to save CommunityImages : {}", communityImages);
        return communityImagesRepository.save(communityImages);
    }

    /**
     * Partially update a communityImages.
     *
     * @param communityImages the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommunityImages> partialUpdate(CommunityImages communityImages) {
        log.debug("Request to partially update CommunityImages : {}", communityImages);

        return communityImagesRepository
            .findById(communityImages.getId())
            .map(
                existingCommunityImages -> {
                    if (communityImages.getImgContent() != null) {
                        existingCommunityImages.setImgContent(communityImages.getImgContent());
                    }
                    if (communityImages.getImgContentContentType() != null) {
                        existingCommunityImages.setImgContentContentType(communityImages.getImgContentContentType());
                    }
                    if (communityImages.getImgTitle() != null) {
                        existingCommunityImages.setImgTitle(communityImages.getImgTitle());
                    }
                    if (communityImages.getImgDesc() != null) {
                        existingCommunityImages.setImgDesc(communityImages.getImgDesc());
                    }
                    if (communityImages.getOrderNum() != null) {
                        existingCommunityImages.setOrderNum(communityImages.getOrderNum());
                    }
                    if (communityImages.getLastModifyDate() != null) {
                        existingCommunityImages.setLastModifyDate(communityImages.getLastModifyDate());
                    }
                    if (communityImages.getLastModifyBy() != null) {
                        existingCommunityImages.setLastModifyBy(communityImages.getLastModifyBy());
                    }

                    return existingCommunityImages;
                }
            )
            .map(communityImagesRepository::save);
    }

    /**
     * Get all the communityImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunityImages> findAll(Pageable pageable) {
        log.debug("Request to get all CommunityImages");
        return communityImagesRepository.findAll(pageable);
    }

    /**
     * Get one communityImages by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommunityImages> findOne(Long id) {
        log.debug("Request to get CommunityImages : {}", id);
        return communityImagesRepository.findById(id);
    }

    /**
     * Delete the communityImages by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommunityImages : {}", id);
        communityImagesRepository.deleteById(id);
    }
}
