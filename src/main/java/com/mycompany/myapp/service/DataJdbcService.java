package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CommunityImages;
import com.mycompany.myapp.domain.CompanyDept;
import com.mycompany.myapp.domain.CompanyPost;
import com.mycompany.myapp.domain.CompanyUser;
import com.mycompany.myapp.repository.DataJdbcRepository;
import io.jsonwebtoken.lang.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class DataJdbcService {

    private final DataJdbcRepository dataJdbcRepository;

    public DataJdbcService(DataJdbcRepository dataJdbcRepository) {
        this.dataJdbcRepository = dataJdbcRepository;
    }

    public List<CommunityImages> getCommunityImagesByGroupId(Long id){
        List<CommunityImages> communityImagesByGroupId = dataJdbcRepository.getCommunityImagesByGroupId(id);
        if (communityImagesByGroupId != null) {
            return communityImagesByGroupId;
        }
        return new ArrayList<CommunityImages>();
    }

    public List<CompanyDept> selectChildrenDeptById(Long id){
        return dataJdbcRepository.selectChildrenDeptById(id);
    }

    /**
     * 查找所有未被逻辑删除的部门
     * @return
     */
    public List<CompanyDept> findAllWithDelFlagIsFalse(){
        return dataJdbcRepository.findAllWithDelFlagIsFalse();
    }

    /**
     * 逻辑删除
     * @param id
     */
    public void logicDeleteDept(Long id) {
        dataJdbcRepository.logicDeleteDept(id);
    }

    /**
     *
     * @param id 部门id
     * @return 该部门是否有子部门 有为 true
     */
    public boolean hasChildByDeptId(Long id) {
        return dataJdbcRepository.hasChildByDeptId(id) > 0;
    }

    /**
     *
     * @param id 部门id
     * @return 该部门如果有用户返回 true
     */
    public boolean checkDeptExistUser(Long id) {
        return dataJdbcRepository.checkDeptExistUser(id) > 0;
    }

    /**
     * 新增用户时 查询可以选择的岗位
     * @return 获取所有部门信息
     */
    @Transactional(readOnly = true)
    public List<CompanyPost> getAllCompanyPostsSelect() {
        List<CompanyPost> list = dataJdbcRepository.getAllCompanyPostsSelect();
        return list == null ? new ArrayList<>() : list;
    }

    public int updateAuthorityById(String oldName, String newName){
        return dataJdbcRepository.updateAuthorityById(oldName,newName);
    }

    /**
     * 在物业用户管理页面,点击某个部门时,
     * @param deptId 点击的部门ID
     * @param page 页码数
     * @param size 每页数量
     * @return 该部门ID下面的所有员工信息并分页
     */
    public List<CompanyUser> findCompanyUserByDeptId(Long deptId,int page , int size){
        List<CompanyUser> list = dataJdbcRepository.findCompanyUserByDeptId(deptId,page,size);
        return list == null ? new ArrayList<>() : list;
    }

    public void logicDeleteCompanyUsers(Long[] ids) {
        dataJdbcRepository.logicDeleteCompanyUsers(ids);
    }

}
