package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CompanyDept;
import com.mycompany.myapp.repository.CompanyDeptRepository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.mycompany.myapp.repository.DataJdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompanyDept}.
 */
@Service
@Transactional
public class CompanyDeptService {

    private final Logger log = LoggerFactory.getLogger(CompanyDeptService.class);

    private final CompanyDeptRepository companyDeptRepository;

    private final DataJdbcRepository dataJdbcRepository;

    public CompanyDeptService(CompanyDeptRepository companyDeptRepository, DataJdbcRepository dataJdbcRepository) {
        this.companyDeptRepository = companyDeptRepository;
        this.dataJdbcRepository = dataJdbcRepository;
    }

    /**
     * Save a companyDept.
     *
     * @param companyDept the entity to save.
     * @return the persisted entity.
     */
    public CompanyDept save(CompanyDept companyDept) {
        log.debug("Request to save CompanyDept : {}", companyDept);
        if (companyDept.getId() != null) {
            //Id不为空时 修改
            //查父级记录
            Optional<CompanyDept> oldParentOpt = companyDeptRepository.findById(companyDept.getParentId());
            //查自己的记录
            Optional<CompanyDept> oldDeptOpt = companyDeptRepository.findById(companyDept.getId());
            if (oldParentOpt.isPresent() && oldDeptOpt.isPresent()) {
                CompanyDept newParentDept = oldParentOpt.get();
                CompanyDept oldDept = oldDeptOpt.get();
                //两者都不为空的,拼出新的祖籍,
                String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getId();
                //拿到老的祖籍
                String oldAncestors = oldDept.getAncestors();
                //给当前修改的部门写上新祖籍
                companyDept.setAncestors(newAncestors);
                //更新当前部门下子部门的祖籍
                updateDeptChildren(companyDept.getId(), newAncestors, oldAncestors);
                return companyDeptRepository.save(companyDept);
            }
            throw new IllegalArgumentException("修改部门时出错,提供的部门信息有误! "+ this.getClass().getName());
        }

        //新增 , 先查有没有这个父ID
        Optional<CompanyDept> optionalCompanyDept =
            companyDeptRepository.findById(companyDept.getParentId());
        if (optionalCompanyDept.isEmpty()) {
            throw new IllegalArgumentException("父ID有误,不能新增部门");
        }
        companyDept.setAncestors(
            optionalCompanyDept.get().getAncestors() + "," + companyDept.getParentId()
        );
        return companyDeptRepository.save(companyDept);
    }

    /**
     * 修改子元素关系
     *
     * @param id 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long id, String newAncestors, String oldAncestors)
    {
        //用被修改的部门ID 查询子部门
        List<CompanyDept> childrenDeptList = dataJdbcRepository.selectChildrenDeptById(id);
        //如果这个集合和size大于0
        for (CompanyDept childDept : childrenDeptList) {
            // 子部门当前的祖籍.replace(老的祖籍,新的祖籍);
            // 例如:
            // [0,100,111,112].replace( [0,100,111], [0,100,102,110] )
            // 因为第一个参数里有和调用replace方法者相同的3个元素,所以这3个元素会被替换成第二个参数
            // 返回值为[0,100,102,110,112]
            childDept.setAncestors(childDept.getAncestors().replace(oldAncestors,newAncestors));
        }
        if (childrenDeptList.size() > 0) {
            //修改数据库的值
            for (CompanyDept newChild : childrenDeptList) {
                companyDeptRepository.save(newChild);
            }
        }
    }

    /**
     * Partially update a companyDept.
     *
     * @param companyDept the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyDept> partialUpdate(CompanyDept companyDept) {
        log.debug("Request to partially update CompanyDept : {}", companyDept);

        return companyDeptRepository
            .findById(companyDept.getId())
            .map(
                existingCompanyDept -> {
                    if (companyDept.getParentId() != null) {
                        existingCompanyDept.setParentId(companyDept.getParentId());
                    }
                    if (companyDept.getAncestors() != null) {
                        existingCompanyDept.setAncestors(companyDept.getAncestors());
                    }
                    if (companyDept.getDeptName() != null) {
                        existingCompanyDept.setDeptName(companyDept.getDeptName());
                    }
                    if (companyDept.getOrderNum() != null) {
                        existingCompanyDept.setOrderNum(companyDept.getOrderNum());
                    }
                    if (companyDept.getLeaderName() != null) {
                        existingCompanyDept.setLeaderName(companyDept.getLeaderName());
                    }
                    if (companyDept.getTel() != null) {
                        existingCompanyDept.setTel(companyDept.getTel());
                    }
                    if (companyDept.getEmail() != null) {
                        existingCompanyDept.setEmail(companyDept.getEmail());
                    }
                    if (companyDept.getEnable() != null) {
                        existingCompanyDept.setEnable(companyDept.getEnable());
                    }
                    if (companyDept.getDelFlag() != null) {
                        existingCompanyDept.setDelFlag(companyDept.getDelFlag());
                    }
                    if (companyDept.getCreateBy() != null) {
                        existingCompanyDept.setCreateBy(companyDept.getCreateBy());
                    }
                    if (companyDept.getCreateDate() != null) {
                        existingCompanyDept.setCreateDate(companyDept.getCreateDate());
                    }
                    if (companyDept.getLastModifyBy() != null) {
                        existingCompanyDept.setLastModifyBy(companyDept.getLastModifyBy());
                    }
                    if (companyDept.getLastModifyDate() != null) {
                        existingCompanyDept.setLastModifyDate(companyDept.getLastModifyDate());
                    }

                    return existingCompanyDept;
                }
            )
            .map(companyDeptRepository::save);
    }

    /**
     * Get all the companyDepts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyDept> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyDepts");
        return companyDeptRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<CompanyDept> findAll() {
        log.debug("Request to get all CompanyDepts");
        return (Page<CompanyDept>) companyDeptRepository.findAll();
    }

    /**
     * Get one companyDept by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyDept> findOne(Long id) {
        log.debug("Request to get CompanyDept : {}", id);
        return companyDeptRepository.findById(id);
    }

    /**
     * Delete the companyDept by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyDept : {}", id);
        companyDeptRepository.deleteById(id);
    }
}
