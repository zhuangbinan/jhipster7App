package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityImages;
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


}
