package com.zxq.iov.cloud.sp.vp.api.impl.event;

import com.alibaba.dubbo.common.json.JSON;
import com.zxq.iov.cloud.sp.vp.dao.event.*;
import com.zxq.iov.cloud.sp.vp.entity.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 安防 事件转换器
 *
 * @author 叶荣杰
 * create date 2015-6-8 10:12
 * modify date 2015-6-9 9:23
 * @version 0.2, 2015-6-9
 */
@Service
public class EventConverter {

    @Autowired
    private IEventDefinitionDaoService eventDefinitionDaoService;
    @Autowired
    private IEventInstanceDaoService eventInstanceDaoService;
    @Autowired
    private ITaskInstanceDaoService taskInstanceDaoService;
    @Autowired
    private IStepInstanceDaoService stepInstanceDaoService;
    @Autowired
    private IEventParameterDaoService eventParameterDaoService;

    private static final Integer RUNNING_STATUS = 1;
    private static final Integer FINISH_STATUS = 2;

    /**
     * 得到事件实例
     * @param eventInstantId        事件实例ID
     * @return                      事件实例对象
     */
    public EventInstance findEventInstanceById(Long eventInstantId) {
        return eventInstanceDaoService.findEventInstanceById(eventInstantId);
    }

    /**
     * 得到指定事件定义的未结束实例
     * @param eventDefinitionId     事件定义ID
     * @return                      事件实例列表
     */
    public List<EventInstance> findRunningEventInstance(Long eventDefinitionId) {
        Integer runningStatus = 1;
        return eventInstanceDaoService.listEventInstanceByEventDefinitionId(eventDefinitionId, runningStatus);
    }

    /**
     * 得到事件实例下未结束的任务实例
     * @param eventInstanceId       事件实例ID
     * @param taskDefinitionId      任务定义ID
     * @return                      任务实例对象
     */
    public TaskInstance findRunningTaskInstance(Long eventInstanceId, Long taskDefinitionId) {
        Integer runningStatus = 1;
        List<TaskInstance> list = taskInstanceDaoService.listTaskInstanceByEventInstanceId(eventInstanceId, taskDefinitionId, runningStatus);
        return (list.size()>0)?list.get(0):null;
    }

    /**
     * 完成运行的步骤实例
     * @param eventInstanceId       事件实例ID
     * @param stepDefinitionId      步骤定义ID
     * @param eventCreateTime       事件发生时间
     * @return                      步骤实例对象
     */
    public StepInstance finishRunningStepInstance(Long eventInstanceId, Long stepDefinitionId, Date eventCreateTime) {
        List<StepInstance> list = stepInstanceDaoService.listStepInstanceByEventInstanceId(eventInstanceId,
                stepDefinitionId, RUNNING_STATUS);
        if(list.size() > 0) {
            StepInstance stepInstance = list.get(0);
            stepInstance.setEndTime(eventCreateTime);
            stepInstance.setStatus(FINISH_STATUS);
            return stepInstanceDaoService.updateStepInstance(stepInstance);
        }
        return null;
    }

    /**
     * 完成运行的任务实例
     * @param eventInstanceId       事件实例ID
     * @param taskDefinitionId      任务定义ID
     * @param eventCreateTime       事件发生时间
     * @return                      任务实例对象
     */
    public TaskInstance finishRunningTaskInstance(Long eventInstanceId, Long taskDefinitionId, Date eventCreateTime) {
        List<TaskInstance> list = taskInstanceDaoService.listTaskInstanceByEventInstanceId(eventInstanceId,
                taskDefinitionId, RUNNING_STATUS);
        if(list.size() > 0) {
            TaskInstance taskInstance = list.get(0);
            taskInstance.setEndTime(eventCreateTime);
            taskInstance.setStatus(FINISH_STATUS);
            return taskInstanceDaoService.updateTaskInstance(taskInstance);
        }
        return null;
    }

    /**
     * 完成运行的事件实例
     * @param eventInstanceId       事件实例ID
     * @param eventDefinitionId     事件定义ID
     * @param eventCreateTime       事件发生时间
     * @return                      事件实例对象
     */
    public EventInstance finishRunningEventInstance(Long eventInstanceId, Long eventDefinitionId, Date eventCreateTime) {
        EventInstance eventInstance = null;
        if(null != eventInstanceId) {
            eventInstance = eventInstanceDaoService.findEventInstanceById(eventInstanceId);
        }
        if(null == eventInstance) {
            EventDefinition eventDefinition = eventDefinitionDaoService.findEventDefinitionById(eventDefinitionId);
            if(eventDefinition.isExclusive()) {
                List<EventInstance> list = eventInstanceDaoService.listEventInstanceByEventDefinitionId(eventDefinitionId, RUNNING_STATUS);
                if(list.size() > 0) {
                    eventInstance = list.get(0);
                }
            }
        }
        eventInstance.setEndTime(eventCreateTime);
        eventInstance.setStatus(FINISH_STATUS);
        return eventInstanceDaoService.updateEventInstance(eventInstance);
    }

    /**
     * 保存结果
     * @param stepInstanceId        步骤实例ID
     * @param result                结果对象
     */
    public void saveResult(Long stepInstanceId, Object result) {
        Integer outputType = 2;
        EventParameter eventParameter = new EventParameter();
        eventParameter.setStepInstanceId(stepInstanceId);
        eventParameter.setType(outputType);
        eventParameter.setName("result");
        try {
            eventParameter.setValue(JSON.json(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
        eventParameterDaoService.createEventParameter(eventParameter);
    }
}