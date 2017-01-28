package at.ac.tuwien.inso.service.impl;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.sun.istack.internal.Nullable; this does not exist!

import at.ac.tuwien.inso.dto.SemesterDto;
import at.ac.tuwien.inso.entity.Semester;
import at.ac.tuwien.inso.repository.SemesterRepository;
import at.ac.tuwien.inso.service.SemesterService;

@Service
public class SemesterServiceImpl implements SemesterService {

    private static final Logger log = LoggerFactory.getLogger(SemesterServiceImpl.class);

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private Clock clock;

    @Override
    @Transactional
    public SemesterDto create(SemesterDto semester) {
        log.info("creating semester " + semester.getLabel());
        return semesterRepository.save(semester.toEntity()).toDto();
    }

    @Override
    @Transactional(readOnly = true)
    public SemesterDto getCurrentSemester() {
        Semester semester = semesterRepository.findFirstByOrderByIdDesc();

        if (semester != null){
            log.info("returning current semester " + semester.getLabel());
            return semester.toDto();
        } else {
            log.warn("current semester is null");
            return null;
        }
    }

    @Override
    public SemesterDto getOrCreateCurrentSemester() {
        LocalDateTime localDateTime = LocalDateTime.now(clock);
        Calendar now = GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
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
            return create(SemesterDto.calculateCurrentSemester(now));
        }

        return current;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterDto> findAll() {
        log.info("finding all semesters");
        return convertSemesterListToSemesterDtoList(semesterRepository.findAllByOrderByIdDesc());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterDto> findAllSince(SemesterDto semester) {
        log.info("finding all semesters since " + semester.getLabel());
        return convertSemesterListToSemesterDtoList(semesterRepository.findAllSince(semester.toEntity()));
    }
    
    private List<SemesterDto> convertSemesterListToSemesterDtoList(List<Semester> semesters){
        List<SemesterDto> toReturn = new ArrayList<>();
        for(Semester s : semesters){
            toReturn.add(s.toDto());
        }
        return toReturn;
    }
}
