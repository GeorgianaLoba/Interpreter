package Model.Statement;


import Model.DataTypes.MyIDictionary;
import Model.DataTypes.MyIStack;
import Model.Expression.Expression;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.Type;
import Model.Value.Value;

import java.util.Map;

public class AssignmentStatement implements IStatement {
    private String id;
    private Expression expression;

    public AssignmentStatement(String id, Expression expression){
        this.id=id;
        this.expression=expression;
    }

    @Override
    public String toString(){
        return this.id + "=" + this.expression.toString();
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> stack = state.getExecutionStack();
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        Value expressionValue = expression.evaluate(symbolTable, heap);

        if (symbolTable.isDefined(id)){
            Type variableType = (symbolTable.lookup(id)).getType();
            if (expressionValue.getType().equals(variableType)){

                symbolTable.update(id, expressionValue);
                //i simply assign to a specific id (say v or a it doesn't matter) a value
                //since the statement will look like: int a, a=3
                //first it's computed "int a" that places in my symbol table a tuple ("a", 0)
                //therefore when i get to "a=3", when performing the assignment statement
                //i just update the symbol table with the specifil value => ("a", 3)
            }
            else{
                throw new MyException("no match\n");
            }
        }
        else{
            throw new MyException("the used variable was not declared before\n");
        }
        return null;
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        AssignmentStatement clone = (AssignmentStatement)super.clone();
        clone.id = this.id;
        clone.expression = this.expression.clone();
        return clone;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type typeVariable = typeEnvironment.get(id);
        Type typeExpression = this.expression.typecheck(typeEnvironment);
        if(!(typeVariable.equals(typeExpression)))
            throw new MyException("Assignment: right hand side and left hand side have diff types\n");
        return typeEnvironment;
    }


}
