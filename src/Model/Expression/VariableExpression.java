package Model.Expression;

import Model.DataTypes.MyIDictionary;
import Model.MyException;
import Model.Type.Type;
import Model.Value.Value;

import java.util.Map;

public class VariableExpression implements Expression {
    private String id;
    public VariableExpression (String id){this.id=id;}

    @Override
    public Value evaluate(MyIDictionary<String, Value> table, MyIDictionary<Integer, Value> heap) throws MyException {
        return table.lookup(this.id); //will return the value that has a certain id (looking into symbol table)
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public Expression clone() throws CloneNotSupportedException {
        VariableExpression clone = (VariableExpression) super.clone();
        clone.id=this.id;
        return clone;
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnvironment) throws MyException {
        return typeEnvironment.get(id);
    }
}
