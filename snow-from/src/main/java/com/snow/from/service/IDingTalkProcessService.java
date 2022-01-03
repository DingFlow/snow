package com.snow.from.service;

public interface IDingTalkProcessService {

   /**
    * 新增或者更新流程钉钉模板
    * @param formCode 系统表单code
    * @return
    */
   String saveOrUpdateDingTalkProcess(String formCode);


   /**
    * 发起自有工作流程流程实例
    * @param fromNo 表单编号
    * @return
    */
   String SaveFakeProcessInstance(String fromNo);
}
