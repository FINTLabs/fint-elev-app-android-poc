package no.fint;

import java.util.Date;

public class Student {
    private String firstName;
    private String lastName;
    private String birthDate;
    private String studentId;
    private School school;
    private int photoId;

    public Student(String firstName, String lastName, String birthDate, String studentId, School school, int photoId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.studentId = studentId;
        this.school = school;
        this.photoId = photoId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getStudentId() {
        return studentId;
    }

    public School getSchool() {
        return school;
    }

    public int getPhotoId() {
        return photoId;
    }
}
