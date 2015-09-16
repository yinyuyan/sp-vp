package com.zxq.iov.cloud.sp.vp.service.impl.event;

import com.saicmotor.telematics.framework.core.exception.ServLayerException;
import com.zxq.iov.cloud.sp.vp.common.constants.ExceptionConstants;
import com.zxq.iov.cloud.sp.vp.dao.event.IEventRuleDao;
import com.zxq.iov.cloud.sp.vp.dao.event.IStepDefinitionDao;
import com.zxq.iov.cloud.sp.vp.entity.event.EventRule;
import com.zxq.iov.cloud.sp.vp.entity.event.StepDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 安防 事件解析器
 *
 * @author 叶荣杰
 * create date 2015-6-5 16:11
 * modify date 2015-7-24 13:33
 * @version 0.6, 2015-7-24
 */
@Service
public class EventParser {

    @Autowired
    private EventConverter eventConvert;
    @Autowired
    private IStepDefinitionDao stepDefinitionDaoService;
    @Autowired
    private IEventRuleDao eventRuleDaoService;

    /**
     * 定位步骤定义
     * @param code              代码
     * @param paramMap          参数MAP
     * @param eventInstanceId   事件实例ID
     * @return                  步骤定义对象
     */
    public StepDefinition findStepDefiniton(String code, Map<String, Object> paramMap, Long eventInstanceId)
            throws ServLayerException {
        Long eventDefinitionId = null;
        if(null != eventInstanceId) {
            eventDefinitionId = eventConvert.findEventInstanceById(eventInstanceId).getEventDefinitionId();
        }
        List<StepDefinition> list = stepDefinitionDaoService.listStepDefinitionByStartCodeAndEventDefinitionId(code,
                eventDefinitionId);
        if(list.size() > 0) {
            if(list.size() == 1) {
                return list.get(0);
            }
            if(null != paramMap && paramMap.size() > 0) {
                for(StepDefinition stepDefinition : list) {
                    boolean isMatch = true;
                    for(EventRule eventRule : eventRuleDaoService.listEventRuleByStepDefinitionId(stepDefinition.getId())) {
                        isMatch = isMatch && validateRule(paramMap.get(eventRule.getName()), eventRule.getOperator(), eventRule.getValue());
                    }
                    if(isMatch) {
                        return stepDefinition;
                    }
                }
            }
        }
        throw new ServLayerException(ExceptionConstants.START_CODE_NOT_MATCH);
    }

    /**
     * 查询步骤对应的业务类
     * @param stepDefinitionId  步骤定义ID
     * @return                  业务类路径
     */
    public String findStepCallbackClass(Long stepDefinitionId) {
        return null;
    }

    /**
     * 验证是否满足规则
     * @param obj           比较对象
     * @param operator      比较操作符
     * @param value         比较值
     * @return              是否满足规则
     */
    private boolean validateRule(Object obj, String operator, String value) {
        if(null != obj && operator.equals("eq")) {
            return obj.toString().equals(value);
        }
        return false;
    }
}