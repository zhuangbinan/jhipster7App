package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CommunityImages;
import com.mycompany.myapp.domain.CompanyDept;
import com.mycompany.myapp.repository.DataJdbcRepository;
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

}
