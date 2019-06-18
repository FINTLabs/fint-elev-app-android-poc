package no.fint;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    private String userName;
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

    protected Student(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        birthDate = in.readString();
        studentId = in.readString();
        school = in.readParcelable(School.class.getClassLoader());
        photoId = in.readInt();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public Student() {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.birthDate);
        dest.writeString(this.studentId);
        dest.writeParcelable(this.school, flags);
        dest.writeInt(this.photoId);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
