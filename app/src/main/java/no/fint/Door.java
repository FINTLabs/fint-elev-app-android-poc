package no.fint;

public class Door {
    private String name;
    private String doorID;
    private int pictureOfDoorID;

    public Door(String name, String doorID, int pictureOfDoorID) {
        this.name = name;
        this.doorID = doorID;
        this.pictureOfDoorID = pictureOfDoorID;
    }

    public String getName() {
        return name;
    }

    public String getDoorID() {
        return doorID;
    }

    public int getPictureOfDoorID() {
        return pictureOfDoorID;
    }
}
