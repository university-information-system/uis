package at.ac.tuwien.inso.service.study_progress.impl;

import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.Course;
import at.ac.tuwien.inso.entity.Feedback;
import at.ac.tuwien.inso.entity.Grade;
import at.ac.tuwien.inso.entity.SemesterType;
import at.ac.tuwien.inso.entity.Student;
import at.ac.tuwien.inso.entity.StudyPlanRegistration;
import at.ac.tuwien.inso.service.*;
import at.ac.tuwien.inso.service.study_progress.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

import static java.util.Collections.*;
import static java.util.Comparator.*;

@Service
public class StudyProgressServiceImpl implements StudyProgressService {

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private FeedbackService feedbackService;

    @Override
    @Transactional(readOnly = true)
    public StudyProgress studyProgressFor(Student student) {
    	SemesterDto currentSemester = semesterService.getOrCreateCurrentSemester();
        
        List<SemesterDto> semesters = studentSemesters(student);
        List<Course> courses = courseService.findAllForStudent(student);
        List<Grade> grades = gradeService.findAllOfStudent(student);
        List<Feedback> feedbacks = feedbackService.findAllOfStudent(student);
        
      
        List<SemesterProgress> semestersProgress = semesters.stream()
                .map(it -> new SemesterProgress(it, courseRegistrations(it, currentSemester, courses, grades, feedbacks)))
                .collect(Collectors.toList());
        
        return new StudyProgress(currentSemester, semestersProgress);
    }

    private List<SemesterDto> studentSemesters(Student student) {
    	SemesterDto firstSem = getFirstSemesterFor(student);
    	if(firstSem!=null){
    		return semesterService.findAllSince(getFirstSemesterFor(student));
    	}else{
    		return new ArrayList<SemesterDto>();
    	}
    }

    /**
     * Get the first semester the student registered for
     *
     * TODO: WTF does this code? Please clean up!
     *
     * @param student
     * @return
     */
    private SemesterDto getFirstSemesterFor(Student student) {
        List<StudyPlanRegistration> registrations = student.getStudyplans();

        // TODO refactor: this is not a valid way to treat Semesters
    	SemesterDto min = new SemesterDto(Integer.MAX_VALUE, SemesterType.SummerSemester);
    	min.setId(Long.MAX_VALUE);
    	
    	for(StudyPlanRegistration spr: registrations){
    		if(min!=null&spr!=null&&spr.getRegisteredSince()!=null&&min.getId() > spr.getRegisteredSince().getId()){
    			min = spr.getRegisteredSince().toDto();
    		}
    	}

    	if(min.getId().longValue()==Long.MAX_VALUE){
    		return null;
    	}
    	return min;
        
    }

    private List<CourseRegistration> courseRegistrations(SemesterDto semester, SemesterDto currentSemester, List<Course> courses, List<Grade> grades, List<Feedback> feedbacks) {
        return courses.stream()
                .filter(it -> it.getSemester().toDto().equals(semester))
                .map(it -> new CourseRegistration(it, courseRegistrationState(it, currentSemester, grades, feedbacks)))
                .collect(Collectors.toList());
    }

    private CourseRegistrationState courseRegistrationState(Course course, SemesterDto currentSemester, List<Grade> grades, List<Feedback> feedbacks) {
        Optional<Grade> grade = grades.stream().filter(it -> it.getCourse().equals(course)).findFirst();
        Optional<Feedback> feedback = feedbacks.stream().filter(it -> it.getCourse().equals(course)).findFirst();

        if (feedback.isPresent() && grade.isPresent()) {
            return grade.get().getMark().isPositive() ? CourseRegistrationState.complete_ok : CourseRegistrationState.complete_not_ok;
        } else if (feedback.isPresent()) {
            return CourseRegistrationState.needs_grade;
        } else if (grade.isPresent()) {
            return CourseRegistrationState.needs_feedback;
        } else {
            return course.getSemester().toDto().equals(currentSemester) ? CourseRegistrationState.in_progress : CourseRegistrationState.needs_feedback;
        }
    }
}
