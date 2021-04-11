package com.ydqp.common.poker;

import java.util.List;
import java.util.Map;

public interface ICardPoker {

    //生成一副牌
    List<Poker> generatePoker();

    //根据人数发牌
    Map<Integer, List<Poker>> faPai(List<Poker> pokerList, int num, int laizi);

}

