package com.ydqp.common.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PlayerPromoteDetail;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class PlayerPromoteDetailDao {

    private PlayerPromoteDetailDao() {
    }

    private static PlayerPromoteDetailDao instance;

    public static PlayerPromoteDetailDao getInstance() {
        if (instance == null)
            instance = new PlayerPromoteDetailDao();
        return instance;
    }

    public void insert(Map<String, Object> parameterMap) {
        JdbcOrm.getInstance().insert("player_promote_detail", parameterMap);
    }

    public List<PlayerPromoteDetail> findByPlayerIds(List<Long> playerPromoteIds) {
        String str = "";
        for (Long id : playerPromoteIds) {
            if (StringUtils.isBlank(str))
                str += id;
            else
                str += "," + id;
        }
        String sql = "select * from player_promote_detail where playerId in (" + str + ") order by id desc limit 100;";
        return JdbcOrm.getInstance().getListBean(sql, PlayerPromoteDetail.class);
    }
}
