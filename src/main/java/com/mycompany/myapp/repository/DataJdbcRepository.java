package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityImages;
import com.mycompany.myapp.domain.CompanyDept;
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
}
