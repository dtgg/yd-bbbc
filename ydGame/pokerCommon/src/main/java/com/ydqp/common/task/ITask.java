package com.ydqp.common.task;

public interface ITask {

    boolean acceptTask(int taskId);

    boolean doTask(int taskId);

    int getReward(int taskId);
}
