package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.enumeration.CertificateType;
import com.mycompany.myapp.domain.enumeration.UserType;
import com.mycompany.myapp.repository.*;

import java.time.Instant;
import java.util.*;

import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.CompanyUserDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompanyUser}.
 */
@Service
@Transactional
public class CompanyUserService {

    private final Logger log = LoggerFactory.getLogger(CompanyUserService.class);

    private final CompanyUserRepository companyUserRepository;

    private final WamoliUserRepository wamoliUserRepository;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final CompanyDeptRepository companyDeptRepository;

    private final CompanyPostRepository companyPostRepository;

    private final CacheManager cacheManager;

    private final PasswordEncoder passwordEncoder;

    private final DataJdbcRepository dataJdbcRepository;

    public CompanyUserService(CompanyUserRepository companyUserRepository, WamoliUserRepository wamoliUserRepository, UserRepository userRepository, AuthorityRepository authorityRepository, CompanyDeptRepository companyDeptRepository, CompanyPostRepository companyPostRepository, CacheManager cacheManager, PasswordEncoder passwordEncoder, DataJdbcRepository dataJdbcRepository) {
        this.companyUserRepository = companyUserRepository;
        this.wamoliUserRepository = wamoliUserRepository;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.companyDeptRepository = companyDeptRepository;
        this.companyPostRepository = companyPostRepository;
        this.cacheManager = cacheManager;
        this.passwordEncoder = passwordEncoder;
        this.dataJdbcRepository = dataJdbcRepository;
    }

    /**
     *
     * @param dto ???????????? CompanyUser ??????
     * @return ???????????????
     */
    @Transactional
    public CompanyUser add(final CompanyUserDTO dto){

        Optional<CompanyDept> deptOpt = companyDeptRepository.findById(dto.getDeptId());
        if(deptOpt.isEmpty()){
            throw new IllegalArgumentException("?????????????????????");
        }
        Optional<CompanyPost> postOpt = companyPostRepository.findById(dto.getPostId());
        if(postOpt.isEmpty()){
            throw new IllegalArgumentException("?????????????????????");
        }
        User user = this.addUser(dto);

        WamoliUser wamoliUser = this.addWamoliUser(dto, user,deptOpt.get(),postOpt.get());

        return this.addCompanyUser(dto,wamoliUser);
    }

    private CompanyUser addCompanyUser(CompanyUserDTO dto,WamoliUser wamoliUser){
        CompanyUser companyUser = new CompanyUser();

        companyUser.setRemark(dto.getRemark());
        companyUser.setDeptName(dto.getDeptName());
        companyUser.setPostName(dto.getPostName());

        companyUser.setUserName(wamoliUser.getUserName());
        companyUser.setGender(wamoliUser.getGender());
        companyUser.setPhoneNum(wamoliUser.getPhoneNum());
        companyUser.setEmail(wamoliUser.getEmail());
        companyUser.setIdCardNum(wamoliUser.getIdCardNum());
        companyUser.setEnable(wamoliUser.getEnable());
        companyUser.setDelFlag(false);  //????????????WamoliUser??????
        companyUser.setCreatedBy(wamoliUser.getLastModifiedBy());
        companyUser.setCreatedDate(wamoliUser.getLastModifiedDate());

        return companyUserRepository.save(companyUser);
    }

    private WamoliUser addWamoliUser(final CompanyUserDTO dto,final User user,CompanyDept dept,CompanyPost post){
        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new IllegalArgumentException("????????????????????????!");
        }

        //???????????????????????????????????????????????????????????????????????????
        Set<CompanyDept> deptSet = new HashSet<>();
        deptSet.add(dept);
        Set<CompanyPost> postSet = new HashSet<>();
        postSet.add(post);

        WamoliUser wamoliUser = new WamoliUser();

        wamoliUser.setUserName(dto.getUserName());   //  ?????????user????????????
        wamoliUser.setGender(dto.getGender());       //  ????????????
        wamoliUser.setIdCardNum(dto.getIdCardNum()); //  ???????????????
        wamoliUser.setIdCardType(CertificateType.IDCARD);
        wamoliUser.setUserType(UserType.MANAGER);    //  ????????????????????????,???????????????ROLE_MANAGER????????????
        wamoliUser.setPhoneNum(user.getLogin());     //  ?????????
        wamoliUser.setEmail(user.getEmail());        //  ??????
        wamoliUser.setEnable(true);                  //  ???????????? ?????????????????????

        wamoliUser.setLastModifiedBy(login.get());   //  ????????????
        wamoliUser.setLastModifiedDate(Instant.now());// ????????????
        wamoliUser.setIsManager(true);                // ??????????????????,??????????????????????????????,???????????????

        wamoliUser.setUser(user);                    //  ???User??????

//        wamoliUser.setCompanyPosts(postSet);         //  ??????,??????
//        wamoliUser.setCompanyDepts(deptSet);         //  ??????

        return wamoliUserRepository.save(wamoliUser);
    }

    /**
     * ?????????????????? ??? jhi_user
     * @param dto
     * @return
     */
    private User addUser(CompanyUserDTO dto){

        String encryptedPassword = checkPhoneNumAndEmailAndReturnNewPasswordEncoded(dto);
        User newUser = new User();

        newUser.setLogin(dto.getPhoneNum().trim());
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(dto.getEmail().toLowerCase());
        newUser.setLangKey("zh-cn");
        newUser.setActivated(true);
        Set<Authority> authorities = new HashSet<>();
        //??????ROLE_MANAGER??????,???AuthoritiesConstants????????????????????????,??????????????????????????????????????????
        authorityRepository.findById(AuthoritiesConstants.MANAGER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("?????????????????? ??? jhi_user ??????: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    /**
     * Save a companyUser.
     *
     * @param companyUser the entity to save.
     * @return the persisted entity.
     */
    public CompanyUser save(CompanyUser companyUser) {
        log.debug("Request to save CompanyUser : {}", companyUser);
        return companyUserRepository.save(companyUser);
    }

    /**
     * Partially update a companyUser.
     *
     * @param companyUser the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyUser> partialUpdate(CompanyUser companyUser) {
        log.debug("Request to partially update CompanyUser : {}", companyUser);

        return companyUserRepository
            .findById(companyUser.getId())
            .map(
                existingCompanyUser -> {
                    if (companyUser.getUserName() != null) {
                        existingCompanyUser.setUserName(companyUser.getUserName());
                    }
                    if (companyUser.getIdCardNum() != null) {
                        existingCompanyUser.setIdCardNum(companyUser.getIdCardNum());
                    }
                    if (companyUser.getGender() != null) {
                        existingCompanyUser.setGender(companyUser.getGender());
                    }
                    if (companyUser.getPhoneNum() != null) {
                        existingCompanyUser.setPhoneNum(companyUser.getPhoneNum());
                    }
                    if (companyUser.getEmail() != null) {
                        existingCompanyUser.setEmail(companyUser.getEmail());
                    }
                    if (companyUser.getDeptName() != null) {
                        existingCompanyUser.setDeptName(companyUser.getDeptName());
                    }
                    if (companyUser.getPostName() != null) {
                        existingCompanyUser.setPostName(companyUser.getPostName());
                    }
                    if (companyUser.getEnable() != null) {
                        existingCompanyUser.setEnable(companyUser.getEnable());
                    }
                    if (companyUser.getRemark() != null) {
                        existingCompanyUser.setRemark(companyUser.getRemark());
                    }
                    if (companyUser.getDelFlag() != null) {
                        existingCompanyUser.setDelFlag(companyUser.getDelFlag());
                    }
                    if (companyUser.getCreatedBy() != null) {
                        existingCompanyUser.setCreatedBy(companyUser.getCreatedBy());
                    }
                    if (companyUser.getCreatedDate() != null) {
                        existingCompanyUser.setCreatedDate(companyUser.getCreatedDate());
                    }
                    if (companyUser.getLastModifiedBy() != null) {
                        existingCompanyUser.setLastModifiedBy(companyUser.getLastModifiedBy());
                    }
                    if (companyUser.getLastModifiedDate() != null) {
                        existingCompanyUser.setLastModifiedDate(companyUser.getLastModifiedDate());
                    }

                    return existingCompanyUser;
                }
            )
            .map(companyUserRepository::save);
    }

    /**
     * Get all the companyUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyUser> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyUsers");
        return companyUserRepository.findAll(pageable);
    }

    /**
     * Get one companyUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyUser> findOne(Long id) {
        log.debug("Request to get CompanyUser : {}", id);
        return companyUserRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the companyUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyUser : {}", id);
        companyUserRepository.deleteById(id);
    }

    //???CompanyUser????????????,????????????WamoliUser???JhiUser????????????
    public void logicDelete(Long[] ids) {

        //??????????????????????????????,??????????????????ID???1?????????
        for (Long id : ids) {
            if (this.checkUserAllowed(id)){
                throw new IllegalArgumentException("????????????????????????????????????");
            }
        }

        List<CompanyUser> list = new ArrayList<>();
        //????????????????????????id??????
        for (Long id : ids) {
            Optional<CompanyUser> byId = companyUserRepository.findByIdAndDelFlagIsFalse(id);
            if (byId.isEmpty()) {
                list.clear();
                throw new IllegalArgumentException("????????????id???"+id+"???CompanyUser");
            }
            list.add(byId.get());
        }

        //????????????CompanyUser????????????
        dataJdbcRepository.logicDeleteCompanyUsers(ids);
        //????????????WamoliUser???JhiUser
        for (CompanyUser companyUser : list) {
            int wamoliUserDelCount = wamoliUserRepository.deleteByPhoneNum(companyUser.getPhoneNum());
            if (wamoliUserDelCount != 1) {
                throw new IllegalArgumentException("??????WamoliUser????????????:" + companyUser.getPhoneNum() + "??????");
            }
            int userDelCount = userRepository.deleteByLogin(companyUser.getPhoneNum());
            if (userDelCount != 1) {
                throw new IllegalArgumentException("??????CompanyUser?????????:" + companyUser.getPhoneNum() + "??????");
            }
        }
        list.clear();
    }

    //???????????????????????????????????????ID????????????
    private boolean checkUserAllowed(Long id) {
        return id != null && 1L == id;
    }


    public List<CompanyUser> findAllWithDelFlag(Pageable pageable) {
        List<CompanyUser> result = dataJdbcRepository.findCompanyUserAllWithDelFlagIsFalse(pageable);
        return result == null ? new ArrayList<>() : result;
    }

    public CompanyUser update(Long id, CompanyUserDTO companyUser) {
        log.debug("Request to update CompanyUser : {}", companyUser);

        Optional<String> loginOpt = SecurityUtils.getCurrentUserLogin();
        if (loginOpt.isEmpty()) {
            throw new SecurityException("????????????????????????!");
        }

        Optional<CompanyUser> companyUserOptional = companyUserRepository.findById(id);
        if (companyUserOptional.isEmpty()) {
            throw new BadRequestAlertException("??????????????????", "companyUserService", "idnotfound");
        }

        String login = loginOpt.get();
        CompanyUser oldCompanyUser = companyUserOptional.get();

        //??????WamoliUser
        Optional<WamoliUser> wamoliUserOptional =
            wamoliUserRepository.findOneByEmailAndPhoneNumAndUserIsNotNull(oldCompanyUser.getEmail(), oldCompanyUser.getPhoneNum());
        if (wamoliUserOptional.isEmpty()) {
            throw new IllegalArgumentException("??????????????????????????????WamoliUser");
        }

        Optional<User> userOptional = userRepository.findOneByLogin(oldCompanyUser.getPhoneNum());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("??????????????????????????????User");
        }

        Optional<CompanyDept> companyDeptOpt = companyDeptRepository.findById(companyUser.getDeptId());
        if (companyDeptOpt.isEmpty()) {
            throw new IllegalArgumentException("???????????????????????????");
        }

        Optional<CompanyPost> companyPostOptional = companyPostRepository.findById(companyUser.getPostId());
        if (companyPostOptional.isEmpty()) {
            throw new IllegalArgumentException("???????????????????????????");
        }

        //??????DeptSet
        CompanyDept companyDept = companyDeptOpt.get();
        Set<CompanyDept> deptSet = new HashSet<>();
        deptSet.add(companyDept);
        //??????PostSet
        CompanyPost companyPost = companyPostOptional.get();
        Set<CompanyPost> postSet = new HashSet<>();
        postSet.add(companyPost);

        //??????????????????????????????
        String passwordEncoded = checkPhoneNumAndEmailAndReturnNewPasswordEncoded(companyUser);

        //??????WamoliUserUser
        WamoliUser wamoliUser = wamoliUserOptional.get();
        wamoliUser.setPhoneNum(companyUser.getPhoneNum());   //1.?????????
        wamoliUser.setEmail(companyUser.getEmail());         //2.??????
//        wamoliUser.setCompanyDepts(deptSet);                 //3.??????
        wamoliUser.setGender(companyUser.getGender());       //4.??????
        wamoliUser.setEnable(companyUser.getEnable());       //5.????????????
//        wamoliUser.setCompanyPosts(postSet);                 //6.??????
        wamoliUser.setIdCardNum(companyUser.getIdCardNum()); //7.????????????
        wamoliUser.setUserName(companyUser.getUserName());   //8.??????

        //???????????????
        wamoliUser.setLastModifiedBy(login);
        wamoliUser.setLastModifiedDate(Instant.now());

        //??????User
        User user = userOptional.get();
        user.setLogin(companyUser.getPhoneNum());
        user.setEmail(companyUser.getEmail());
        user.setLastModifiedBy(login);
        user.setLastModifiedDate(Instant.now());
        //?????????????????????????????????,??????????????????????????????? ??????
        //?????????????????????????????????? ???????????????????????????????????????????????????,????????????,??????????????????6???
        user.setPassword(passwordEncoded);

        CompanyUser dto = new CompanyUser();
        BeanUtils.copyProperties(companyUser,dto);
        dto.setDelFlag(false);
        dto.setCreatedDate(user.getCreatedDate());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setLastModifiedBy(SecurityUtils.getCurrentUserLogin().get());
        dto.setLastModifiedDate(Instant.now());
        return companyUserRepository.save(dto);
    }

    private String checkPhoneNumAndEmailAndReturnNewPasswordEncoded(CompanyUserDTO companyUser){
        userRepository
            .findOneByLogin(companyUser.getPhoneNum().trim())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                }
            );
        userRepository
            .findOneByEmailIgnoreCase(companyUser.getEmail())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                }
            );
        String password = companyUser.getPhoneNum().substring(companyUser.getPhoneNum().length()-6); //??????????????????,???????????????6???
        return passwordEncoder.encode(password);
    }

}
