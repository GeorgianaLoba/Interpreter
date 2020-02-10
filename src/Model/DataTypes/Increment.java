package Model.DataTypes;

public class Increment {
    private int empty;
    public Increment() {
        this.empty=0;
    }
    public int getEmpty(){
        this.empty+=1;
        return this.empty;
    }
}
