## 平台简介

  Ding-Flow项目也叫Snow系统。项目之初是笔者的一个flowable学习demo，取其雪之意，念及雪之人便有了Snow。后来
  在公司集成钉钉接口时，偶然间发现钉钉自带工作流插件，便想利用业余时间想打造一个开源的面向小型企业的管理系统。
  该管理系统能和钉钉数据打通，企业可以通过钉钉移动化办公，也可通过系统开发满足自身不同需求。于是便有了现在的Ding-Flow。
  Ding-Flow取其DingTalk和Flowable的前四个单词组成。寓意着构建钉钉工作流一体化。目前系统还处于初始孵化阶段，
  虽然目标很明确，但是具体的实施方案还不够详细。笔者也呼吁有能之士能参与进来一起携手做好Ding-Flow。

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置并能**实现钉钉用户数据同步**。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限并能**实现钉钉部门数据同步**。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 在线构建器：拖动表单元素生成相应的HTML代码。
17. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。
18. **钉钉管理：钉钉用户，部门体系数据同步，请假单，采购单流程**
19. **流程管理： 流程模型在线设计，流程组管理，待办钉钉，邮件通知**
20. **账单管理：一键导入支付宝和微信账单，可系统化管理账单**
21. **系统序列设置：每日凌晨更新序列号，系统按照日期生成自增序列单号**
22. **可视化大屏：智能化科技大屏，实时了解钉钉数据，流程数据状态**
23. **消息管理：在线设计消息模板，对流程节点消息推送开关化配置**
24. **邮件管理：Ding-Flow邮件发送功能（支持草稿箱，垃圾箱，重要邮件标识）**
## 在线体验
  暂未开放数据库，有需要的可以加群，项目可用于搭建流程平台商业化
- QQ群: **577813338**
- 超级管理员： admin/admin123  
- 普通管理员： yuanyb/admin123
- 钉钉管理员： ry/admin123    
## 启动项目配置
- 1.加群，在群文档获取项目数据库
- 2.配置钉钉秘钥
  <img src="https://qimetons.oss-cn-beijing.aliyuncs.com/c2a24a8fd77e462586137f21f3a5380f.png"/>
- 3.配置钉钉回调参数
  <img src="https://qimetons.oss-cn-beijing.aliyuncs.com/bbee68c5a1c64092ab4e0fa29d7cdf5d.png"/>
  钉钉回调需要公网，为了方便提供一个内网穿透工具，详情参考：http://snowblog.shop.csj361.com/post/26  
  
## 项目截图
<table>
    <tr>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/094981bad3844c25b4e0e9ae5fdfb253.png"/></td>
            <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/a5b69fa27ba147a89efc229d90e1d27e.png"/></td>
    </tr>
    <tr>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/694936c6e1b34af4a9c2525bf25156ba.png"/></td>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/102a95b10c5b4094ae9eea060ed3d0c4.png"/></td>
    </tr>
    <tr>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/de48282f792d4300ba26b7a397464d71.png"/></td>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/952eaddf26164feaae13ec9ffdbeab35.png"/></td>
    </tr>
    <tr>
         <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/593f9a39070b4a5f879b91d94c6cc65e.png"/></td>
         <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/75db4a86e2224be7bdfd3de6e6cce43b.png"/></td>
    </tr>
    <tr>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/416ffa7c1f1f42f8807a24dc2e63a08d.png"/></td>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/0ffee016a6be4d5eb5b5f18a0291353e.png"/></td>
    </tr>
    <tr>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/593ec8182b814b77adedb1d5a7265268.png"/></td>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/b0f956c913ee42af9767b91694b04497.png"/></td>
    </tr>

</table>

## 版本发布
v1.0 2021-02-24 ding-flow首发
  - 1.0 集成flowable官方设计器
  - 2.0 抛弃官方设计器用户组，使用ding-flow自带用户，流程角色。流程角色实现向下兼容（即流程某节点配置父角色，其父角色下所有子角色都具有流程审批权限）
  - 3.0 系统自带请假申请和采购单申请，采用类form表单提交，保存流程资源表。反序列化表单，实现流程中取业务数据。接口通用，一个接口按照规定格式设计流程即可。
  - 4.0 全局监听器抽象设计，实现任务创建时，钉钉消息通知，待办人实时接收消息，及时处理任务。
  - 5.0 任务监听器抽象设计，满足各种节点自定义业务需求，按照规则开发即可。
  - 6.0 系统创建用户和部门，同步到钉钉用户和部门。实现钉钉移动化办公。
  - 7.0 钉钉修改用户和部门，系统异步监听数据变化，实时修改系统数据。实现钉钉和系统数据同步。
  - 8.0 账单管理，导出支付宝，微信账单。一键导入系统，系统轻松管理账单数据，日常消费明了清晰。
  
v1.1 2021-02-26 
   - 1.0 集成ding-flow数据大屏，智能化科技大屏，实时了解钉钉数据，流程数据状态
   - 2.0 修复流程历史数据保存失败问题
   - 3.0 调整待办，增加已办功能
   
v1.2 2021-03-08
  - 1.0 新增消息管理
     - (1) 消息节点树形配置
     - (2) 消息节点触发配置，可实时对流程消息推送做开关配置
     - (3) 新增消息模板：在线设计dingTalk和邮件模板。消息内容支持参数化配置(使用${}识别参数)
  - 2.0 新增流程发起和流程完结消息推送功能
  - 3.0 新增转办任务功能(直接将办理人换成其他人)
  - 4.0 新增委派任务功能(将任务节点分配给其他人处理，等其他人处理完之后，委派任务会自动回到委派人的任务中) 
  - 5.0 挂起流程
  - 6.0 激活流程 
   
v1.3 2021-03-14
  - 1.0 新增离职申请流程（会签版）
  - 2.0 新增Ding-Flow邮件发送功能（支持草稿箱，垃圾箱，重要邮件标识）
  - 3.0 修改请假申请流程，统一流程开发规范
  - 4.0 Ding-Flow内置公告现可同步到钉钉公告平台
  - 5.0 分离审批业务数据页和审批页
  - 6.0 修复驳回时修改表单，流程审批中表单数据未修改问题
  - 7.0 修复消息模板参数替换时为空异常情况
  - 8.0 修复消息节点添加时没传父节点参数问题
  - 9.0 调整部分代码包位置，删除部分冗余代码    
    
   
## 未来规划

 - 1.0 流程表单设计
 - 2.0 钉钉工作流与系统工作流打通,让流程移动化
 - 3.0 钉钉同步数据失败后，手工重试
 - 4.0 记录消息通知轨迹
 - 5.0 钉钉机器人集成
 
## 交流群
 - QQ群: 577813338
 - 欢迎入群讨论，我们的口号：**借助钉钉，致力于流程，让流程更简单**
 
 - Ding-Flow公众号（关注公众号即可获取DingFlow开发手册一份）
 - <img src="https://qimetons.oss-cn-beijing.aliyuncs.com/0f39a32c5ffd44069dcbc8c1861bfe54.jpg"/>
   
 
## 我有话说 
  开源离不开您的参与、支持与鼓励。开源不易，如果您觉得项目对您有帮助，请您动动小手star一下，也是对作者的莫大帮助与支持。

## 友情链接
   - 博客：http://snowblog.shop.csj361.com
   - 若依：http://ruoyi.vip 
   
## 最后，特别感谢若依系统提供框架
    
