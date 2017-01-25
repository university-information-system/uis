package at.ac.tuwien.inso.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.inso.entity.Lecturer;
import at.ac.tuwien.inso.entity.Subject;
import at.ac.tuwien.inso.repository.LecturerRepository;
import at.ac.tuwien.inso.repository.SubjectRepository;
import at.ac.tuwien.inso.service.LecturerService;
import at.ac.tuwien.inso.service.UserAccountService;

@Service
public class LecturerServiceImpl implements LecturerService {
	
	private static final Logger log = LoggerFactory.getLogger(LecturerServiceImpl.class);


    public static String QR_PREFIX =
            "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME =
            "UIS";

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    @Transactional(readOnly = true)
    public Lecturer getLoggedInLecturer() {
        Long id = userAccountService.getCurrentLoggedInUser().getId();
        log.info("returning currently logged in lecturer with id "+id);
        return lecturerRepository.findLecturerByAccountId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getOwnSubjects() {
    	log.info("returning owned subjects for currently logged in lecturer");
        return subjectRepository.findByLecturers_Id(getLoggedInLecturer().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> findSubjectsFor(Lecturer lecturer) {
    	log.info("returning subjects for lecturer "+lecturer.toString());
        return subjectRepository.findByLecturers_Id(lecturer.getId());
    }

    @Override
    public String generateQRUrl(Lecturer lecturer) throws UnsupportedEncodingException {
    	log.info("generating QR url for lecturer with id "+lecturer.getId());
        String url = "otpauth://totp/" + APP_NAME
                + ":" + lecturer.getEmail()
                + "?secret=" + lecturer.getTwoFactorSecret()
                + "&issuer=" + APP_NAME + "";
        return QR_PREFIX + URLEncoder.encode(url, "UTF-8");
    }
}
