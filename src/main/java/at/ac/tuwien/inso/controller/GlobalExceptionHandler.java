package at.ac.tuwien.inso.controller;

import at.ac.tuwien.inso.exception.*;
import org.slf4j.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.i18n.*;
import org.springframework.dao.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(value=HttpStatus.CONFLICT)
    public ModelAndView handleDataAccessExceptions(HttpServletRequest request, DataAccessException ex) {
        logger.info("DataAccessException: " + ex.getMessage() + "\n url="+request.getRequestURL());
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", messageSource.getMessage("error.dataaccess", null, LocaleContextHolder.getLocale()));
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(BusinessObjectNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView handleBusinessObjectNotFoundExceptions(HttpServletRequest request, BusinessObjectNotFoundException ex) {
        logger.info("BusinessObjectNotFoundException: " + ex.getMessage() + "\n url="+request.getRequestURL());
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(ActionNotAllowedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ModelAndView handleActionNotAllowedExceptions(HttpServletRequest request, ActionNotAllowedException ex) {
        logger.info("ActionNotAllowedException: " + ex.getMessage() + "\n url=" + request.getRequestURL());
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleTypeMismatchExceptions(HttpServletRequest request, TypeMismatchException ex) {
        logger.info("TypeMismatchRequest: " + ex.getMessage() + "\n url="+request.getRequestURL());
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", messageSource.getMessage("error.badrequest", null, LocaleContextHolder.getLocale()));
        mav.setViewName("error");
        return mav;
    }
}
