package at.ac.tuwien.inso.dto;

import at.ac.tuwien.inso.entity.StudyPlan;

public class StudyPlanRegistrationDto {

    private StudyPlan studyplan;

    private SemesterDto registeredSince;

	public StudyPlan getStudyplan() {
		return studyplan;
	}

	public void setStudyplan(StudyPlan studyplan) {
		this.studyplan = studyplan;
	}

	public SemesterDto getRegisteredSince() {
		return registeredSince;
	}

	public void setRegisteredSince(SemesterDto registeredSince) {
		this.registeredSince = registeredSince;
	}
    
    
    
}
