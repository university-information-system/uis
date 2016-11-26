package at.ac.tuwien.inso.service;

import org.springframework.security.access.prepost.PreAuthorize;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public interface TagService {

    @PreAuthorize("isAuthenticated()")
    List<Tag> findAll();
}
