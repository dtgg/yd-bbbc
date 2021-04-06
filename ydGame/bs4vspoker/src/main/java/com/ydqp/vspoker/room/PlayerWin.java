package com.ydqp.vspoker.room;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class PlayerWin {

    @Setter
    @Getter
    private Double winMoney = 0.0;

    @Setter
    @Getter
    private List<Integer> winTypes = new ArrayList<>();
}
