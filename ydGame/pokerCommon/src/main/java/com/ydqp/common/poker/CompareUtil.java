package com.ydqp.common.poker;

import java.util.Comparator;

public class CompareUtil implements Comparator {
    /**
     * 排序类型 1：升序 -1：降序
     */
    private int compareType = 1;

    public CompareUtil(int compareType) {
        this.compareType = compareType;
    }

    public CompareUtil() {
    }

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Poker) {
            return compare((Poker) o1, (Poker) o2);
        }

        if (o1 instanceof Integer) {
            return compare((Integer) o1, (Integer) o2);
        }
        return 0;
    }


    private int compare(Poker poker1, Poker poker2) {
        int diffValue = poker1.getNum() - poker2.getNum();
        if (diffValue > 0) {
            return 1 * compareType;
        } else if (diffValue < 0) {
            return -1 * compareType;
        } else {
            if (poker1.getTag().compareTo(poker2.getTag()) > 0) {
                return 1 * compareType;
            } else {
                return -1 * compareType;
            }
        }
    }

    private int compare(Integer o1, Integer o2) {
        int diffValue = o1 - o2;
        if (diffValue > 0) {
            return 1 * compareType;
        } else if (diffValue < 0) {
            return -1 * compareType;
        }

        return 0;
    }

}
