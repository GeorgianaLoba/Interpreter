package Model.Value;

import Model.Type.ReferenceType;
import Model.Type.Type;

public class ReferenceValue implements Value {
    private int address;
    private Type locationType;

    public ReferenceValue(int address, Type locationType){
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return this.address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public Type getLocationType() {
        return this.locationType;
    }

    @Override
    public Type getType(){
        return new ReferenceType(this.locationType);
    }

    @Override
    public Object getValue() {
        return this.address;
        //TODO ???
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ReferenceValue clone = (ReferenceValue) super.clone();
        clone.address = this.address;
        clone.locationType = (Type) this.locationType.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "RefValue( "+ this.address + ","+ this.locationType.toString()+")";
    }
}
