package at.ac.tuwien.inso.service.impl;

//import com.sun.istack.internal.Nullable; this does not exist!

import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class SemesterServiceImpl implements SemesterService {
	
	private static final Logger log = LoggerFactory.getLogger(SemesterServiceImpl.class);

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    @Transactional
    public SemesterDto create(SemesterDto semester) {
    	log.info("creating semester "+semester.getLabel());
        return semesterRepository.save(semester.toEntity()).toDto();
    }

    @Override
    @Transactional(readOnly = true)
    public SemesterDto getCurrentSemester() {
        Semester s = semesterRepository.findFirstByOrderByIdDesc();
        
        if(s!=null){
        	log.info("returning current semester "+s.getLabel());
        	return s.toDto();
        }else{
        	log.warn("current semester is null");
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
     * Otherwise the current semester will be created and returned
     *
     * @param now date to compare with
     */
    public SemesterDto getOrCreateCurrentSemester(Calendar now) {
        SemesterDto current = getCurrentSemester();

        if (current == null || !current.isCurrent(now)) {
            // TODO logging!!!
            create(SemesterDto.calculateCurrentSemester(now));
        }

        return current;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterDto> findAll() {
    	log.info("finding all semsters");
        return convertSemesterListToSemesterDtoList(semesterRepository.findAllByOrderByIdDesc());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterDto> findAllSince(SemesterDto semester) {
    	log.info("finding all semesters since "+semester.getLabel());
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
