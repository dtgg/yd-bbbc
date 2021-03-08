package com.ydqp.common.task;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.utils.HttpUtils;
import com.ydqp.common.utils.StatUploadUrlUtil;

import java.util.HashMap;
import java.util.Map;

public class StatisticsUploadTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsUploadTask.class);

    private String type;

    private final JSONObject params;

    public StatisticsUploadTask(String type, JSONObject params) {
        this.type = type;
        this.params = params;
    }

    @Override
    public void run() {
        try {
            String url = StatUploadUrlUtil.getInstance().getUrl();
            logger.info("数据上报地址：{}", url);
            JSONObject data = new JSONObject() {{
                put("type", type);
                put("data", params);
            }};
            logger.info("上报数据：{}", data.toJSONString());
            Map<String, String> headParams = new HashMap<>();
            headParams.put("content-type", "application/json");
            String s = HttpUtils.getInstance().sendPost(url, headParams, data);
            logger.info("数据上报结果：{}", s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
