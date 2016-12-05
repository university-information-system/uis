package at.ac.tuwien.inso.service.study_progress;

import at.ac.tuwien.inso.entity.*;

import java.util.*;

public class StudyProgress {

    private Semester currentSemester;

    private List<SemesterProgress> semestersProgress;

    public StudyProgress(Semester currentSemester, List<SemesterProgress> semestersProgress) {
        this.currentSemester = currentSemester;
        this.semestersProgress = semestersProgress;
    }

    public Semester getCurrentSemester() {
        return currentSemester;
    }

    public List<SemesterProgress> getSemestersProgress() {
        return semestersProgress;
    }
}
