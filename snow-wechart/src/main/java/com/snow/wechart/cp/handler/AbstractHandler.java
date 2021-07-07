package com.snow.wechart.cp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chanjar.weixin.cp.message.WxCpMessageHandler;

/**
 * 抽象处理
 */
public abstract class AbstractHandler implements WxCpMessageHandler {
  protected Logger logger = LoggerFactory.getLogger(getClass());
}
