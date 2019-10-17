package cn.qzlyhua.ssa.utils.qxt;

import com.linkage.netmsg.NetMsgclient;
import com.linkage.netmsg.server.AnswerBean;
import com.linkage.netmsg.server.ReceiveMsg;
import com.linkage.netmsg.server.ReturnMsgBean;
import com.linkage.netmsg.server.UpMsgBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 企信通客户端工厂类
 *
 * @author qzlyhua
 */
@Component
@Slf4j
public class QxtClientFactory {
    private final Map<String, Object> clientLock = new ConcurrentHashMap<>();
    private final Map<String, NetMsgclient> clientCache = new ConcurrentHashMap<>();

    public NetMsgclient create(String ip, int port, String username, String password) {
        Object lock = clientLock.get(username);
        if (lock == null) {
            lock = new Object();
            clientLock.put(username, lock);
        }

        synchronized (lock) {
            NetMsgclient client = getClient(ip, port, username, password);
            return client;
        }
    }

    private NetMsgclient getClient(String ip, int port, String username, String password) {
        if (clientCache.containsKey(username)) {
            return clientCache.get(username);
        } else {
            try {
                NetMsgclient client = new NetMsgclient();
                ReceiveMsg receiveMsg = new Receiver();
                client.initParameters(ip, port, username, password, receiveMsg);
                client.anthenMsg(client);
                clientCache.put(username, client);
                return client;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class Receiver extends ReceiveMsg {
        @Override
        public void getAnswer(AnswerBean answerBean) {
            super.getAnswer(answerBean);
            String seqIdString = answerBean.getSeqId();
            int status = answerBean.getStatus();
            String msgId = answerBean.getMsgId();
            log.info("发送短信[{}]\tAnswer：status:{}，msgId:{}",
                    seqIdString, status, msgId);
        }

        @Override
        public void getUpMsg(UpMsgBean upMsgBean) {
            super.getUpMsg(upMsgBean);
            String sequenceId = upMsgBean.getSequenceId();
            String sendNum = upMsgBean.getSendNum();
            String receiveNum = upMsgBean.getReceiveNum();
            String msgRecTime = upMsgBean.getMsgRecTime();
            String msgContent = upMsgBean.getMsgContent();
            log.info("发送短信[{}]\tUpMsg：sendNum:{}，receiveNum:{}，msgRecTime:{}，msgContent:{}",
                    sequenceId, sendNum, receiveNum, msgRecTime, msgContent);
        }

        @Override
        public void getReturnMsg(ReturnMsgBean returnMsgBean) {
            super.getReturnMsg(returnMsgBean);
            String sequenceId = returnMsgBean.getSequenceId();
            String msgId = returnMsgBean.getMsgId();
            String sendNum = returnMsgBean.getSendNum();
            String receiveNum = returnMsgBean.getReceiveNum();
            String submitTime = returnMsgBean.getSubmitTime();
            String sendTime = returnMsgBean.getSendTime();
            String msgStatus = returnMsgBean.getMsgStatus();
            int msgErrStatus = returnMsgBean.getMsgErrStatus();
            log.info("发送短信[{}]\tReturnMsg：sequenceId:{}，msgId:{}，sendNum:{}，receiveNum:{}，submitTime:{}，sendTime:{}，msgStatus:{}，msgErrStatus:{}",
                    msgId, sequenceId, msgId, sendNum, receiveNum, submitTime, sendTime, msgStatus, msgErrStatus);
        }
    }
}
