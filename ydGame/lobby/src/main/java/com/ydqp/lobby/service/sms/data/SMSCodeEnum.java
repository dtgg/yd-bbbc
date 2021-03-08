package com.ydqp.lobby.service.sms.data;

/**
 * 业务错误码枚举
 */
public enum SMSCodeEnum {
	SUCCESS(0, "获取成功"),
	AUTHENTICATION_ERROR(-1, "认证错误"),
	IP_ACCESS_RESTRICTED(-2, "Ip访问受限"),
	CONTAIN_SENSITIVE_CHARACTERS(-3, "短信内容含有敏感字符"),
	CONTENT_NULL(-4, "短信内容为空"),
	CONTENT_TOO_LONG(-5, "短信内容过长"),
	NOT_TEMPLATE(-6, "不是模板的短信"),
	NUMBERS_TOO_MANY(-7, "号码个数过多"),
	NUMBERS_NULL(-8, "号码为空"),
	NUMBERS_EXCEPTION(-9, "号码异常"),
	INSUFFICIENT_BALANCE(-10, "该通道余额不足，不能满足本次发送"),
	TIME_FORMAT_WRONG(-11, "定时时间格式不对"),
	PLATFORM_EXCEPTION(-12, "由于平台的原因，批量提交出错，请与管理员联系"),
	USER_LOCKED(-13, "用户被锁定");

	public final Integer code;
	public final String message;

	SMSCodeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public static String getMessage(int code) {
		for (SMSCodeEnum c : SMSCodeEnum.values()) {
			if (c.getCode() == code) {
				return c.message;
			}
		}
		return null;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
