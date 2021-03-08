package com.ydqp.lobby.service;

import com.ydqp.common.entity.PlayerTask;
import com.ydqp.common.entity.TaskConfig;
import com.ydqp.lobby.dao.TaskDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TaskService {

    private TaskService() {}

    public static TaskService instance;

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    public PlayerTask getCurrentTask(int taskType, long playerId) {
        return TaskDao.getInstance().getCurrentTask(taskType, playerId);
    }

    public long saveTask(Map<String, Object> task) {
        return TaskDao.getInstance().saveTask(task);
    }

    public void doneTask(Object[] params) {
        TaskDao.getInstance().doneTask(params);
    }

    public void completeTask(Object[] params) {
        TaskDao.getInstance().completeTask(params);
    }

    public void doDailyTask(Object[] params) {
        TaskDao.getInstance().doDailyTask(params);
    }

    public List<TaskConfig> getTasks(int taskType) {
        return TaskDao.getInstance().findAllByType(taskType);
    }

    public PlayerTask QueryByTaskIdAndPlayerId(int taskId, long playerId) {
        return TaskDao.getInstance().QueryByTaskIdAndPlayerId(taskId, playerId);
    }


    public static int getDayDiffer(Date startDate, Date endDate) {
        //判断是否跨年
        try {
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            String startYear = yearFormat.format(startDate);
            String endYear = yearFormat.format(endDate);
            if (startYear.equals(endYear)) {
                /*  使用Calendar跨年的情况会出现问题    */
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                int startDay = calendar.get(Calendar.DAY_OF_YEAR);
                calendar.setTime(endDate);
                int endDay = calendar.get(Calendar.DAY_OF_YEAR);
                return endDay - startDay;
            } else {
                /*  跨年不会出现问题，需要注意不满24小时情况（2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 0）  */
                //  只格式化日期，消除不满24小时影响
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                long startDateTime = dateFormat.parse(dateFormat.format(startDate)).getTime();
                long endDateTime = dateFormat.parse(dateFormat.format(endDate)).getTime();
                return (int) ((endDateTime - startDateTime) / (1000 * 3600 * 24));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
