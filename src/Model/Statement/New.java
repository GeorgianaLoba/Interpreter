package Model.Statement;

import Model.DataTypes.Increment;
import Model.DataTypes.MyIDictionary;
import Model.Expression.Expression;
import Model.MyException;
import Model.State.ProgramState;
import Model.Type.ReferenceType;
import Model.Type.Type;
import Model.Value.ReferenceValue;
import Model.Value.Value;

import java.util.Map;

public class New implements IStatement {
    private String variable_name;
    private Expression expression;
    private Increment placeInHeap;

    public New(String variable_name, Expression expression, Increment placeInHeap){
        this.variable_name=variable_name;
        this.expression=expression;
        this.placeInHeap=placeInHeap;
    }

    @Override
    public String toString() {
        return "New (" + this.variable_name + "," + this.expression.toString()+ ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        if (symbolTable.isDefined(variable_name)){
            Value val = symbolTable.lookup(variable_name);
            if (val.getType() instanceof ReferenceType){
                ReferenceValue referenceVal = (ReferenceValue) val;
                ReferenceType referenceType = (ReferenceType) referenceVal.getType();
                Value expressionVal = this.expression.evaluate(symbolTable, heap);
                if (referenceType.getInner().equals(expressionVal.getType())){
                    int empty = this.placeInHeap.getEmpty();
                    heap.add(empty, expressionVal);
                    symbolTable.update(this.variable_name, new ReferenceValue(empty,referenceType.getInner()));
                    return null;
                }
                else{
                    throw new MyException("types don't match\n");
                }

            }
            else {
                throw new MyException("not a reference type\n");
            }
        }
        else{
            throw new MyException("not in symbol table\n");
        }
    }

    @Override
    public IStatement clone() throws CloneNotSupportedException {
        //TODO clone
        return null;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type typeVariable = typeEnvironment.get(variable_name);
        Type typeExpression = expression.typecheck(typeEnvironment);
        if (!(typeVariable.equals(new ReferenceType(typeExpression))))
            throw new MyException("New: right hand side and left hand side have diff types\n");
        return typeEnvironment;
    }
}
