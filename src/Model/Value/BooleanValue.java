package Model.Value;

import Model.Type.Type;
import Model.Type.BooleanType;

public class BooleanValue implements Value<Boolean> {
    private boolean value;

    public BooleanValue(boolean val){this.value=val;}

    public Boolean getValue(){return this.value;}

    @Override
    public Object clone() throws CloneNotSupportedException {
        BooleanValue clone = (BooleanValue)super.clone();
        clone.value=this.value;
        return clone;
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof BooleanValue){
            return this.value==((BooleanValue) another).getValue();
        }
        else{
            return false;
        }
    }

    @Override
    public String toString(){return " "+ this.value +" ";}

    @Override
    public Type getType() {
        return new BooleanType();
    }
}
