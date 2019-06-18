package no.fint;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Student implements Parcelable {
    private String userName;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String studentId;
    private School school;
    private int photoId;

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
}
