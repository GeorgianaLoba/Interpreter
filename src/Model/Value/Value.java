package Model.Value;

import Model.Type.Type;

public interface Value<T> {
    Type getType();
    T getValue();
    Object clone() throws CloneNotSupportedException;
}
