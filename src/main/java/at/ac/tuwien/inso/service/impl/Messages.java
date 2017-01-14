package at.ac.tuwien.inso.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.i18n.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class Messages {
	
	private static final Logger log = LoggerFactory.getLogger(Messages.class);

    public static final Locale LOCALE = Locale.ENGLISH;

    @Autowired
    private MessageSource messageSource;

    public String get(String path) {
    	log.info("getting messages for path "+path);
        return messageSource.getMessage(path, null, LOCALE);
    }

    public String msg(String path, Object... args) {
    	log.info("getting messages for object and path "+path);
        return messageSource.getMessage(path, args, LocaleContextHolder.getLocale());
    }
}
