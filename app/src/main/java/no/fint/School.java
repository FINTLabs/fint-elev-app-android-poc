package no.fint;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class School implements Parcelable {
    private String schoolId;
    private String schoolName;

    protected School(Parcel in) {
        schoolId = in.readString();
        schoolName = in.readString();
    }

    public static final Creator<School> CREATOR = new Creator<School>() {
        @Override
        public School createFromParcel(Parcel in) {
            return new School(in);
        }

        @Override
        public School[] newArray(int size) {
            return new School[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.schoolId);
        dest.writeString(this.schoolName);
    }
}
