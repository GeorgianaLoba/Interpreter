package Model.Type;

import Model.Value.IntValue;
import Model.Value.Value;

public class IntType implements Type{
    public IntType(){}

    @Override
    public boolean equals (Object another){
        if (another instanceof IntType){
            return true;
        }
        else{
            return false;
        }

    }

    @Override
    public Value defaultValue() {
        return new IntValue(0);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString(){
        return "int";
    }
}
