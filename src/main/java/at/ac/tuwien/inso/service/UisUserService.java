package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface UisUserService {

    List<UisUser> findAllMatching(String searchFilter);

    UisUser findOne(long id);

    boolean existsUserWithIdentificationNumber(String id);
}
