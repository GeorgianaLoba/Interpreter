package Model.DataTypes;

import Model.MyException;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyHeap<T1, T2> implements MyIDictionary<T1, T2> {
    private HashMap<T1, T2> heap;

    public MyHeap(){

        this.heap=new HashMap<T1, T2>();
    }

    @Override
    public  void add(T1 key, T2 value) throws MyException {
        try{
            Lock lock = new ReentrantLock();
            lock.lock();
            this.heap.put(key, value);
            lock.unlock();
        }
        catch(Exception e){
            throw new MyException("Unable to add into heap\n");
        }
    }

    @Override
    public synchronized void update(T1 key, T2 value) throws MyException {
        try{
            Lock lock = new ReentrantLock();
            lock.lock();
            this.heap.replace(key, value);
            lock.unlock();
        }
        catch(Exception e){
            throw new MyException("Specified key doesn't exist in the heap\n");
        }
    }

    @Override
    public synchronized T2 remove(T1 key) throws MyException {
        try{
            return this.heap.remove(key);
        }
        catch (Exception e){
            throw new MyException("Specified key doesn't exist in the heap\n");
        }
    }

    @Override
    public synchronized boolean isEmpty() {

        return this.heap.isEmpty();
    }

    @Override
    public synchronized T2 lookup(T1 key) throws MyException {
        try{
            Lock lock = new ReentrantLock();
            lock.lock();
            T2 el = this.heap.get(key);
            lock.unlock();
            return el;
        }
        catch (Exception e){
            throw new MyException("Specified key doesn't exist in the heap\n");
        }
    }

    @Override
    public synchronized boolean isDefined(T1 key) {
        return this.heap.containsKey(key);
    }

    @Override
    public synchronized Map<T1, T2> getContent() {
        return this.heap;
    }


    @Override
    public synchronized void setContent(Map<T1, T2> map) {
        this.heap= (HashMap<T1, T2>) map;
    }

    @Override
    public synchronized int size() {
        return this.heap.size();
    }

    @Override
    public synchronized String toString() {
        StringBuilder tipar = new StringBuilder();
        Iterator dictIterator = this.heap.entrySet().iterator();
        while (dictIterator.hasNext()){
            Map.Entry pair = (Map.Entry) dictIterator.next();
            tipar.append(pair.getKey().toString()+ " -> " + pair.getValue().toString() +"; ");
        }
        return tipar.toString();
    }
}
