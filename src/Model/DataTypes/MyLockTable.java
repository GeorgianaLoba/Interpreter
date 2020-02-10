package Model.DataTypes;

import Model.MyException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyLockTable<T1,T2> implements MyIDictionary<T1, T2> {
    private ConcurrentHashMap<T1,T2> lockTable;
    static int freeLocation=1;

    public MyLockTable(){
        this.lockTable=new ConcurrentHashMap<>();
    }

    public synchronized static int getFreeLocation() {
        return freeLocation;
    }

    public synchronized static void moveFreeLocation(){
        Lock lock = new ReentrantLock();
        lock.lock();
        freeLocation+=1;
        lock.unlock();
    }

    @Override
    public void add(T1 key, T2 value) throws MyException {
        try{
            Lock lock = new ReentrantLock();
            lock.lock();
            this.lockTable.put(key, value);
            lock.unlock();

        }
        catch(Exception e){
            throw new MyException("Unable to add into heap\n");
        }
    }

    @Override
    public void update(T1 key, T2 value) throws MyException {
        try{
            Lock lock = new ReentrantLock();
            lock.lock();
            this.lockTable.replace(key, value);
            lock.unlock();
        }
        catch(Exception e){
            throw new MyException("Specified key doesn't exist in the heap\n");
        }
    }

    @Override
    public synchronized T2 remove(T1 key) throws MyException {
        try{
            return this.lockTable.remove(key);
        }
        catch (Exception e){
            throw new MyException("Specified key doesn't exist in the heap\n");
        }
    }

    @Override
    public synchronized boolean isEmpty() {
        return this.lockTable.isEmpty();

    }

    @Override
    public synchronized T2 lookup(T1 key) throws MyException {
        try{
            Lock lock = new ReentrantLock();
            lock.lock();
            T2 el = lockTable.get(key);
            lock.unlock();
            return el;

        }
        catch (Exception e){
            throw new MyException("Specified key doesn't exist in the heap\n");
        }
    }

    @Override
    public synchronized boolean isDefined(T1 key) {
        return this.lockTable.containsKey(key);
    }

    @Override
    public synchronized Map<T1, T2> getContent() {
        return this.lockTable;
    }

    @Override
    public synchronized void setContent(Map<T1, T2> map) {
        //todo
    }

    @Override
    public int size() {
        return this.lockTable.size();
    }
}
