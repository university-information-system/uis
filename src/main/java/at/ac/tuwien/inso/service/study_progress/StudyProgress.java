package at.ac.tuwien.inso.service.study_progress;

import java.util.List;

import at.ac.tuwien.inso.dto.SemesterDto;

/**
 * Study progress for a student
 */
public class StudyProgress {

    private SemesterDto currentSemester;

    private List<SemesterProgress> semestersProgress;

    public StudyProgress(SemesterDto currentSemester, List<SemesterProgress> semestersProgress) {
        this.currentSemester = currentSemester;
        this.semestersProgress = semestersProgress;
    }

    public SemesterDto getCurrentSemester() {
        return currentSemester;
    }

    public List<SemesterProgress> getSemestersProgress() {
        return semestersProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyProgress that = (StudyProgress) o;

        if (getCurrentSemester() != null ? !getCurrentSemester().equals(that.getCurrentSemester()) : that.getCurrentSemester() != null)
            return false;
        return getSemestersProgress() != null ? getSemestersProgress().equals(that.getSemestersProgress()) : that.getSemestersProgress() == null;

    }

    @Override
    public int hashCode() {
        int result = getCurrentSemester() != null ? getCurrentSemester().hashCode() : 0;
        result = 31 * result + (getSemestersProgress() != null ? getSemestersProgress().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StudyProgress{" +
                "currentSemester=" + currentSemester +
                ", semestersProgress=" + semestersProgress +
                '}';
    }
}
