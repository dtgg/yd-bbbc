package com.ydqp.lobby.service.sms.data;

import lombok.Data;

/**
 * @Description: 发送短信参数
 * @Author: whw
 * @Date: 2020/06/07
 */
@Data
public class SMSParam {

    private String content;

    private String numbers;

    private String senderid;
}
