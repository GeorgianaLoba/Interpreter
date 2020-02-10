package Model.Value;

import Model.Type.IntType;
import Model.Type.Type;

public class IntValue implements Value<Integer>{
    private int value;

    public IntValue(int val){this.value=val;}

    @Override
    public boolean equals(Object another) {
        if (another instanceof IntValue){
            return this.value==((IntValue) another).getValue();
        }
        else{
            return false;
        }
    }

    @Override
    public Integer getValue(){
        return this.value;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        IntValue clone = (IntValue)super.clone();
        clone.value=this.value;
        return clone;
    }

    @Override
    public String toString(){
        return " " + this.value +" ";
    }
    @Override
    public Type getType() {
        return new IntType();
    }
}
