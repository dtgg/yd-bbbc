package com.ydqp.vspoker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NumberPool {

    private static final NumberPool instance = new NumberPool(1, 70000);

    public static final NumberPool getInstance() {
        return instance;
    }

    private Lock lock = new ReentrantLock();

    private List<Integer> ids = new ArrayList<Integer>();

    public NumberPool(int start, int end) {
        for (int i = start; i < end; i++) {
            ids.add(i);
        }
    }

    /**
     * 获取id
     *
     * @return
     * @throws Exception
     */
    public int pop() throws Exception {
        lock.lock();
        try {
            return ids.remove(0);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 回收id
     *
     * @param value
     * @throws Exception
     */
    public void push(int value) throws Exception {
        lock.lock();
        try {
            ids.add(value);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            lock.unlock();
        }
    }
}
