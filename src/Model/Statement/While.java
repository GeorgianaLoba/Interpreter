package Model.Statement;

import Model.DataTypes.MyIDictionary;
import Model.DataTypes.MyIStack;
import Model.Expression.Expression;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.BooleanType;
import Model.Type.Type;
import Model.Value.BooleanValue;
import Model.Value.Value;

import java.util.HashMap;
import java.util.Map;

public class While implements IStatement {
    private Expression expr;
    private IStatement statement;

    public While(Expression expr, IStatement statement){
        this.expr=expr;
        this.statement = statement;
    }

    @Override
    public String toString() {
        return "While ("+this.expr.toString()+") "+this.statement.toString();
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        MyIStack<IStatement> stack = state.getExecutionStack();
        Value v = this.expr.evaluate(symbolTable, heap);
        if (v.getType().equals(new BooleanType())){
            BooleanValue boolV = (BooleanValue)v;
            if (boolV.getValue()==true){
                stack.push(this);
                stack.push(statement);
                return null;
            }
        }
        else{
            throw new MyException("Expression not bool\n");
        }
        return state;
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        While clone = (While) super.clone();
        clone.expr = (Expression)this.expr.clone();
        clone.statement = (IStatement)this.statement.clone();
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type typeExpr = this.expr.typecheck(typeEnvironment);
        if (!(typeExpr.equals(new BooleanType())))
            throw new MyException("The condition of While is not boolean\n");
        Map<String,Type> cloned = new HashMap<>(typeEnvironment);
        this.statement.typecheck(cloned);
        return typeEnvironment;
    }
}
