package Model.Statement;

import Model.DataTypes.MyIDictionary;
import Model.DataTypes.MyIStack;
import Model.Expression.Expression;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.BooleanType;
import Model.Type.Type;
import Model.Value.Value;

import java.util.HashMap;
import java.util.Map;

public class IfStatement implements IStatement{
    private Expression expression;
    private IStatement thenStatement;
    private IStatement elseStatement;

    public IfStatement(Expression expression, IStatement thenStatement, IStatement elseStatement){
        this.expression=expression;
        this.thenStatement=thenStatement;
        this.elseStatement=elseStatement;
    }

    @Override
    public String toString(){
        return "IF (" + expression.toString() + ") THEN ("+thenStatement.toString()+") ELSE (" +
                elseStatement.toString() +")";}

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> executionStack = state.getExecutionStack();
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        Value result = this.expression.evaluate(symbolTable, heap);
        if (result.getType() instanceof BooleanType) {
       //if (expression that can evaluate to true or false) doSomething else doSomethingElse
       //i place of the exection stack the correct statement based on the
       //evaluation of the expression
            if (result.getValue().equals(false)) {
                executionStack.push(elseStatement);
            } else {
                executionStack.push(thenStatement);
            }
        }
        else{
            throw new MyException("not a boolean, can't compare\n");
        }
        return null;
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        IfStatement clone = (IfStatement)super.clone();
        clone.expression= this.expression.clone();
        clone.thenStatement = this.thenStatement.clone();
        clone.elseStatement = this.elseStatement.clone();
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type typeExpression = this.expression.typecheck(typeEnvironment);
        if(!(typeExpression.equals(new BooleanType())))
            throw new MyException("The condition in the IF statement is not of type bool\n");
        Map<String, Type> cloneThen = new HashMap<>(typeEnvironment);
        Map<String, Type> cloneElse = new HashMap<>(typeEnvironment);
        thenStatement.typecheck(cloneThen);
        elseStatement.typecheck(cloneElse);
        return typeEnvironment;
    }
}
