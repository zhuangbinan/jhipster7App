package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityImages;
import com.mycompany.myapp.domain.CompanyDept;
import com.mycompany.myapp.domain.CompanyPost;
import com.mycompany.myapp.domain.CompanyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CommunityImages> getCommunityImagesByGroupId(Long id){
        String sql = String.format("select ci.* from community_images ci , community_image_group cig where cig.id = ci.community_image_group_id  and cig.id = '%s'", id);
        return jdbcTemplate.query(sql , new BeanPropertyRowMapper<>(CommunityImages.class));
    }

    public List<CompanyDept> selectChildrenDeptById(Long id){
        String sql = String.format("select wcd.* from wamoli_company_dept wcd where find_in_set(%s, wcd.ancestors)",id);
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(CompanyDept.class));
    }


    public List<CompanyDept> findAllWithDelFlagIsFalse() {
        String sql = String.format("select wcd.* from wamoli_company_dept wcd where wcd.del_flag= '0' ");
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(CompanyDept.class));
    }

    public void logicDeleteDept(Long id) {
        String sql = String.format("update wamoli_company_dept set del_flag = 1 where id = %s",id);
        int update = jdbcTemplate.update(sql);
        if (update != 1) {
            throw new IllegalArgumentException("逻辑删除id为"+id + "的操作失败!");
        }
    }

    public int hasChildByDeptId(Long id) {
        String sql = String.format("select count(1) from wamoli_company_dept where del_flag= '0' and parent_id = %s limit 1",id);
        Integer integer = jdbcTemplate.queryForObject(sql, Integer.class);
        return integer != null ? integer : 0;
    }

    public int checkDeptExistUser(Long id) {
        String sql = String.format(
            " select count(1) from " +
            " wamoli_user wu, " +
            " rel_wamoli_user__company_dept  rel," +
            " wamoli_company_dept wcd " +
            " where " +
            " wu.id = rel.wamoli_user_id " +
            " and " +
            " wcd.id = rel.company_dept_id" +
            " and " +
            " wcd.del_flag = 0 " + " and wcd.id = %s ",id);
        Integer integer = jdbcTemplate.queryForObject(sql, Integer.class);
        return integer != null ? integer : 0;
    }

    /**
     * 新增用户时 查询可以选择的岗位
     * @return
     */
    public List<CompanyPost> getAllCompanyPostsSelect() {
        String sql = String.format("select wcp.id,wcp.post_code,wcp.post_name,wcp.order_num from wamoli_company_post wcp where wcp.enable = '1' ");
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(CompanyPost.class));
    }

    public int updateAuthorityById(String oldName,String newName) {
        String sql = String.format("update jhi_authority set name = '%s' where name = '%s' ", newName , oldName);
        return jdbcTemplate.update(sql);
    }

    /**
     * 在物业员工管理时
     * 按部门ID查询部门下所有员工
     * @param deptId 部门ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 按部门ID查询部门下所有员工并返回分页集合
     */
    public List<CompanyUser> findCompanyUserByDeptId(Long deptId , int pageNum , int pageSize) {
        String sql = String.format(
            "SELECT " +
                "wcu.* " +
            "FROM " +
                "wamoli_company_user wcu " +
            "WHERE " +
                "wcu.dept_name = ( " +
                                "SELECT " +
                                    "wcd.dept_name " +
                                "FROM " +
                                    "wamoli_company_dept wcd " +
                                "WHERE " +
                                    "wcd.id = %s )" +
            "LIMIT %s , %s ", deptId, pageNum, pageSize);

        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(CompanyUser.class));
    }


}
