package cn.qzlyhua.ssa.utils.qxt;

import cn.qzlyhua.ssa.annotation.response.AppException;
import cn.qzlyhua.ssa.annotation.response.ResponseCode;
import cn.qzlyhua.ssa.annotation.response.ResponseResult;
import cn.qzlyhua.ssa.utils.SpringUtils;
import cn.qzlyhua.ssa.utils.ThreadLocals;
import com.linkage.netmsg.NetMsgclient;
import lombok.extern.slf4j.Slf4j;

/**
 * 企信通客户端
 *
 * @author qzlyhua
 */
@Slf4j
public class QxtClient {
    private static QxtClientFactory factory;

    /**
     * 企信通短信发送方法
     *
     * @param ip
     * @param port
     * @param username
     * @param passwrord
     * @param phone
     * @param content
     * @return
     * @throws RuntimeException
     */
    public static ResponseResult sendSms(String ip, int port, String username, String passwrord, String phone, String content) throws RuntimeException {
        if (factory == null) {
            factory = SpringUtils.getBean(QxtClientFactory.class);
        }
        NetMsgclient client = factory.create(ip, port, username, passwrord);
        if (client == null) {
            throw new AppException(ResponseCode.CLIENT_CREATE_ERROR);
        }

        String code = client.sendMsg(client, 0, phone, content, 1);

        if ("16".equals(code)) {
            log.info("发送短信[{}]：\t失败：{}", ThreadLocals.TIMES.get(), code);
            throw new AppException(ResponseCode.CLIENT_SEND_ERROR);
        } else {
            log.info("发送短信[{}]：\t完成，seqId:{}", ThreadLocals.TIMES.get(), code);
        }
        ThreadLocals.removeAll();
        return new ResponseResult("0", "调用成功", code);
    }
}
