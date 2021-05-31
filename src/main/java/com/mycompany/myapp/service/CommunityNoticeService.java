package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CommunityNotice;
import com.mycompany.myapp.repository.CommunityNoticeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommunityNotice}.
 */
@Service
@Transactional
public class CommunityNoticeService {

    private final Logger log = LoggerFactory.getLogger(CommunityNoticeService.class);

    private final CommunityNoticeRepository communityNoticeRepository;

    public CommunityNoticeService(CommunityNoticeRepository communityNoticeRepository) {
        this.communityNoticeRepository = communityNoticeRepository;
    }

    /**
     * Save a communityNotice.
     *
     * @param communityNotice the entity to save.
     * @return the persisted entity.
     */
    public CommunityNotice save(CommunityNotice communityNotice) {
        log.debug("Request to save CommunityNotice : {}", communityNotice);
        return communityNoticeRepository.save(communityNotice);
    }

    /**
     * Partially update a communityNotice.
     *
     * @param communityNotice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommunityNotice> partialUpdate(CommunityNotice communityNotice) {
        log.debug("Request to partially update CommunityNotice : {}", communityNotice);

        return communityNoticeRepository
            .findById(communityNotice.getId())
            .map(
                existingCommunityNotice -> {
                    if (communityNotice.getTitle() != null) {
                        existingCommunityNotice.setTitle(communityNotice.getTitle());
                    }
                    if (communityNotice.getImg1() != null) {
                        existingCommunityNotice.setImg1(communityNotice.getImg1());
                    }
                    if (communityNotice.getImg1ContentType() != null) {
                        existingCommunityNotice.setImg1ContentType(communityNotice.getImg1ContentType());
                    }
                    if (communityNotice.getImg1Title() != null) {
                        existingCommunityNotice.setImg1Title(communityNotice.getImg1Title());
                    }
                    if (communityNotice.getContent1() != null) {
                        existingCommunityNotice.setContent1(communityNotice.getContent1());
                    }
                    if (communityNotice.getImg2() != null) {
                        existingCommunityNotice.setImg2(communityNotice.getImg2());
                    }
                    if (communityNotice.getImg2ContentType() != null) {
                        existingCommunityNotice.setImg2ContentType(communityNotice.getImg2ContentType());
                    }
                    if (communityNotice.getImg2Title() != null) {
                        existingCommunityNotice.setImg2Title(communityNotice.getImg2Title());
                    }
                    if (communityNotice.getContent2() != null) {
                        existingCommunityNotice.setContent2(communityNotice.getContent2());
                    }
                    if (communityNotice.getImg3() != null) {
                        existingCommunityNotice.setImg3(communityNotice.getImg3());
                    }
                    if (communityNotice.getImg3ContentType() != null) {
                        existingCommunityNotice.setImg3ContentType(communityNotice.getImg3ContentType());
                    }
                    if (communityNotice.getImg3Title() != null) {
                        existingCommunityNotice.setImg3Title(communityNotice.getImg3Title());
                    }
                    if (communityNotice.getContent3() != null) {
                        existingCommunityNotice.setContent3(communityNotice.getContent3());
                    }
                    if (communityNotice.getImg4() != null) {
                        existingCommunityNotice.setImg4(communityNotice.getImg4());
                    }
                    if (communityNotice.getImg4ContentType() != null) {
                        existingCommunityNotice.setImg4ContentType(communityNotice.getImg4ContentType());
                    }
                    if (communityNotice.getImg4Title() != null) {
                        existingCommunityNotice.setImg4Title(communityNotice.getImg4Title());
                    }
                    if (communityNotice.getContent4() != null) {
                        existingCommunityNotice.setContent4(communityNotice.getContent4());
                    }
                    if (communityNotice.getImg5() != null) {
                        existingCommunityNotice.setImg5(communityNotice.getImg5());
                    }
                    if (communityNotice.getImg5ContentType() != null) {
                        existingCommunityNotice.setImg5ContentType(communityNotice.getImg5ContentType());
                    }
                    if (communityNotice.getImg5Title() != null) {
                        existingCommunityNotice.setImg5Title(communityNotice.getImg5Title());
                    }
                    if (communityNotice.getContent5() != null) {
                        existingCommunityNotice.setContent5(communityNotice.getContent5());
                    }
                    if (communityNotice.getTail() != null) {
                        existingCommunityNotice.setTail(communityNotice.getTail());
                    }
                    if (communityNotice.getEnable() != null) {
                        existingCommunityNotice.setEnable(communityNotice.getEnable());
                    }
                    if (communityNotice.getDelFlag() != null) {
                        existingCommunityNotice.setDelFlag(communityNotice.getDelFlag());
                    }
                    if (communityNotice.getOrderNum() != null) {
                        existingCommunityNotice.setOrderNum(communityNotice.getOrderNum());
                    }
                    if (communityNotice.getLastModifyDate() != null) {
                        existingCommunityNotice.setLastModifyDate(communityNotice.getLastModifyDate());
                    }
                    if (communityNotice.getLastModifyBy() != null) {
                        existingCommunityNotice.setLastModifyBy(communityNotice.getLastModifyBy());
                    }

                    return existingCommunityNotice;
                }
            )
            .map(communityNoticeRepository::save);
    }

    /**
     * Get all the communityNotices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunityNotice> findAll(Pageable pageable) {
        log.debug("Request to get all CommunityNotices");
        return communityNoticeRepository.findAll(pageable);
    }

    /**
     * Get one communityNotice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommunityNotice> findOne(Long id) {
        log.debug("Request to get CommunityNotice : {}", id);
        return communityNoticeRepository.findById(id);
    }

    /**
     * Delete the communityNotice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommunityNotice : {}", id);
        communityNoticeRepository.deleteById(id);
    }
}
