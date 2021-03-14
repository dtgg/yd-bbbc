package com.ydqp.common.lottery.player;

import java.util.ArrayList;
import java.util.List;

public class ManageLotteryRoom {

    private ManageLotteryRoom() {}

    private static ManageLotteryRoom instance;

    public static ManageLotteryRoom getInstance() {
        return instance;
    }

    public Integer getRoomId(int lotteryType) {
        if (lotteryType < 5) return 5000001;
        else return 5000002;
    }

    public List<Integer> getType(int roomId) {
        if (roomId == 5000001) {
            return new ArrayList<Integer>() {{
                add(1);add(2);add(3);add(4);
            }};
        } else {
            return new ArrayList<Integer>() {{
                add(5);add(6);add(7);add(8);
            }};
        }
    }
}
