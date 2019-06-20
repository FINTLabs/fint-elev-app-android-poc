package no.fint;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Subject {
    private String name;
    private String startTime;
    private String endTime;
    private String teacherName;
}
