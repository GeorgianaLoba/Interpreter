package Model.Expression;

import Model.DataTypes.MyIDictionary;
import Model.MyException;
import Model.Type.Type;
import Model.Value.Value;

import java.util.Map;

public class ValueExpression implements Expression {
    private Value val;
    public ValueExpression(Value val){this.val=val;}

    @Override
    public Value evaluate(MyIDictionary<String, Value> table, MyIDictionary<Integer, Value> heap) throws MyException {
        return this.val;
    }

    @Override
    public String toString() {
        return this.val.toString();
    }

    @Override
    public Expression clone() throws CloneNotSupportedException {
        ValueExpression clone = (ValueExpression)super.clone();
        clone.val= (Value) this.val.clone();
        return clone;
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnvironment) throws MyException {
        return this.val.getType();
    }
}
