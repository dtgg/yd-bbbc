package com.ydqp.common.poker.vspoker;

import com.ydqp.common.poker.ICardPoker;
import com.ydqp.common.poker.Poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class VsPokerCard implements ICardPoker {
    @Override
    public List<Poker> generatePoker() {
        List<Poker> rs = new ArrayList<Poker>();
        List<String> tags = new ArrayList<String>();

        tags.add("A");
        tags.add("B");
        tags.add("C");
        tags.add("D");

        for (int n = 2; n <= 14; n++) {
            for (String t : tags) {
                Poker p = new Poker(t, n);
                rs.add(p);
            }
        }
        Collections.shuffle(rs);
        // 分成再堆
        List<Poker> rs1 = new ArrayList<Poker>();
        List<Poker> rs2 = new ArrayList<Poker>();
        int onePartSize = rs.size() / 2;
        for (Poker aPoker : rs) {
            Poker pSecond = new Poker(aPoker.getTag(), aPoker.getNum());
            if (rs1.size() < onePartSize) {
                rs1.add(pSecond);
            } else {
                rs2.add(pSecond);
            }
        }

        // 平均穿插
        if (rs1.size() < rs2.size()) {
            onePartSize = rs2.size();
        } else {
            onePartSize = rs1.size();
        }
        List<Poker> rsFinal = new ArrayList<Poker>();
        for (int thirdIndex = 0; thirdIndex < onePartSize; thirdIndex++) {
            if (thirdIndex < rs1.size()) {
                rsFinal.add(new Poker(rs1.get(thirdIndex).getTag(), rs1.get(thirdIndex).getNum()));
            }
            if (thirdIndex < rs2.size()) {
                rsFinal.add(new Poker(rs2.get(thirdIndex).getTag(), rs2.get(thirdIndex).getNum()));
            }
        }

        return rsFinal;
    }

    @Override
    public Map<Integer, List<Poker>> faPai(List<Poker> pokerList, int num, int laizi) {
        return null;
    }
}
