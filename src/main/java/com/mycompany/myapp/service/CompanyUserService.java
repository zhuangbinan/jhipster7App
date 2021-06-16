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
     * @param dto 新增一个 CompanyUser 对象
     * @return 新增的对象
     */
    @Transactional
    public CompanyUser add(final CompanyUserDTO dto){

        Optional<CompanyDept> deptOpt = companyDeptRepository.findById(dto.getDeptId());
        if(deptOpt.isEmpty()){
            throw new IllegalArgumentException("没有找到该部门");
        }
        Optional<CompanyPost> postOpt = companyPostRepository.findById(dto.getPostId());
        if(postOpt.isEmpty()){
            throw new IllegalArgumentException("没有找到该岗位");
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
        companyUser.setDelFlag(false);  //这个字段WamoliUser没有
        companyUser.setCreatedBy(wamoliUser.getLastModifiedBy());
        companyUser.setCreatedDate(wamoliUser.getLastModifiedDate());

        return companyUserRepository.save(companyUser);
    }

    private WamoliUser addWamoliUser(final CompanyUserDTO dto,final User user,CompanyDept dept,CompanyPost post){
        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new IllegalArgumentException("用户身份信息有误!");
        }

        //这俩多对多关系为以后一个人兼任多个职位的需求做准备
        Set<CompanyDept> deptSet = new HashSet<>();
        deptSet.add(dept);
        Set<CompanyPost> postSet = new HashSet<>();
        postSet.add(post);

        WamoliUser wamoliUser = new WamoliUser();

        wamoliUser.setUserName(dto.getUserName());   //  姓名在user里没设置
        wamoliUser.setGender(dto.getGender());       //  性别也是
        wamoliUser.setIdCardNum(dto.getIdCardNum()); //  身份证也是
        wamoliUser.setIdCardType(CertificateType.IDCARD);
        wamoliUser.setUserType(UserType.MANAGER);    //  设置物业员工身份,这个字段和ROLE_MANAGER有些重合
        wamoliUser.setPhoneNum(user.getLogin());     //  手机号
        wamoliUser.setEmail(user.getEmail());        //  邮箱
        wamoliUser.setEnable(true);                  //  是否可用 直接整成可用了

        wamoliUser.setLastModifiedBy(login.get());   //  谁创建的
        wamoliUser.setLastModifiedDate(Instant.now());// 创建时间
        wamoliUser.setIsManager(true);                // 是物业管理者,这个字段感觉有点多余,可能要去掉

        wamoliUser.setUser(user);                    //  和User关联

        wamoliUser.setCompanyPosts(postSet);         //  职位,岗位
        wamoliUser.setCompanyDepts(deptSet);         //  部门

        return wamoliUserRepository.save(wamoliUser);
    }

    /**
     * 创建物业员工 到 jhi_user
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
        //给到ROLE_MANAGER权限,在AuthoritiesConstants类里要有这个记录,在数据库里也要有一个这个记录
        authorityRepository.findById(AuthoritiesConstants.MANAGER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("创建物业员工 到 jhi_user 完成: {}", newUser);
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
        return companyUserRepository.findById(id);
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

    //对CompanyUser逻辑删除,对对应的WamoliUser和JhiUser物理删除
    public void logicDelete(Long[] ids) {

        //不能修改系统性的用户,这里是暂时指ID为1的用户
        for (Long id : ids) {
            if (this.checkUserAllowed(id)){
                throw new IllegalArgumentException("不允许操作超级管理员用户");
            }
        }

        List<CompanyUser> list = new ArrayList<>();
        //先查询看有没有该id的值
        for (Long id : ids) {
            Optional<CompanyUser> byId = companyUserRepository.findByIdAndDelFlagIsFalse(id);
            if (byId.isEmpty()) {
                list.clear();
                throw new IllegalArgumentException("没有找到id为"+id+"的CompanyUser");
            }
            list.add(byId.get());
        }

        //开始执行CompanyUser逻辑删除
        dataJdbcRepository.logicDeleteCompanyUsers(ids);
        //开始删除WamoliUser和JhiUser
        for (CompanyUser companyUser : list) {
            int wamoliUserDelCount = wamoliUserRepository.deleteByPhoneNum(companyUser.getPhoneNum());
            if (wamoliUserDelCount != 1) {
                throw new IllegalArgumentException("删除WamoliUser手机号为:" + companyUser.getPhoneNum() + "失败");
            }
            int userDelCount = userRepository.deleteByLogin(companyUser.getPhoneNum());
            if (userDelCount != 1) {
                throw new IllegalArgumentException("删除CompanyUser账号为:" + companyUser.getPhoneNum() + "失败");
            }
        }
        list.clear();
    }

    //超级管理员用户在数据库里的ID是固定的
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
            throw new SecurityException("该用户信息有问题!");
        }

        Optional<CompanyUser> companyUserOptional = companyUserRepository.findById(id);
        if (companyUserOptional.isEmpty()) {
            throw new BadRequestAlertException("没找到该对象", "companyUserService", "idnotfound");
        }

        String login = loginOpt.get();
        CompanyUser oldCompanyUser = companyUserOptional.get();

        //修改WamoliUser
        Optional<WamoliUser> wamoliUserOptional =
            wamoliUserRepository.findOneByEmailAndPhoneNumAndUserIsNotNullAndEnableIsTrue(oldCompanyUser.getEmail(), oldCompanyUser.getPhoneNum());
        if (wamoliUserOptional.isEmpty()) {
            throw new IllegalArgumentException("修改时没有找到对应的WamoliUser");
        }

        Optional<User> userOptional = userRepository.findOneByLogin(oldCompanyUser.getPhoneNum());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("修改时没有找到对应的User");
        }

        Optional<CompanyDept> companyDeptOpt = companyDeptRepository.findById(companyUser.getDeptId());
        if (companyDeptOpt.isEmpty()) {
            throw new IllegalArgumentException("没有找到对应的部门");
        }

        Optional<CompanyPost> companyPostOptional = companyPostRepository.findById(companyUser.getPostId());
        if (companyPostOptional.isEmpty()) {
            throw new IllegalArgumentException("没有找到对应的岗位");
        }

        //构造DeptSet
        CompanyDept companyDept = companyDeptOpt.get();
        Set<CompanyDept> deptSet = new HashSet<>();
        deptSet.add(companyDept);
        //构造PostSet
        CompanyPost companyPost = companyPostOptional.get();
        Set<CompanyPost> postSet = new HashSet<>();
        postSet.add(companyPost);

        //检验新的手机号或邮箱
        String passwordEncoded = checkPhoneNumAndEmailAndReturnNewPasswordEncoded(companyUser);

        //修改WamoliUserUser
        WamoliUser wamoliUser = wamoliUserOptional.get();
        wamoliUser.setPhoneNum(companyUser.getPhoneNum());   //1.手机号
        wamoliUser.setEmail(companyUser.getEmail());         //2.邮箱
        wamoliUser.setCompanyDepts(deptSet);                 //3.部门
        wamoliUser.setGender(companyUser.getGender());       //4.性别
        wamoliUser.setEnable(companyUser.getEnable());       //5.账号状态
        wamoliUser.setCompanyPosts(postSet);                 //6.岗位
        wamoliUser.setIdCardNum(companyUser.getIdCardNum()); //7.身份证号
        wamoliUser.setUserName(companyUser.getUserName());   //8.姓名

        //设置其他项
        wamoliUser.setLastModifiedBy(login);
        wamoliUser.setLastModifiedDate(Instant.now());

        //修改User
        User user = userOptional.get();
        user.setLogin(companyUser.getPhoneNum());
        user.setEmail(companyUser.getEmail());
        user.setLastModifiedBy(login);
        user.setLastModifiedDate(Instant.now());
        //这里直接修改账号和邮箱,不知道密码还能不能用? 不能
        //能不能再加个修改密码呢? 所以要在修改手机号时把密码也给改了,密码改为,新手机号的后6位
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
        String password = companyUser.getPhoneNum().substring(companyUser.getPhoneNum().length()-6); //设置初始密码,为手机号后6位
        return passwordEncoder.encode(password);
    }

}
