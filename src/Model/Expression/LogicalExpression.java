package Model.Expression;

import Model.DataTypes.MyIDictionary;
import Model.MyException;
import Model.Type.Type;
import Model.Value.BooleanValue;
import Model.Value.Value;

import java.util.Map;

public class LogicalExpression implements Expression {
    private Expression expression1;
    private Expression expression2;
    private int operator; //1=, 2!=, 3>, 4>=, 5<, 6<=, 7&&, 8||

    public LogicalExpression(Expression e1, Expression e2, int op){
        this.expression1=e1;
        this.expression2=e2;
        this.operator=op;
    }

    @Override
    public Value evaluate(MyIDictionary<String, Value> table, MyIDictionary<Integer, Value> heap) throws MyException {
        Value firstV, secondV;
        firstV=this.expression1.evaluate(table, heap);
        secondV=this.expression2.evaluate(table, heap);
        boolean r = false;
        //TODO evaluate in logic(haven't used this one so far, will implement eventually)
        switch(this.operator){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
        }
        return new BooleanValue(r);
    }

    @Override
    public Expression clone() throws CloneNotSupportedException {
        return null;
        //TODO clone in logic
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnvironment) throws MyException {
        return null;
    }
}
