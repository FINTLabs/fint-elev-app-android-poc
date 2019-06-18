package no.fint;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class Door {
    private String name;
    private String doorID;
    private int pictureOfDoorID;
}
