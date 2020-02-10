package Model.DataTypes;

import Model.MyException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyFileTable<T1, T2> implements MyIDictionary<T1, T2> {
    private HashMap<T1, T2> fileTable;
    public MyFileTable(){
        this.fileTable=new HashMap<T1, T2>();
    }
    @Override
    public void add(T1 key, T2 value) throws MyException {
        try{
            this.fileTable.put(key, value);
        }
        catch (Exception e){
            throw new MyException("Can't add null keys and/or values into the dictionary");
        }
    }

    @Override
    public void update(T1 key, T2 value) throws MyException {
        try{
            this.fileTable.replace(key, value);
        }
        catch(Exception e){
            throw new MyException("Specified key doesn't exist in the dictionary");
        }
    }

    @Override
    public T2 remove(T1 key) throws MyException {
        try{
            return this.fileTable.remove(key);
        }
        catch (Exception e){
            throw new MyException("Specified key doesn't exist in the dictionary");
        }
    }

    @Override
    public boolean isEmpty() {
        return this.fileTable.isEmpty();
    }

    @Override
    public T2 lookup(T1 key) throws MyException {
        try{
            return this.fileTable.get(key);
        }
        catch (Exception e){
            throw new MyException("Specified key doesn't exist in the dictionary");
        }    }

    @Override
    public boolean isDefined(T1 key) {
        return this.fileTable.containsKey(key);
    }

    @Override
    public Map<T1, T2> getContent() {
        return this.fileTable;
    }

    @Override
    public void setContent(Map<T1, T2> map) {
        this.fileTable= (HashMap<T1, T2>) map;
    }

    @Override
    public int size() {
        return this.fileTable.size();
    }

    @Override
    public String toString() {
        StringBuilder tipar = new StringBuilder();
        Iterator dictIterator = this.fileTable.entrySet().iterator();
        while (dictIterator.hasNext()){
            Map.Entry pair = (Map.Entry)dictIterator.next();
            tipar.append(pair.getKey().toString()+ " -> "+ pair.getValue().toString() + "; " );
        }
        return tipar.toString();
    }
}
