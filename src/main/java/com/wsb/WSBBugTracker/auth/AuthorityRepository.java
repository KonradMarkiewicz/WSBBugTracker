package com.wsb.WSBBugTracker.auth;

import com.wsb.WSBBugTracker.enums.AuthorityName;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Authority findByName(AuthorityName name);
}
