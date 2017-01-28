package at.ac.tuwien.inso.controller.grade;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.util.UriComponentsBuilder;

import at.ac.tuwien.inso.entity.Grade;
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
                                         UriComponentsBuilder uriComponentsBuilder) throws Exception {

        View view = new GradePDFView();
        Grade grade = gradeService.getForValidation(identifier);
        if (grade == null) {
            return new ModelAndView("error/404");
        }
        modelMap.addAttribute("grade", grade);
        modelMap.addAttribute(
                "validationLink",
                uriComponentsBuilder
                        .path("/public/grade")
                        .queryParam("identifier", identifier)
                        .build()
                        .toString()
        );
        ModelAndView modelAndView = new ModelAndView(view);
        return modelAndView;
    }



}
