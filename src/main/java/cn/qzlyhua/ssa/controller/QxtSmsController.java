package cn.qzlyhua.ssa.controller;

import cn.qzlyhua.ssa.annotation.response.ResponseResult;
import cn.qzlyhua.ssa.annotation.response.StandardResponse;
import cn.qzlyhua.ssa.utils.ThreadLocals;
import cn.qzlyhua.ssa.utils.qxt.QxtClient;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * @author qzlyhua
 * <p>
 * 短信适配程序-企信通
 */
@Controller
@StandardResponse
@Slf4j
@RequestMapping("/qxt")
public class QxtSmsController extends BaseController {
    @Value("${qxt.msgClient.ip}")
    private String ipAddress;
    @Value("${qxt.msgClient.port}")
    private String port;
    @Value("${qxt.msgClient.username}")
    private String username;
    @Value("${qxt.msgClient.password}")
    private String password;
    @Value("${qxt.msgClient.sign}")
    private String sign;

    private int count = 1;

    /**
     * 企信通短信发送-金坛短信平台
     *
     * @param param
     * @return
     */
    @RequestMapping("/send")
    @ResponseBody
    public ResponseResult sendMessageService(@RequestBody JSONObject param) throws UnsupportedEncodingException {
        SmsContent smsContent = check(param, sign);
        ThreadLocals.TIMES.set(count++);
        log.info("发送短信[{}]：\t{}\t{}", ThreadLocals.TIMES.get(), smsContent.getPhone(), smsContent.getContent());
        return QxtClient.sendSms(ipAddress, Integer.valueOf(port), username, password,
                smsContent.getPhone(), smsContent.getContent());
    }
}