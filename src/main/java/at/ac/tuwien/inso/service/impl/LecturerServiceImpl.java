package at.ac.tuwien.inso.service.impl;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.repository.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class LecturerServiceImpl implements LecturerService {

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
        return lecturerRepository.findLecturerByAccountId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getOwnSubjects() {
        return subjectRepository.findByLecturers_Id(getLoggedInLecturer().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> findSubjectsFor(Lecturer lecturer) {
        return lecturer.getSubjects();
    }

    @Override
    public String generateQRUrl(Lecturer lecturer) throws UnsupportedEncodingException {
        String url = "otpauth://totp/" + APP_NAME
                + ":" + lecturer.getEmail()
                + "?secret=" + lecturer.getTwoFactorSecret()
                + "&issuer=" + APP_NAME + "";
        return QR_PREFIX + URLEncoder.encode(url, "UTF-8");
    }
}
