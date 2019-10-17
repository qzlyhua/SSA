package cn.qzlyhua.ssa.controller;

import cn.qzlyhua.ssa.annotation.response.StandardResponse;
import cn.qzlyhua.ssa.utils.ThreadLocals;
import cn.qzlyhua.ssa.utils.ws.WsClient;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * 卫宁 webservice 短信适配程序
 *
 * @author qzlyhua
 */
@Controller
@StandardResponse
@Slf4j
@RequestMapping("/winning")
public class WsSmsController extends BaseController {
    @Value("${webservice.msgService.address}")
    private String wsAddress;
    @Value("${webservice.msgService.username}")
    private String userName;
    @Value("${webservice.msgService.password}")
    private String userPassword;
    @Value("${webservice.msgService.ssxt}")
    private String ssxt;
    @Value("${webservice.msgService.ywmkdm}")
    private String ywmkdm;
    @Value("${webservice.msgService.sign}")
    private String sign;

    private int count = 1;

    /**
     * 短信发送-webservice短信平台
     *
     * @param param
     */
    @RequestMapping("/send")
    @ResponseBody
    public Object sendSms(@RequestBody JSONObject param) throws UnsupportedEncodingException {
        SmsContent smsContent = check(param, sign);
        ThreadLocals.TIMES_LH.set(count++);
        log.info("发送短信[{}]：\t{}\t{}", ThreadLocals.TIMES_LH.get(), smsContent.getPhone(), smsContent.getContent());
        return WsClient.invoke(wsAddress, "sendMsgService",
                getXML(smsContent.getPhone(), smsContent.getContent()));
    }

    /**
     * webservice入参组装
     */
    private String getXML(String phone, String content) {
        StringBuilder sb = new StringBuilder();
        sb.append("<param>");
        sb.append("<baseInfo>");
        sb.append("<rybm>0123456789</rybm>");
        sb.append("<rylb>1</rylb>");
        sb.append("<ryxm>超级管理员</ryxm>");
        sb.append("<csrq>1987-12-12</csrq>");
        sb.append("<xbdm>1</xbdm>");
        sb.append("<xbmc>男</xbmc>");
        sb.append("<yljgdm>1222222222</yljgdm>");
        sb.append("</baseInfo>");
        sb.append("<msgInfos userName=\"");
        sb.append(userName);
        sb.append("\" userPassword=\"");
        sb.append(userPassword);
        sb.append("\" ssxt=\"");
        sb.append(ssxt);
        sb.append("\" ywmkdm=\"");
        sb.append(ywmkdm);
        sb.append("\">");
        sb.append("<msgInfo>");
        sb.append("<sendType>1</sendType>");
        sb.append("<recPhone>");
        sb.append(phone);
        sb.append("</recPhone>");
        sb.append("<msgContent>");
        sb.append(content);
        sb.append("</msgContent>");
        sb.append("<isNeedReport>1</isNeedReport>");
        sb.append("</msgInfo>");
        sb.append("</msgInfos>");
        sb.append("</param>");
        return sb.toString();
    }
}
