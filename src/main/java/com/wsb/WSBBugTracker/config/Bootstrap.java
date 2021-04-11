package com.wsb.WSBBugTracker.config;

import com.wsb.WSBBugTracker.auth.Authority;
import com.wsb.WSBBugTracker.auth.AuthorityRepository;
import com.wsb.WSBBugTracker.enums.AuthorityName;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class Bootstrap implements InitializingBean {

    private final AuthorityRepository authorityRepository;

    public Bootstrap(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("Rozpoczynamy przygotowywanie aplikacji...");

        prepareAuthorities();
    }

    private void prepareAuthorities() {
        for (AuthorityName authorityName : AuthorityName.values()) {
            Authority existingAuthority = authorityRepository.findByName(authorityName);

            if (existingAuthority != null) {
                continue;
            }

            Authority newAuthority = new Authority(authorityName);
            authorityRepository.save(newAuthority);
        }
    }
}
