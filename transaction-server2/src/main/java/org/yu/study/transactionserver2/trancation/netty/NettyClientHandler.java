package org.yu.study.transactionserver2.trancation.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.yu.study.transactionserver2.trancation.transactional.TransactionType;
import org.yu.study.transactionserver2.trancation.transactional.YhyTrancationManager;
import org.yu.study.transactionserver2.trancation.transactional.YhyTransaction;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext context;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接受数据:" + msg.toString());
        JSONObject jsonObject = JSON.parseObject((String) msg);

        String groupId = jsonObject.getString("groupId");
        String command = jsonObject.getString("command");

        System.out.println("接收command:" + command);
        // 对事务进行操作

        YhyTransaction yhyTransaction = YhyTrancationManager.getYhyTransaction(groupId);
        if("commit".equals(command)){
            yhyTransaction.setTransactionType(TransactionType.commit);
        }else {
            yhyTransaction.setTransactionType(TransactionType.rollback);
        }
        yhyTransaction.getTask().singalTask();
    }

    public synchronized Object call(JSONObject data) throws Exception {
        context.writeAndFlush(data.toJSONString());
        return null;
    }
}
