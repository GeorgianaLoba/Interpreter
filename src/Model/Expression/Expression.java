package Model.Expression;

import Model.DataTypes.MyIDictionary;
import Model.MyException;

import Model.Type.Type;
import Model.Value.Value;

import java.util.Map;

public interface Expression {
    Value evaluate(MyIDictionary<String, Value> table, MyIDictionary<Integer, Value> heap) throws MyException;
    Expression clone() throws CloneNotSupportedException;
    Type typecheck(Map<String, Type> typeEnvironment) throws MyException;
}
