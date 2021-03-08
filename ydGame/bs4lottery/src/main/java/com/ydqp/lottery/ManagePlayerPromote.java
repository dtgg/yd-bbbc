package com.ydqp.lottery;

import com.ydqp.common.constant.TaskType;
import com.ydqp.common.dao.TaskDao;
import com.ydqp.common.entity.PlayerPromoteConfig;
import com.ydqp.common.entity.TaskConfig;
import com.ydqp.lottery.dao.PlayerPromoteConfigDao;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class ManagePlayerPromote {

    private ManagePlayerPromote() {}

    private static ManagePlayerPromote instance;

    public static ManagePlayerPromote getInstance() {
        if (instance == null)
            instance = new ManagePlayerPromote();
        return instance;
    }

    static {
//        config = PlayerPromoteConfigDao.getInstance().getConfig();
        taskConfigList = TaskDao.getInstance().findAllByType(TaskType.RECOMMEND_TASK);
    }

//    private static PlayerPromoteConfig config;

    private static List<TaskConfig> taskConfigList;

    public PlayerPromoteConfig getConfig() {
        return PlayerPromoteConfigDao.getInstance().getConfig();
    }

    public List<TaskConfig> getTaskConfigList() {
        return taskConfigList;
    }

    public TaskConfig getTaskConfig(int taskId) {
        if (CollectionUtils.isEmpty(taskConfigList)) return null;

        TaskConfig config = null;
        for (TaskConfig taskConfig : taskConfigList) {
            if (taskConfig.getId() == taskId) {
                config = taskConfig;
                break;
            }
        }
        return config;
    }
}
