package Model.TableViewData;

public class LockData {
    private int location, value;

    public LockData(int location, int value){
        this.location=location;
        this.value=value;
    }


    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
