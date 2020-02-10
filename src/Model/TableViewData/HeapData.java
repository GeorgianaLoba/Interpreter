package Model.TableViewData;

public class HeapData {
    private int address;
    private String value;

    public HeapData(int address, String value){
        this.address=address;
        this.value=value;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.address) + " " + this.value.toString();
    }
}
