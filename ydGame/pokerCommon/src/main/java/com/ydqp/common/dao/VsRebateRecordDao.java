package com.ydqp.common.dao;

import com.cfq.jdbc.JdbcOrm;

import java.util.Map;

public class VsRebateRecordDao {

    private VsRebateRecordDao() {
    }

    private static VsRebateRecordDao instance = new VsRebateRecordDao();

    public static VsRebateRecordDao getInstance() {
        return instance;
    }

    public void insert(Map<String, Object> parameterMap) {
        JdbcOrm.getInstance().insert("vs_rebate_record", parameterMap);
    }
}
