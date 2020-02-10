package Model.DataTypes;
import Model.MyException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MySymbolTable<T1, T2> implements MyIDictionary<T1, T2>{
    private HashMap<T1, T2> dict;

    public MySymbolTable(){
        dict = new HashMap<T1, T2>();
    }

    @Override
    public void add(T1 key, T2 value) throws MyException {
        try{
            dict.put(key, value);
        }
        catch (Exception e){
            throw new MyException("Can't add null keys and/or values into the dictionary");
        }
    }

    @Override
    public void update(T1 key, T2 value) throws MyException {
        try{
            dict.replace(key, value);
        }
        catch(Exception e){
            throw new MyException("Specified key doesn't exist in the dictionary");
        }
    }

    @Override
    public T2 remove(T1 key) throws MyException {
        try{
            return dict.remove(key);
        }
        catch (Exception e){
            throw new MyException("Specified key doesn't exist in the dictionary");
        }
    }

    @Override
    public boolean isEmpty() {
        return dict.isEmpty();
    }

    @Override
    public String toString(){
       /* BiConsumer<T1, T2> printing = new MyBiConsumer<T1, T2>();
        this.dictionary.forEach(printing);*/
       StringBuilder tipar = new StringBuilder();
       Iterator dictIterator = dict.entrySet().iterator();
       while (dictIterator.hasNext()){
           Map.Entry pair = (Map.Entry)dictIterator.next();
           tipar.append(pair.getKey().toString()+ " -> "+ pair.getValue().toString() + " ; " );
           //dictIterator.remove();
       }

        /*
       for (HashMap.Entry<T1,T2> element : this.dict.entrySet()){
           tipar.append("Key: " + element.getKey() + " Value: "+ element.getValue());
       } */
       return tipar.toString();
    }

    @Override
    public T2 lookup(T1 key) throws MyException {
        try{
            return dict.get(key);
        }
        catch (Exception e){
            throw new MyException("Specified key doesn't exist in the dictionary");
        }
    }

    @Override
    public boolean isDefined(T1 key) {
        if (dict.containsKey(key)) {
            return true;
        }
        else
            return false;
        //return this.dictionary.containsKey()
    }

    @Override
    public Map<T1, T2> getContent() {
        return this.dict;
    }


    @Override
    public void setContent(Map<T1, T2> map) {
        this.dict= (HashMap<T1, T2>) map;
    }

    @Override
    public int size() {
        return this.dict.size();
    }
}


/*
class MyBiConsumer<T1, T2> implements BiConsumer<T1, T2>{

    @Override
    public void accept(T1 t1, T2 t2) {
        System.out.println("Key: " + t1.toString() + " Value: " + t2.toString() + "\n");
    }
}*/
