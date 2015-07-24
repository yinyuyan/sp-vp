package com.zxq.iov.cloud.sp.vp.api;

/**
 * 安防 事件接口
 *
 * @author 叶荣杰
 * create date 2015-6-29 17:04
 * modify date 2015-7-24 10:45
 * @version 0.4, 2015-7-24
 */
public interface IEventService {

    /**
     * Dispatch超时
     * @param stepInstanceId    步骤实例ID
     */
    void dispatchTimeout(Long stepInstanceId);


    /**
     * Dispatch确认
     * @param stepInstanceId     步骤实例ID
     */
    void dispatchAck(Long stepInstanceId) throws Exception;

    /**
     * 检查超时步骤、任务及事件
     */
    void checkTimeout();
}
