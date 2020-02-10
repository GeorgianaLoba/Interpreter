package Model.Expression;

import Model.DataTypes.MyIDictionary;
import Model.MyException;
import Model.Type.BooleanType;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.BooleanValue;
import Model.Value.IntValue;
import Model.Value.Value;

import java.util.Map;

public class RelationalExpression implements Expression{
    private Expression first;
    private Expression second;
    private int operator; //1<, 2<=, 3==, 4!=, 5>, 6>=
    // ^ simplified this expression by making a convention, if I type 1, the interpreter
    // will now that i mean "<" and, if i type 3, the interpreter knows it's "=="
    // and so on with the rest of them

    public RelationalExpression(Expression first, Expression second, int operator){
        this.first = first;
        this.second = second;
        this.operator = operator;
    }

    @Override
    public Value evaluate(MyIDictionary<String, Value> table, MyIDictionary<Integer, Value> heap) throws MyException {
        Value firstV, secondV;
        firstV = this.first.evaluate(table, heap);
        secondV = this.second.evaluate(table, heap);
        boolean res = false;
        if (firstV.getType().equals(new IntType()) && secondV.getType().equals(new IntType())){
            int nr1, nr2;
            nr1 = ((IntValue)firstV).getValue();
            nr2 = ((IntValue)secondV).getValue();
            switch (this.operator){
                case 1:
                    if (nr1<nr2){ res=true;}
                    break;
                case 2:
                    if (nr1<=nr2){res=true;}
                    break;
                case 3:
                    if (nr1==nr2){res=true;}
                    break;
                case 4:
                    if (nr1!=nr2){res=true;}
                    break;
                case 5:
                    if (nr1>nr2){res=true;}
                    break;
                case 6:
                    if(nr1>=nr2){res=true;}
                    break;
            }
        }
        else{
            throw new MyException("types don't match/not int, can't compare\n");
        }
        return new BooleanValue(res);
    }


    @Override
    public String toString() {
        StringBuilder tipar = new StringBuilder();
        switch(this.operator){
            case 1:
                tipar.append(this.first.toString() + "<" + this.second.toString());
                break;
            case 2:
                tipar.append(this.first.toString() + "<="+ this.second.toString());
                break;
            case 3:
                tipar.append(this.first.toString() + "=="+ this.second.toString());
                break;
            case 4:
                tipar.append(this.first.toString() + "!=" + this.second.toString());
                break;
            case 5:
                tipar.append(this.first.toString() + ">" + this.second.toString());
                break;
            case 6:
                tipar.append(this.first.toString() + ">=" + this.second.toString());
                break;

        }
        return tipar.toString();
    }

    @Override
    public Expression clone() throws CloneNotSupportedException {
        RelationalExpression clone = (RelationalExpression) super.clone();
        clone.first=this.first.clone();
        clone.second = this.second.clone();
        clone.operator=this.operator;
        return clone;
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnvironment) throws MyException {
        Type type1, type2;
        type1=this.first.typecheck(typeEnvironment);
        type2=this.second.typecheck(typeEnvironment);
        if (!(type1.equals(new IntType())))
            throw new MyException("First operand not int\n");
        if (!(type2.equals(new IntType())))
            throw new MyException("Second operand not int\n");
        return new BooleanType();
    }
}
