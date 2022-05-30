package org.yu.study.transactionserver2.trancation.transactional;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yu.study.transactionserver2.trancation.netty.NettyClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component
public class YhyTrancationManager {

    private static NettyClient nettyClient;

    private static ThreadLocal<YhyTransaction> current = new ThreadLocal<>();
    private static ThreadLocal<String> currentGroupId = new ThreadLocal<>();
    private static ThreadLocal<Integer> transactionCount = new ThreadLocal<>();

    @Autowired
    public void setNettyClient(NettyClient nettyClient) {
        YhyTrancationManager.nettyClient = nettyClient;
    }

    public static Map<String, YhyTransaction> LB_TRANSACION_MAP = new HashMap<>();

    /**
     * 创建事务组，并且返回groupId
     *
     * @return
     */
    public static String createYhyTransactionGroup() {
        String groupId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", groupId);
        jsonObject.put("command", "create");
        nettyClient.send(jsonObject);
        System.out.println("创建事务组server2");

        currentGroupId.set(groupId);
        return groupId;
    }

    /**
     * 创建分布式事务
     *
     * @param groupId
     * @return
     */
    public static YhyTransaction createYhyTransaction(String groupId) {
        String transactionId = UUID.randomUUID().toString();
        YhyTransaction yhyTransaction = new YhyTransaction(transactionId, groupId);
        LB_TRANSACION_MAP.put(groupId, yhyTransaction);
        current.set(yhyTransaction);
        addTransactionCount();

        System.out.println("创建事务server2");

        return yhyTransaction;
    }

    /**
     * 添加事务到事务组
     *
     * @param yhyTransaction
     * @param isEnd
     * @param transactionType
     * @return
     */
    public static YhyTransaction addYhybTransaction(YhyTransaction yhyTransaction, Boolean isEnd, TransactionType transactionType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", yhyTransaction.getGroupId());
        jsonObject.put("transactionId", yhyTransaction.getTransactionId());
        jsonObject.put("transactionType", transactionType);
        jsonObject.put("command", "add");
        jsonObject.put("isEnd", isEnd);
        jsonObject.put("transactionCount", YhyTrancationManager.getTransactionCount());
        nettyClient.send(jsonObject);
        System.out.println("添加事务server2");
        return yhyTransaction;
    }

    public static YhyTransaction getYhyTransaction(String groupId) {
        return LB_TRANSACION_MAP.get(groupId);
    }

    public static YhyTransaction getCurrent() {
        return current.get();
    }

    public static String getCurrentGroupId() {
        return currentGroupId.get();
    }

    public static void setCurrentGroupId(String groupId) {
        currentGroupId.set(groupId);
    }

    public static Integer getTransactionCount() {
        return transactionCount.get();
    }

    public static void setTransactionCount(int i) {
        transactionCount.set(i);
    }

    public static Integer addTransactionCount() {
        int i = (transactionCount.get() == null ? 0 : transactionCount.get()) + 1;
        transactionCount.set(i);
        return i;
    }
}
