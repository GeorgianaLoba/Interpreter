package Model.Expression;

import Model.DataTypes.MyIDictionary;
import Model.MyException;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;

import java.util.Map;

public class ArithmeticExpression implements Expression{
    private Expression first;
    private Expression second;
    private int operator; //1+, 2-, 3*, 4/
    //you have 2 expressions and in evaluate you basically computer the sum, diff and so on between them

    public ArithmeticExpression(int operator,Expression first, Expression second){
        //this class gets (in cunstructor) 2 expressions and an operator
        this.first=first;
        this.second=second;
        this.operator=operator;
    }

    @Override
    public Value evaluate(MyIDictionary<String, Value> table, MyIDictionary<Integer, Value> heap) throws MyException {
        Value firstV, secondV;
        firstV = this.first.evaluate(table, heap);
        secondV = this.second.evaluate(table, heap);
        int r = 0;
        if (firstV.getType().equals(new IntType()) && secondV.getType().equals(new IntType())) {
            int nr1, nr2;
            nr1 = ((IntValue) firstV).getValue();
            nr2 = ((IntValue) secondV).getValue();
            switch (this.operator) {
                case 1:
                    r = nr1 + nr2;
                    break;
                case 2:
                    r = nr1 - nr2;
                    break;
                case 3:
                    r = nr1 * nr2;
                    break;
                case 4:
                    if (nr2 == 0)
                        throw new MyException("division by zero");
                    else
                        r = nr1 / nr2;
                    break;
            }
        } else {
            throw new MyException("operands not int");
        }
        return new IntValue(r);
    }

    @Override
    public String toString() {
        StringBuilder tipar = new StringBuilder();
        switch(this.operator){
            case 1:
                tipar.append(this.first.toString() + "+" + this.second.toString());
                break;
            case 2:
                 tipar.append(this.first.toString() + "-"+this.second.toString());
                 break;
            case 3:
                tipar.append(this.first.toString() + "*"+this.second.toString());
                break;
            case 4:
                tipar.append(this.first.toString() + "/" + this.second.toString());
                break;
        }
        return tipar.toString();
    }


    @Override
    public Expression clone() throws CloneNotSupportedException {
        ArithmeticExpression clone = (ArithmeticExpression) super.clone();
        clone.first = this.first.clone();
        clone.second = this.second.clone();
        clone.operator=this.operator;
        return clone;
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type type1, type2;
        type1 = this.first.typecheck(typeEnvironment);
        type2 = this.second.typecheck(typeEnvironment);
        if (!(type1.equals(new IntType())))
            throw new MyException("First operand is not integer\n");
        if (!(type2.equals(new IntType())))
            throw new MyException("Second operand is not integer\n");
        return new IntType();
    }
}
