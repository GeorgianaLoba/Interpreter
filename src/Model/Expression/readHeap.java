package Model.Expression;

import Model.DataTypes.MyIDictionary;
import Model.MyException;
import Model.Type.ReferenceType;
import Model.Type.Type;
import Model.Value.ReferenceValue;
import Model.Value.Value;

import java.util.Map;

public class readHeap implements Expression {
    private Expression expr;

    public readHeap(Expression expr){
        this.expr=expr;
    }

    @Override
    public String toString() {
        return "readHeap ("+ this.expr.toString() +")";
    }

    @Override
    public Value evaluate(MyIDictionary<String, Value> table, MyIDictionary<Integer, Value> heap) throws MyException {
        Value evaluation = this.expr.evaluate(table, heap);
        if (!(evaluation.getType() instanceof ReferenceType)){
            throw new MyException("not a reference value\n");
        }
        ReferenceValue refVal = (ReferenceValue) evaluation;
        int address = refVal.getAddress();

        if (!(heap.isDefined(address))){
            throw new MyException("not in heap\n");
        }

        return heap.lookup(address);
    }

    @Override
    public Expression clone() throws CloneNotSupportedException {
        readHeap clone = (readHeap) super.clone();
        clone.expr = this.expr;
        return clone;
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type type = this.expr.typecheck(typeEnvironment);
        if (!(type instanceof ReferenceType))
            throw new MyException("Not a reference type\n");
        ReferenceType refType = (ReferenceType)type;
        return refType.getInner();
    }
}
