package Model.Type;

import Model.Value.ReferenceValue;
import Model.Value.Value;

public class ReferenceType implements Type{
    private Type inner;

    public ReferenceType(Type inner){ this.inner = inner;}

    public Type getInner(){return this.inner;}

    @Override
    public boolean equals(Object another) {
        if (another instanceof ReferenceType){
            return this.inner.equals(((ReferenceType) another).getInner());
        }
        else{
            return false;
        }
    }

    @Override
    public String toString() { return "Ref(" + inner.toString() + ")";}

    @Override
    public Value defaultValue() {
        return new ReferenceValue(0, inner);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ReferenceType clone = (ReferenceType) super.clone();
        clone.inner = (Type) this.inner.clone();
        return clone;
    }
}
