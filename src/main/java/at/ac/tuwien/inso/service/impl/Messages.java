package at.ac.tuwien.inso.service.impl;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class Messages {

    public static final Locale LOCALE = Locale.ENGLISH;

    @Autowired
    private MessageSource messageSource;

    public String get(String path) {
        return messageSource.getMessage(path, null, LOCALE);
    }
}
