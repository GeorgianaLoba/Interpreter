package Model.Value;

import Model.Type.StringType;
import Model.Type.Type;

public class StringValue implements Value {
    private String value;

    public StringValue(String val){
        this.value=val;
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof StringValue){
            return this.value.equals(((StringValue) another).getValue());
        }
        else{
            return false;
        }
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        StringValue clone = (StringValue) super.clone();
        clone.value = this.value;
        return clone;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
