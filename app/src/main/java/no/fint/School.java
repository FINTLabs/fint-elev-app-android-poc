package no.fint;

public class School {
    private String schoolId;
    private String schoolName;

    public School(String schoolId, String schoolName) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }
}
