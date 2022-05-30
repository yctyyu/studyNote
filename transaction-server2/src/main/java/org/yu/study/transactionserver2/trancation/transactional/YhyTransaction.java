package org.yu.study.transactionserver2.trancation.transactional;


import org.yu.study.transactionserver2.trancation.util.Task;

public class YhyTransaction {
    private String transactionId;
    private String groupId;
    private TransactionType transactionType;
    private Task task;

    public YhyTransaction(String transactionId, String groupId) {
        this.transactionId = transactionId;
        this.groupId = groupId;
        this.task = new Task();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
