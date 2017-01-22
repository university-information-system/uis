package at.ac.tuwien.inso.controller.grade;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import at.ac.tuwien.inso.service.GradeService;
import at.ac.tuwien.inso.view.pdf.GradePDFView;


@Controller
@RequestMapping("/public/grade")
public class PublicGradeController{

    @Autowired
    public GradeService gradeService;

    @GetMapping
    public ModelAndView generateGradePDF(@RequestParam("identifier") String identifier,
                                         ModelMap modelMap,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {

        View view = new GradePDFView();
        modelMap.addAttribute("grade", gradeService.getForValidation(identifier));
        modelMap.addAttribute("validationLink",
                request.getRequestURL().toString() + "?identifier=" + identifier);
        ModelAndView modelAndView = new ModelAndView(view);
        return modelAndView;
    }



}
