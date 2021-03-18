package com.ydqp.common.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PaySuccessDeal;

import java.util.List;
import java.util.Map;

public class PaySuccessDealDao {

    private PaySuccessDealDao() {
    }

    private static PaySuccessDealDao instance = new PaySuccessDealDao();

    public static PaySuccessDealDao getInstance() {
        if (instance == null) instance = new PaySuccessDealDao();
        return instance;
    }

    private static final String dealPending = "update pay_success_deal set isDeal = 1 where id = ";
    public void setDealPending(long id) {
        String sql = dealPending + id + ";";
        JdbcOrm.getInstance().update(sql);
    }

    private static final String update = "update pay_success_deal set isDeal = 2 where id in ";
    public void setDealSuccess(String ids) {
        String sql = update + ids + ";";
        JdbcOrm.getInstance().update(sql);
    }

    private static final String findAllNotDeal = "select * from pay_success_deal where isDeal = 0;";
    public List<PaySuccessDeal> findAllNotDeal() {
        return JdbcOrm.getInstance().getListBean(findAllNotDeal, PaySuccessDeal.class);
    }

    public PaySuccessDeal getPaySuccessDeal(long id) {
        String sql = "select * from pay_success_deal where id = " + id + ";";
        PaySuccessDeal paySuccessDeal = (PaySuccessDeal) JdbcOrm.getInstance().getBean(sql, PaySuccessDeal.class);
        return paySuccessDeal;
    }

    public void save(Map<String, Object> parameterMap) {
        JdbcOrm.getInstance().insert("pay_success_deal", parameterMap);
    }
}
