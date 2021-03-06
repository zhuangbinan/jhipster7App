package com.mycompany.myapp.config;

import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Caffeine caffeine = jHipsterProperties.getCache().getCaffeine();

        CaffeineConfiguration<Object, Object> caffeineConfiguration = new CaffeineConfiguration<>();
        caffeineConfiguration.setMaximumSize(OptionalLong.of(caffeine.getMaxEntries()));
        caffeineConfiguration.setExpireAfterWrite(OptionalLong.of(TimeUnit.SECONDS.toNanos(caffeine.getTimeToLiveSeconds())));
        caffeineConfiguration.setStatisticsEnabled(true);
        jcacheConfiguration = caffeineConfiguration;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.User.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.Book.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Author.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CommunityImages.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CommunityImageGroup.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CommunityImageGroup.class.getName() + ".communityImages");
            createCache(cm, com.mycompany.myapp.domain.Test01.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CommunityLeader.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Community.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Community.class.getName() + ".authors");
            createCache(cm, com.mycompany.myapp.domain.Community.class.getName() + ".communityLeaders");
            createCache(cm, com.mycompany.myapp.domain.Community.class.getName() + ".communityNotices");
            createCache(cm, com.mycompany.myapp.domain.CommunityNotice.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Author.class.getName() + ".books");
            createCache(cm, com.mycompany.myapp.domain.Test01.class.getName() + ".authors");
            createCache(cm, com.mycompany.myapp.domain.Community.class.getName() + ".homelandStations");
            createCache(cm, com.mycompany.myapp.domain.Company.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Company.class.getName() + ".homelandStations");
            createCache(cm, com.mycompany.myapp.domain.Buildings.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Buildings.class.getName() + ".roomAddrs");
            createCache(cm, com.mycompany.myapp.domain.HomelandStation.class.getName());
            createCache(cm, com.mycompany.myapp.domain.HomelandStation.class.getName() + ".buildings");
            createCache(cm, com.mycompany.myapp.domain.RoomAddr.class.getName());
            createCache(cm, com.mycompany.myapp.domain.RoomAddr.class.getName() + ".visitors");
            createCache(cm, com.mycompany.myapp.domain.RoomAddr.class.getName() + ".wamoliUsers");
            createCache(cm, com.mycompany.myapp.domain.TuYaCmd.class.getName());
            createCache(cm, com.mycompany.myapp.domain.TuYaDevice.class.getName());
            createCache(cm, com.mycompany.myapp.domain.TuYaDevice.class.getName() + ".tuYaCmds");
            createCache(cm, com.mycompany.myapp.domain.Visitor.class.getName());
            createCache(cm, com.mycompany.myapp.domain.WamoliFaceLibrary.class.getName());
            createCache(cm, com.mycompany.myapp.domain.WamoliUser.class.getName());
            createCache(cm, com.mycompany.myapp.domain.WamoliUser.class.getName() + ".roomAddrs");
            createCache(cm, com.mycompany.myapp.domain.WamoliUserLocation.class.getName());
            createCache(cm, com.mycompany.myapp.domain.WamoliUser.class.getName() + ".companyDepts");
            createCache(cm, com.mycompany.myapp.domain.CompanyDept.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CompanyDept.class.getName() + ".companyPosts");
            createCache(cm, com.mycompany.myapp.domain.CompanyDept.class.getName() + ".wamoliUsers");
            createCache(cm, com.mycompany.myapp.domain.CompanyPost.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CompanyPost.class.getName() + ".wamoliUsers");
            createCache(cm, com.mycompany.myapp.domain.WamoliUser.class.getName() + ".companyPosts");
            createCache(cm, com.mycompany.myapp.domain.CompanyUser.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CompanyDept.class.getName() + ".companyUsers");
            createCache(cm, com.mycompany.myapp.domain.CompanyPost.class.getName() + ".companyUsers");
            createCache(cm, com.mycompany.myapp.domain.CompanyUser.class.getName() + ".companyDepts");
            createCache(cm, com.mycompany.myapp.domain.CompanyUser.class.getName() + ".companyPosts");
            // jhipster-needle-caffeine-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
