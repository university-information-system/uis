package at.ac.tuwien.inso.service.impl;

import com.sun.istack.internal.Nullable;

import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    @Transactional
    public SemesterDto create(SemesterDto semester) {
        return semesterRepository.save(semester.toEntity()).toDto();
    }

    @Override
    @Transactional(readOnly = true)
    public SemesterDto getCurrentSemester() {
        Semester s = semesterRepository.findFirstByOrderByIdDesc();
        if(s!=null){
        	return s.toDto();
        }else{
        	return null;
        }
    }

    @Override
    public SemesterDto getOrCreateCurrentSemester() {
        Calendar now = new GregorianCalendar();
        return getOrCreateCurrentSemester(now);
    }

    /**
     * If now is still in the current semester, we return the semester
     *
     * Otherwise all semesters until now are created and the newest one returned.
     *
     * @param now date to compare with
     */
    public SemesterDto getOrCreateCurrentSemester(Calendar now) {
        SemesterDto current = getCurrentSemester();

        // Recursion, to create all missing semesters
        if (!current.isCurrent(now)) {
            create(current.nextSemester());
            return getOrCreateCurrentSemester();
        }

        return current;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterDto> findAll() {
        return convertSemesterListToSemesterDtoList(semesterRepository.findAllByOrderByIdDesc());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterDto> findAllSince(SemesterDto semester) {
        return convertSemesterListToSemesterDtoList(semesterRepository.findAllSince(semester.toEntity()));
    }
    
    private List<SemesterDto> convertSemesterListToSemesterDtoList(List<Semester> semesters){
    	List<SemesterDto> toReturn = new ArrayList<SemesterDto>();
        for(Semester s : semesters){
        	toReturn.add(s.toDto());
        }
        return toReturn;
    }
}
