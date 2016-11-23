package at.ac.tuwien.inso.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(DataAccessException.class)
    public ModelAndView handleDataAccessExceptions(HttpServletRequest request, DataAccessException ex) {
        logger.info("DataAccessException: " + ex.getMessage() + "\n url="+request.getRequestURL());
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", messageSource.getMessage("error.dataaccess", null, LocaleContextHolder.getLocale()));
        mav.setViewName("error");
        return mav;
    }
}
