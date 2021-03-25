package com.ydqp.common.dao.lottery;

import com.cfq.jdbc.JdbcOrm;

import java.util.Map;

public class DrawMethodDao {

    private DrawMethodDao() {}

    private static DrawMethodDao instance = new DrawMethodDao();

    public static DrawMethodDao getInstance() {
        return instance;
    }

    public void insert(Map<String, Object> map) {
        JdbcOrm.getInstance().insert("draw_method", map);
    }
}
