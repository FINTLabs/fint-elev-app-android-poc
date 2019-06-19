package no.fint;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Door {
    private String name;
    private String doorID;
    private int pictureOfDoorID;
}
