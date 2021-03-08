package com.ydqp.common.utils;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.util.ReadProperties;
import com.cfq.util.StackTraceUtil;

import java.util.Map;

public class StatUploadUrlUtil {

    private static final Logger logger = LoggerFactory.getLogger(StatUploadUrlUtil.class);

    private StatUploadUrlUtil() {
        try {
            Map<String, String> propertiesValues = ReadProperties.getPropertiesValues("statupload.properties");
            url = propertiesValues.get("url");
        } catch (Exception e) {
            logger.error(StackTraceUtil.getStackTrace(e));
        }
    }

    private static final StatUploadUrlUtil instance = new StatUploadUrlUtil();

    public static StatUploadUrlUtil getInstance() {
        return instance;
    }

    private String url;


    public String getUrl(){
        return url;
    }
}
