package at.ac.tuwien.inso.controller.student;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.course_recommendation.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.math.*;
import java.security.*;
import java.util.*;

import static java.util.Arrays.*;

@Controller
@RequestMapping("/student/courses")
public class StudentCoursesController {

    private static final Logger log = LoggerFactory.getLogger(StudentMyCoursesController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RecommendationService recommendationService;

    @ModelAttribute("allCourses")
    private List<Course> getAllCourses(@RequestParam(value = "search", required = false) String search) {
        if (search == null || search.isEmpty()) {
            Student student = studentService.findOne(userAccountService.getCurrentLoggedInUser());
            List<Course> recommendedCourses = recommendationService.recommendCourses(student);
            if (!recommendedCourses.isEmpty()) {
                return recommendedCourses;
            }
        }

        final String searchString = getSearchString(search);
        return courseService.findCourseForCurrentSemesterWithName(searchString);
    }

    @ModelAttribute("searchString")
    private String getSearchString(@RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return search;
        }

        return "";
    }

    @GetMapping
    public String courses() {
        return "/student/courses";
    }

    @GetMapping("/{id}")
    public String course(@PathVariable Long id,
                         Model model,
                         Principal principal) {
        log.debug("Student " + principal.getName() + " requesting course " + id + " details");

        String description = "Bicycle rights flannel crucifix, vexillologist venmo migas yuccie. Cardigan vape heirloom, flannel unicorn thundercats williamsburg tilde pitchfork organic yuccie squid crucifix poke. Pour-over butcher hammock leggings, church-key woke knausgaard venmo blue bottle cred franzen fam tote bag lyft air plant. 3 wolf moon kitsch vice vape bespoke raw denim, distillery street art kickstarter godard tote bag activated charcoal echo park neutra. Chicharrones paleo leggings, tote bag yr hammock organic activated charcoal lo-fi +1 coloring book franzen craft beer readymade. Pour-over retro jianbing, pinterest neutra snackwave vexillologist vice brooklyn succulents gastropub. Disrupt fanny pack coloring book vegan post-ironic jean shorts.\n" +
                "\n" +
                "Cardigan hella selvage viral, etsy pinterest schlitz 8-bit church-key. You probably haven't heard of them meggings biodiesel, heirloom vaporware shabby chic pickled letterpress blog +1 snackwave. Hoodie selvage mustache, tumeric tbh woke DIY air plant prism flannel blog. Man braid affogato iceland distillery cardigan fanny pack. Plaid blog dreamcatcher gluten-free, 90's fashion axe portland. Helvetica hell of chartreuse 8-bit. Pug venmo enamel pin affogato.\n" +
                "\n" +
                "Ramps squid art party iPhone, gluten-free direct trade unicorn tote bag tilde selvage. Man braid kitsch messenger bag aesthetic authentic wayfarers kogi hexagon enamel pin helvetica brooklyn. Humblebrag subway tile stumptown, tofu man bun kombucha tattooed truffaut la croix lyft vinyl ethical hella crucifix. 3 wolf moon vegan fixie lumbersexual brunch kickstarter. Bitters banh mi thundercats, blog live-edge fap church-key austin tofu. Lo-fi intelligentsia banh mi +1, four dollar toast keffiyeh master cleanse air plant yr pok pok stumptown tbh pitchfork shoreditch. Fap literally meditation, selfies keytar bespoke farm-to-table crucifix taxidermy.\n" +
                "\n" +
                "Austin cold-pressed art party fanny pack, leggings vape YOLO shabby chic. Cliche gochujang migas, tbh cray stumptown whatever ennui squid fanny pack. Plaid next level irony green juice ethical skateboard cronut everyday carry asymmetrical. Street art disrupt art party chicharrones DIY chillwave. Tumeric hexagon vexillologist XOXO street art brunch, truffaut swag keytar semiotics snackwave stumptown poutine leggings synth. Tumeric knausgaard roof party fashion axe, retro glossier 8-bit poutine. Artisan hoodie leggings poutine, franzen scenester irony.";
        model.addAttribute(
                "course",
                new CourseDetailsForStudent()
                        .setId(id)
                        .setName("VO Algebra und Diskrete Mathematik für Informatik und Wirtschaftsinformatik")
                        .setSemester("SS2016")
                        .setEcts(BigDecimal.TEN)
                        .setTags(asList("fun", "mathe", "problem solving", "tag 1", "tag 2", "tag 3"))
                        .setDescription(description)
                        .setLecturers(asList(new Lecturer("", "John Brown", "john@uis.at"), new Lecturer("", "Mary Ann", "mary@uis.at")))
                        .setStudyplans(asList(
                                new SubjectForStudyPlan(null, new StudyPlan("Bachelor Software Engineering", null), true, 1),
                                new SubjectForStudyPlan(null, new StudyPlan("Bachelor Mathe", null), false, null),
                                new SubjectForStudyPlan(null, new StudyPlan("Master Computer Science", null), true, 4)
                        ))
                        .setPreconditions(asList(
                                new Subject("Mathe Basic", BigDecimal.TEN)
                        ))
        );

        return "/student/course-details";
    }

}
