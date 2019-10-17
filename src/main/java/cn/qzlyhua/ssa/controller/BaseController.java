package cn.qzlyhua.ssa.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * 基类
 * 后续可增加更多短信适配环境，通过基类方法校验基础数据
 *
 * @author qzlyhua
 */
public class BaseController {
    protected SmsContent check(JSONObject params, String sign) throws UnsupportedEncodingException {
        String phone = params.getString("phone");
        String content = params.getString("content");
        String withOutSign = params.getString("withOutSign");

        Assert.hasText(phone, "发送号码不允许为空");
        Assert.hasText(content, "发送内容不允许为空");

        if (!StringUtils.isEmpty(sign) && StringUtils.isEmpty(withOutSign)) {
            content = sign + content;
        }

        int maxLength = 500;
        int length = content.getBytes("gbk").length;
        String msg = "发送内容长度为" + length + "，不允许超过" + maxLength + "个字节（" + maxLength / 2 + "个汉字）";
        Assert.isTrue(length <= maxLength, msg);

        return new SmsContent(phone, content);
    }

    @Data
    @AllArgsConstructor
    protected class SmsContent {
        private String phone;
        private String content;
    }
}
