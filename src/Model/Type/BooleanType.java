package Model.Type;

import Model.Value.BooleanValue;
import Model.Value.Value;

public class BooleanType implements Type {
    public BooleanType(){};

    @Override
    public boolean equals (Object another){
        if (another instanceof BooleanType){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Value defaultValue() {
        return new BooleanValue(false);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString(){
        return "boolean";
    }
}
