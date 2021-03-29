package com.ydqp.common.dao.lottery;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.Lottery;

import java.util.List;
import java.util.Map;

public class LotteryDao {

    private LotteryDao() {
    }

    private static LotteryDao instance;

    public static LotteryDao getInstance() {
        if (instance == null)
            instance = new LotteryDao();
        return instance;
    }

    public List<Lottery> findLastLottery(String types, int nowTime, int limit) {
//        String sql = "select * from lottery where status = 2 and type in " + types + " order by createTime desc limit " + limit + ";";
        int time1 = nowTime - 180;
        int time2 = nowTime - 360;
        String sql = "select * from lottery where type in "+types+" and "+time1+" >= createTime and createTime > "+time2+" order by createTime asc limit "+limit+";";
        return JdbcOrm.getInstance().getListBean(sql, Lottery.class);
    }

    public List<Lottery> findCurrentLottery(String types, int nowTime, int limit) {
//        String sql = "select * from lottery where status != 2 and type in " + types + " order by createTime asc limit " + limit + ";";
        int time = nowTime - 180;
        String sql = "select * from lottery where type in "+types+" and "+nowTime+" >= createTime and createTime > "+time+" order by createTime asc limit "+limit+";";
        return JdbcOrm.getInstance().getListBean(sql, Lottery.class);
    }

    public List<Lottery> findNextLottery(String types, int nowTime, int limit) {
//        String sql = "select * from lottery where status = 0 and type in " + types + " order by createTime asc limit " + limit + ";";
        int time = nowTime + 180;
        String sql = "select * from lottery where type in "+types+" and "+time+" >= createTime and createTime > "+nowTime+" order by createTime asc limit "+limit+";";
        return JdbcOrm.getInstance().getListBean(sql, Lottery.class);
    }

    public void insert(Map<String, Object> param) {
        JdbcOrm.getInstance().insert("lottery", param);
    }

    public List<Lottery> findByStatus(int status, int createTime) {
        String sql = "select * from lottery where status = " + status + " and createTime <= " + createTime + ";";
        return JdbcOrm.getInstance().getListBean(sql, Lottery.class);
    }

    public void batchUpdate(Object[][] lotteryParams) {
        String sql = "update lottery set price = ?, number = ?, status = ?, openTime = ?, totalPay = ?, totalAward = ?, " +
                "totalProfit = ?, totalFee = ? where id = ?;";
        JdbcOrm.getInstance().batchUpdate(sql, lotteryParams);
    }

    public void batchUpdateStatus(Object[][] lotteryParams) {
        String sql = "update lottery set status = ? where id = ?;";
        JdbcOrm.getInstance().batchUpdate(sql, lotteryParams);
    }

    public Lottery findById(int lotteryId) {
        String sql = "select * from lottery where id = " + lotteryId + ";";
        return (Lottery) JdbcOrm.getInstance().getBean(sql, Lottery.class);
    }

    public void batchInsert(String condition) {
        String sql = "insert into lottery (type, period, price, number, status, createTime, openTime) values " + condition + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public List<Lottery> page(int type, int offset, int limit, int dateTime) {
        String sql = "select * from lottery where status = 2 and type = " + type + " and createTime >= " + dateTime
                + " order by id desc limit " + offset + "," + limit + ";";
        return JdbcOrm.getInstance().getListBean(sql, Lottery.class);
    }

    public Total count(int type, int dateTime) {
        String sql = "select count(1) as total from lottery where status = 2 and type = " + type + " and createTime >= " + dateTime + ";";
        return (Total) JdbcOrm.getInstance().getBean(sql, Total.class);
    }

    public Lottery findLast() {
        String sql = "select * from lottery where status = 0 order by createTime desc limit 1;";
        return (Lottery) JdbcOrm.getInstance().getBean(sql, Lottery.class);
    }

    public Lottery findNowLottery() {
        String sql = "select * from lottery where type = 1 and status = 1 order by createTime desc limit 1;";
        return (Lottery) JdbcOrm.getInstance().getBean(sql, Lottery.class);
    }

    public List<Lottery> findCompleteLotteryBy24H(int type,int createTime) {
        String sql = "select * from lottery where type = " + type + " and status = 2 and createTime >= " + createTime + " order by createTime asc;";
        return JdbcOrm.getInstance().getListBean(sql, Lottery.class);
    }

    public List<Lottery> findRacingLotteries(String types, int time) {
        String sql = "select * from lottery where type in " + types + " and createTime > " + time + " order by id asc limit 4;";
        return JdbcOrm.getInstance().getListBean(sql, Lottery.class);
    }

    public List<Map<String, Object>> getHotNum() {
        String sql = "SELECT type, number, COUNT(number) AS count FROM lottery WHERE number IS NOT NULL GROUP BY type,number ORDER BY count desc;";
        return JdbcOrm.getInstance().getListBean(sql, List.class);
    }
}
