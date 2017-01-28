package at.ac.tuwien.inso.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import at.ac.tuwien.inso.exception.ActionNotAllowedException;
import at.ac.tuwien.inso.exception.BusinessObjectNotFoundException;
import at.ac.tuwien.inso.exception.ValidationException;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public ModelAndView handlerExceptions(HttpServletRequest request, Exception exception) {
        logger.warn("Arbitrary exception happened", exception);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(value=HttpStatus.CONFLICT)
    public ModelAndView handleDataAccessExceptions(HttpServletRequest request, DataAccessException ex) {
        logger.warn("DataAccessException: " + request.getRequestURL(), ex);
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", messageSource.getMessage("error.dataaccess", null, LocaleContextHolder.getLocale()));
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(BusinessObjectNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView handleBusinessObjectNotFoundExceptions(HttpServletRequest request, BusinessObjectNotFoundException ex) {
        logger.warn("BusinessObjectNotFoundException: " + request.getRequestURL(), ex);
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationExceptions(HttpServletRequest request, ValidationException ex) {
        logger.warn("ValidationException: " + request.getRequestURL(), ex);
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(ActionNotAllowedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ModelAndView handleActionNotAllowedExceptions(HttpServletRequest request, ActionNotAllowedException ex) {
        logger.warn("ActionNotAllowedException: " + request.getRequestURL(), ex);
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleTypeMismatchExceptions(HttpServletRequest request, TypeMismatchException ex) {
        logger.warn("TypeMismatchRequest: " + request.getRequestURL(), ex);
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", messageSource.getMessage("error.badrequest", null, LocaleContextHolder.getLocale()));
        mav.setViewName("error");
        return mav;
    }
}
