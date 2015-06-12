package com.zxq.iov.cloud.sp.vp.dao.event.impl;

import com.zxq.iov.cloud.core.dal.repo.mybatis.PageResult;
import com.zxq.iov.cloud.core.log.LoggerFactory;
import com.zxq.iov.cloud.core.service.BaseServiceImpl;
import com.zxq.iov.cloud.sp.vp.dao.event.IEventDefinitionDaoService;
import com.zxq.iov.cloud.sp.vp.dao.event.repo.IEventDefinitionRepository;
import com.zxq.iov.cloud.sp.vp.entity.event.EventDefinition;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 安防 事件定义持久化服务接口实现类
 *
 * @author 叶荣杰
 * create date 2015-6-3 14:46
 * @version 0.1, 2015-6-3
 */
@Service
public class EventDefinitionDaoServiceImpl extends BaseServiceImpl<IEventDefinitionRepository, EventDefinition, Long> implements IEventDefinitionDaoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventDefinitionDaoServiceImpl.class);

    @Autowired
	public EventDefinitionDaoServiceImpl(IEventDefinitionRepository repo){
		super(repo);
	}

	public EventDefinition createEventDefinition(EventDefinition eventDefinition) {
		if (eventDefinition == null){
			LOGGER.error("eventDefinition cannot be null");
		}
		eventDefinition.setId(null);
		super.save(eventDefinition);
		return eventDefinition;
	}

	@Override
	public EventDefinition updateEventDefinition(EventDefinition eventDefinition) {
		if (eventDefinition == null){
			LOGGER.error("eventDefinition cannot be null");
		}
		super.update(eventDefinition);
		return eventDefinition;
	}

	@Override
	public void removeEventDefinition(Long eventDefinitionId) {
		if (eventDefinitionId == null){
			LOGGER.error("eventDefinitionId cannot be null");
		}
		super.delete(eventDefinitionId);
	}

	@Override
	public EventDefinition findEventDefinitionById(Long eventDefinitionId) {
		if (eventDefinitionId == null){
			LOGGER.error("eventDefinitionId cannot be null");
		}
		return super.findOne(eventDefinitionId);
	}

	@Override
	public PageResult<EventDefinition> pagingEventDefinition(PageResult<EventDefinition> pageResult,
													Map<String, Object> paramMap) {
		return super.getPagedListViaBatis(pageResult, paramMap);
	}
}