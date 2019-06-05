package no.fint;

public class Door {
    private String name;
    private String doorID;
    private int pictureOfDoor;

    public Door(String name, String doorID, int pictureOfDoor) {
        this.name = name;
        this.doorID = doorID;
        this.pictureOfDoor = pictureOfDoor;
    }

    public String getName() {
        return name;
    }

    public String getDoorID() {
        return doorID;
    }

    public int getPictureOfDoor() {
        return pictureOfDoor;
    }
}
