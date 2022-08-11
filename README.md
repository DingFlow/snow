## 平台简介

  DingFlow项目也叫Snow系统。项目之初是笔者的一个flowable学习demo，取其雪之意，念及雪之人便有了Snow。后来
  在公司集成钉钉接口时，偶然间发现钉钉自带工作流插件，便想利用业余时间想打造一个开源的面向中小型企业的管理系统。
  该管理系统能和钉钉数据打通，企业可以通过钉钉移动化办公，也可通过系统开发满足自身不同需求。于是便有了现在的DingFlow。
  DingFlow取其DingTalk和Flowable的前四个单词组成。寓意着构建钉钉工作流一体化。目前系统还处于初始孵化阶段，
  虽然目标很明确，但是具体的实施方案还不够详细。笔者也呼吁有能之士能参与进来一起携手做好DingFlow。

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
23. **消息中心：消息模板配置;支持站内信、邮箱、钉钉三种消息通知方式**
24. **邮件管理：DingFlow邮件发送功能（支持草稿箱，垃圾箱，重要邮件标识）**
25. **钉钉流程管理：钉钉端发起流程系统可在线审批查看流程详情**
26. **表单设计器：DingFlow可在线设计表单,表单和流程相结合**
27. **DingFlow官网：系统包含前台DingFlow官方网站和官网后端管理系统**
28. **客户管理：简易版客户管理**
29. **账务管理：简易版账户和账户流水，支付申请单管理**
## 在线体验
  暂未开放数据库，有需要的可以加群，项目可用于搭建流程平台商业化

  **后台管理系统地址：https://www.dingflow.yifaoa.top/login**

  **DingFlow前台官网地址：https://www.dingflow.yifaoa.top/front/index**

  **开发地址：https://www.dingflow.yifaoa.top/docs**

- QQ群:   
-  **技术交流群：
   QQ一群： 577813338(已满)**<a target="_blank" href="https://jq.qq.com/?_wv=1027&k=HBdPBdyf"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="Flop Mine" title="DingFlow"></a>
   QQ二群: 473942262
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
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/%E7%99%BB%E5%BD%95%E9%A1%B5.png"/></td>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/a5b69fa27ba147a89efc229d90e1d27e.png"/></td>
    </tr>
    <tr>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/2bfbd71d08604780aae173307e7446df.png"/></td>
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
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/%E8%A1%A8%E5%8D%95.png"/></td>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/%E8%A1%A8%E5%8D%95%E8%AE%BE%E8%AE%A1.png"/></td>
    </tr>
    <tr>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/593ec8182b814b77adedb1d5a7265268.png"/></td>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/b0f956c913ee42af9767b91694b04497.png"/></td>
    </tr>
     <tr>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/%E5%AE%98%E7%BD%91%E5%90%8E%E5%8F%B0.png"/></td>
        <td><img src="https://qimetons.oss-cn-beijing.aliyuncs.com/%E5%AE%98%E7%BD%91%E9%A6%96%E9%A1%B5.png"/></td>
     </tr>

</table>

## 版本发布
v1.0 2021-02-24 DingFlow首发
  - 1.0 集成flowable官方设计器
  - 2.0 抛弃官方设计器用户组，使用DingFlow首发自带用户，流程角色。流程角色实现向下兼容（即流程某节点配置父角色，其父角色下所有子角色都具有流程审批权限）
  - 3.0 系统自带请假申请和采购单申请，采用类form表单提交，保存流程资源表。反序列化表单，实现流程中取业务数据。接口通用，一个接口按照规定格式设计流程即可。
  - 4.0 全局监听器抽象设计，实现任务创建时，钉钉消息通知，待办人实时接收消息，及时处理任务。
  - 5.0 任务监听器抽象设计，满足各种节点自定义业务需求，按照规则开发即可。
  - 6.0 系统创建用户和部门，同步到钉钉用户和部门。实现钉钉移动化办公。
  - 7.0 钉钉修改用户和部门，系统异步监听数据变化，实时修改系统数据。实现钉钉和系统数据同步。
  - 8.0 账单管理，导出支付宝，微信账单。一键导入系统，系统轻松管理账单数据，日常消费明了清晰。
  
v1.1 2021-02-26 
   - 1.0 集成DingFlow数据大屏，智能化科技大屏，实时了解钉钉数据，流程数据状态
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
  - 2.0 新增DingFlow邮件发送功能（支持草稿箱，垃圾箱，重要邮件标识）
  - 3.0 修改请假申请流程，统一流程开发规范
  - 4.0 DingFlow内置公告现可同步到钉钉公告平台
  - 5.0 分离审批业务数据页和审批页
  - 6.0 修复驳回时修改表单，流程审批中表单数据未修改问题
  - 7.0 修复消息模板参数替换时为空异常情况
  - 8.0 修复消息节点添加时没传父节点参数问题
  - 9.0 调整部分代码包位置，删除部分冗余代码  
  
V1.4 2021-03-28
  - 1.0 集成钉钉流程管理
     - 钉钉端发起流程，可在系统查收待办、已办、处理待办、查看我发起流程详情情况。
  - 2.0 新增表单管理
     - 集成表单在线设计器，可在线设计表单
  - 3.0 新增钉钉部门与系统部门定时同步任务
  - 4.0 新增钉钉用户与系统用户定时同步任务
  - 5.0 修复本地图片上传异常情况
  - 6.0 修复系统审批跳转审批详情页异常情况
  - 7.0 修复请假单和离职申请单查看权限问题
  - 8.0 升级钉钉jar包﻿﻿
  
V1.5 2021-05-05
  - 1.0 集成开源积木报表
     - 可在线设计各类报表（表格类报表和图表类报表)
  - 2.0 新增业务管理模块
     - 新增客户准入流程，添加客户流入客户公海，发起准入流程，准入客户进入客户私海，可业务员拜访跟进，未准入客户进入黑名单
     - 原数据大屏并入业务管理
  - 3.0 新增消息中心
  - 4.0 在线邮件更改为站内邮件，不在调用邮件发送功能，便于系统内用户沟通，跟进业务
  - 5.0 修复流程父角色查询所属子角色异常情况
  - 6.0 修复添加用户时部门树筛选异常
  - 7.0 新增Snow社区在线文档（地址见友情链接）

V1.6 2021-05-17
  - 1.0 集成钉钉扫码登录功能
  - 2.0 新增省市区管理
  - 3.0 优化部门管理，支持部门修改和删除钉钉数据双向同步，解决新增添加两次数据
    - 新增根据用户查询部门主管接口
  - 4.0 优化请假流程和离职流程，让流程更贴近业务
     - 上级部门主管审批：由当前申请人的直接上级主管审批，不在是流程组配置
  - 5.0 优化部分界面和查询方式    
  - 6.0 修复部分jar冲突，解决流程报错和部分页面报错
 
 V1.7 2021-07-04
   - 1.0 集成微信、支付宝扫码登录功能
   - 2.0 新增DingFlow官网
   - 3.0 官网注册、登录功能
     - 新增根据用户查询部门主管接口
   - 4.0 新增官网FAQ管理：登录官网可在线提FAQ
      - 上级部门主管审批：由当前申请人的直接上级主管审批，不在是流程组配置
   - 5.0 重构钉钉回调注册管理   
   - 6.0 重构消息中心模块,支持站内信
   - 7.0 重修复分配系统角色时会自动删除流程角色 

V1.8 2021-11-14
- 1.0 新增系统任务模块
    - 系统创建任务后，可同步到钉钉待办任务
- 2.0 解耦系统与钉钉的强依赖，新增是否同步配置
- 3.0 新增钉钉外部联系人模块
    - 系统管理钉钉外部联系人
- 4.0 完善系统用户和钉钉用户同步机制
- 5.0 新增流程待办，流程完结，系统任务站内信消息通知
- 6.0 新增手机号，姓名，身份证，秘钥等重要隐私信息脱敏  
- 7.0 重构部分代码

V2.0 2021-12-12
  - DingFlow从今年2月份发布第一个版本以来，一直不忘初衷--借助钉钉，致力于流程，让流程更简单。这个版本改动还是挺大的，刚好今天是双十二。DingFlow将开启2.0时代，后续版本迭代都是2.X更新。
- 1.0 重构表单模块，自定义表单改成layui表单
    - 支持自定义表单，创建，查看，预览，二维码分享表单等功能
- 2.0 流程和自定义表单关联
    - 可以通过设计自定义表单和流程绑定，实现简易化流程自动流转，不需要您编写一行代码。
- 3.0 对数据库密码明文进行加密
- 4.0 我的待办，已办，发起流程等页面适配自定义表单
- 5.0 官网新闻可后台配置化
- 6.0 登录页官网页等优化
- 7.0 新增简历投递流程

V2.0.1 2021-12-31
  - 2021年最后一次版本迭代，本次迭代主要对底层数据库架构和代码生成模块做了变更，具体变更如下：
- 1.0 整合Mybatis-Plus,版本号：3.4.2
    - 为了快速进行单表CRUD，本次迭代引入Mybatis-Plus,后续功能都会基于此开发
- 2.0 重构代码生成模块
    - 支持生成Mybatis-Plus模式代码
    - 支持在线SQL建表功能
    - 支持一键生成代码到项目文件夹下
    - 支持一键生成菜单按钮功能
- 3.0 调整部分菜单按钮位置，使功能布局更合理化
- 4.0 调整登录、注册等页面

V2.1.0 2022-03-14
- 酝酿了很久不知道要写一些什么，这个版本就简单的写了一些业务代码，后续更新方向，大家可以给一些建议：
- 1.0 新增DingDing考勤管理，同步钉钉考勤数据到系统，系统也可完善考勤信息。
- 2.0 完善账务管理功能
    - 新增虚拟账户管理
    - 新增账户流水管理
    - 新增支付申请单流程
- 3.0 优化采购管理，现在采购单，支付申请单，虚拟账户实现打通
- 4.0 修复Excel导出异常功能
- 5.0 优化部分菜单栏目

V2.1.1 2022-08-01
- 项目断更好久了，最近一段时间阿吉比较忙碌，入职了新公司。从0到1开发了一款奢侈品电商APP（需要买名牌包的联系阿吉），这期先做个小更新，近期都会以优化代码为主：
- 1.0 重构邮件模块
- 2.0 重构消息中心
- 3.0 重构系统任务  
- 4.0 钉钉模块部分代码调整

   
## 未来规划
 - 1.0 接入企业微信
 - 2.0 优化表单和流程
 
## 交流群
 - QQ群: 一群：577813338(已满)，二群: 473942262
 - 欢迎入群讨论，我们的口号：**借助钉钉，致力于流程，让流程更简单**
 
 - DingFlow公众号（关注公众号即可获取DingFlow开发手册一份）
 - <img src="https://qimetons.oss-cn-beijing.aliyuncs.com/0f39a32c5ffd44069dcbc8c1861bfe54.jpg"/>
   
 
## 我有话说 
  开源离不开您的参与、支持与鼓励。开源不易，如果您觉得项目对您有帮助，请您动动小手star一下，也是对作者的莫大帮助与支持。

## 友情链接
   - 博客：http://47.114.7.28:8082（暂时不能访问）
   - 若依：http://ruoyi.vip 
   - 在线文档：http://dingflow.xyz:3000（暂时不能访问）
   
## 最后，特别感谢若依系统提供框架
    
