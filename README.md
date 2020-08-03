# activiti-study

> 工作流学习项目

[TOC]

## 一、子项目说明

### 子项目一：activiti-study-demo01

> 项目结构
> - 项目框架： `Spring Boot 2.3.2.RELEASE`
> - `JDK` 版本：`1.8` 


###  子项目二： activiti-study-demo02

> 项目结构
> - 项目框架：`Spring` + `maven` + `log4j`
> - `JDK` 版本：`1.8` 


## 二、activiti 学习说明

[activiti 6.0 官方文档](https://www.activiti.org/userguide/#_configuration)

### 1）、初始化activity所需要的数据表

> 初始化方式有两种：
>   - 通过手动设置有关数据源的方式
>   - 通过配置文件的方式配置有关数据源

> 具体的实现方式有四种，见子项目二 中的 InitTable.java 中


### 2）、工作流配置文件中的有关配置项
> - `jdbcUrl` ： 数据库连接URL 配置
> - `jdbcDriver` ： 数据库连接驱动 配置
> - `jdbcUsername` ： 数据库连接用户名 配置
> - `jdbcPassword` ：  数据库连接密码 配置
> - `jdbcMaxActiveConnections` ： 设置连接池中处于使用状态的最大连接数，默认10
> - `jdbcMaxIdleConnections` ： 设置连接池中处于空闲状态的连接数
> - `jdbcMaxCheckoutTime` ： 设置连接池中取出使用的连接使用的最长时间，超过该时间会被强制收回，默认20000 (20秒)
> - `jdbcMaxWaitTime` ：  设置获取连接的等待时间，当连接长时间没有得到连接时，会打印一条日志，然后重新获取连接，避免因为配置错误导致的沉默的操作失败，默认值为 20000 (20秒)
> - `dataSource` ：  数据源的配置，通过该种方式是使用外部数据源的时候使用的，如可以直接使用 `spring` 管理的数据源，而不用再次配置
> - `databaseSchemaUpdate` ：  设置数据表的创建策略，官方给出的有三种策略
>   - `ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE` ： 永远不创建数据表
>   - `ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP` ： 使用前创建数据表，使用完后删除所有的表
>   - `ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE` ： 如果数据库中没有对应的数据表就创建


### 3）、Database table names explained （activity流程数据库表名说明）

>The database names of Activiti all start with ACT_. 
>The second part is a two-character identification of the use case of the table. 
>This use case will also roughly match the service API.
> 
> - ACT_RE_*: RE stands for repository. Tables with this prefix contain static information such as process definitions and process resources (images, rules, etc.).
> - ACT_RU_*: RU stands for runtime. These are the runtime tables that contain the runtime data of process instances, user tasks, variables, jobs, etc.
>            Activiti only stores the runtime data during process instance execution, and removes the records when a process instance ends. 
>               This keeps the runtime tables small and fast.
> - ACT_ID_*: ID stands for identity. These tables contain identity information, such as users, groups, etc.
> - ACT_HI_*: HI stands for history. These are the tables that contain historic data, such as past process instances, variables, tasks, etc.
> - ACT_GE_*: general data, which is used in various use cases.

> Activiti的数据库名称均以ACT_开头。第二部分是表用例的两个字符的标识。该用例也将与服务API大致匹配
>

#### Ⅰ、数据库数据表说明

- `RepositoryService` 操作的表
    - `act_ge_bytearray` ：二进制文件表
    - `act_re_deployment` ：流程部署表
    - `act_re_procdef` ：流程定义表
    - `act_ge_property` ：工作流的ID算法和版本信息表
    
- `RuntimeService`  `TaskService`  操作的表
    - `act_ru_execution` ：流程启动一次只要没有执行完，就回有一条数据
    - `act_ru_task` ：可能有多条数据
    - `act_ru_variable` ：记录流程运行时的流程变量
    - `act_ru_identitylink` ：存放流程办理人的信息
    
- `HistroyService` 操作的表
    - `act_hi_procinst` ：历史流程实例
    - `act_hi_taskinst` ：历史任务实例
    - `act_hi_actinst` ：历史活动节点表
    - `act_hi_varinst` ：历史流程变量表
    - `act_hi_identitylink` ：历史办理人表
    - `act_hi_comment` ：批注表
    - `act_hi_atachment` ：附件表

- `IdentityService` 操作的表 `activiti 7` 中没有这些表
    - `act_id_group` ：角色表
    - `act_id_membership` ：用户和角色之间的关系
    - `act_id_info` ：用户的详细信息
    - `act_id_user` ：用户表



### 4）、 `Acitivi` 的核心 `API`

> 1. `ProcessEngine`
> > 1. 是工作流的核心类，工作流中的其他的类均是通过该类进行创建的
> > 2. 该类的对象的创建方式有两种：
> >     - 通过流程引擎配置对象 ( `ProcessEngineConfiguration` )，获取
> >     - 通过 `ProcessEngines` 对象获取

- 通过 `ProcessEngineConfiguration` 获取
```java
 public class InitTable {
         /**
          * 初始化流程引擎所需要的数据表  方式一：
          * 通过手动设置JDBC参数，创建流程引擎配置对象，获得流程引擎对象，从而初始化流程引擎所需的数据库
          */
         public void createTableMethodOne() {
     
             log.info("开始时间",System.nanoTime());
     
             //1. 获得流程引擎配置对象
             ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
     
             //2. 设置流程引擎配置的JDBC属性
             processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
             processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3307/activity?serverTimezone=GMT%2B8");
             processEngineConfiguration.setJdbcUsername("root");
             processEngineConfiguration.setJdbcPassword("123456");
     
             /**
              * 3. 配置流程引擎的数据表初始化方式：
              * ProcessEngineConfiguration对象给出如下几种方式：
              * - DB_SCHEMA_UPDATE_FALSE：永远不创建activity所需要的数据表
              * - DB_SCHEMA_UPDATE_CREATE_DROP：使用前创建表，使用完后删除创建的表
              * - DB_SCHEMA_UPDATE_TRUE：如果数据库中没有表就创建
              * - drop-create：如果数据库存在activity数据表，则先删除再创建
              */
             //processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
             processEngineConfiguration.setDatabaseSchemaUpdate("drop-create");
     
             //4. 得到流程引擎
             ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
             log.warn("得到的流程引擎对象为="+processEngine);
             log.info("结束时间",System.nanoTime());
             System.out.println("得到的流程引擎对象为 ==>  " + processEngine);
         }
     
         /**
          * 初始化工作流所需的数据表，方式二
          * 该方式和第一种方式一样，不同点在于，方式一是直接给流程引擎配置对象设置数据源；方式二是想通过获取spring的数据源管理对象，并设置有关的数据源后，流程引擎配置对象只需要设置数据源即可
          */
         public void createTableMethodTwo(){
     
             //1. 获取DrivaerManagerDataSource对象，并设置数据源属性
             DriverManagerDataSource dataSource = new DriverManagerDataSource();
             dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
             dataSource.setUrl("jdbc:mysql://localhost:3306/activity?useUnicode=true&characterEncoding=UTF-8&userSSL=false&serverTimezone=GMT%2B8");
             dataSource.setUsername("root");
             dataSource.setPassword("123456");
     
             //2. 获取流程引擎配置对象
             ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
             processEngineConfiguration.setDataSource(dataSource);
     
             //3. 设置流程引擎的数据表初始化方式，初始化方式种类见第一中方式中
             processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
     
             //4. 得到流程引擎对象，初始化数据库
             ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
     
             log.info("流程引擎对象为 ==> " + processEngine);
     
         }
     
         /**
          * 初始化工作流所需的数据表方式三
          * 通过配置spring的配置文件的方式将有关数据源的配置放在xml的bean对象中，交由spring进行管理流程引擎配置对象，注意使用该种方式时，对应的配置文件不一定命名为 activiti.cfg.xml 这是与第四种方式的明显差异
          */
         public void createTableMethodThree(){
             //通过读取配置文件 activiti.cfg.xml进行配置其有关数据源的配置，因 ProcessEngineConfiguration.createXXX() 方法都将返回 ProcessEngineConfiguration对象，
             // 所以可以使用链式编程的方式直接得到对应的流程引擎对象
             ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("/activiti.cfg.xml").buildProcessEngine();
             log.info("流程引擎对象 ==> " + processEngine);
         }
 }
 ```

- 通过 `ProcessEngines` 获取
```java
public class InitTable{
    /**
     * 初始化工作流所需的数据表方式四
     * 本质还是通过配置文件进行对应的流程引擎数据源的配置和对象bean交由spring进行管理，但是不需要手动获取配置文件，而是使用默认的方式去获取有关配置，此种方式必须创建一个 activiti.cfg.xml 的配置文件，
     * 因为其底层还是通过获取 activiti.cfg.xml 的配置文件获取对应的数据源配置
     */
    public void createTableMethodFour(){
        /**
         * 其底层是 调用 public synchronized static void init()  方法
         * 在该方法中，是通过 ClassLoader classLoader = ReflectUtil.getClassLoader(); 获得对应的 ClassLoader 对象，
         * 再使用如下方式进行获取 activiti.cfg.xml 配置文件中的数据源配置
         * Enumeration<URL> resources = null;
         * try {
         *   resources = classLoader.getResources("activiti.cfg.xml");
         * } catch (IOException e) {
         *   throw new ActivitiIllegalArgumentException("problem retrieving activiti.cfg.xml resources on the classpath: " + System.getProperty("java.class.path"), e);
         * }
         */
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        log.info("流程引擎对象 ===> " + defaultProcessEngine);

    }
}
```

> 1. 获取工作流中的其他业务类
>   - 获取 `RepositoryService` : 
>   - 获取 `DynamicBpmnService` 
>   - 获取 `HistoryService` 
>   - 获取 `ManagementService` 
>   - 获取 `RuntimeService` 
>   - 获取 `TaskService` 

> 注意：在 `activiti 7` 中，相比于 `activiti 5` 和 `activiti 6` 相比，缺少了 `IdentityService` 和 `FormService` ， 但是增加了 `DynamicBpmnService`

```java
public class A{

    public void getService(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        
        // 流程图的部署 修改 删除的服务  对表 act_ge_bytearray act_re_deployment act_re_model act_re_procdef 的操作 
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        DynamicBpmnService dynamicBpmnService = defaultProcessEngine.getDynamicBpmnService();
        
        //查询历史记录的服务 对所有 act_hi_ 开头的表的操作
        HistoryService historyService = defaultProcessEngine.getHistoryService();
        
        //管理器的服务
        ManagementService managementService = defaultProcessEngine.getManagementService();
        
        //流程的运行 对表 act_ru_event_subscr act_ru_execution act_ru_identitylink act_ru_job act_ru_task act_ru_variable
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        TaskService taskService = defaultProcessEngine.getTaskService();
        //在 activiti 7 之前的版本中通过如下方式进行获取 IdentityService 和 FormService
        
        //对工作流的用户管理的表的操作 act_id_group act_id_info act_id_membership act_id_user
        IdentityService identityService = defaultProcessEngine.getIdentityService();
        
        //页面表单的服务器
        FormService formService = defaultProcessEngine.getFormService();

    }
    
}
```

> 2. 流程引擎中的几个对象 
>   - `ProcessDefinition` 流程定义对象，操作数据表 ( `act_re_procdef` )，从这里获取资源文件等；当流程图被部署后，查询出来的数据就是流程定义的数据
>   - `ProcessInstance` 流程实例，操作数据表 ( `act_ru_execution` )，代表流程定义的执行实例，一个流程实例包括了所有的运行节点，可以利用这个流程实例对象来了解当前流程实例的进度等信息，
>一个流程实例就是表示一个流程从开始到结束的最大的流量分支，即一个流程中流程实例只有一个
>   - `Execution` 执行实例，操作数据表 ( `act_ru_execution` )， 在 `activiti` 中，使用这个对象去描述流程执行的每一个节点，
>在没有并发的情况下， `Execution` 就是同 `ProcessInstance` 。流程按照流程定义的规则执行一次的过程。
>就可以表示执行对象 `Execution` 
>   - `TaskInstance` 任务实例， 操作 ( `act_ru_task` )


## 三、第一个 `activiti` `HelloWord`

### 1）、项目流程步骤

#### Ⅰ、画流程图

- 在 `resources` 文件夹下创建一个 `HelloWorld.bpmn` 的文件
- 设置对应的任务节点的执行人和办理人
- 设置整个流程图的 `id` 和 `name`

#### Ⅱ、部署流程定义
> 流程部署有两种方式：
> - 直接将对应的资源文件添加到部署引擎中
> - 将对应的压缩文件 ( `ZIP` ) 提阿难捱到部署引擎中 

> 注意：
> 1. 在资源文件前加 `/` 和不加 `/` 的区别是：加是从根目录下查找; 不加是从当前目录下查找
> 2. 在部署流程的时候，当部署了多个相同 `key` 的流程定义的时候，在使用是会使用最新版本的流程定义，即表 `act_re_procdef` 表中，相同的流程的数据下 `version_` 字段最大的流程

#### Ⅲ、启动流程

#### Ⅳ、查询任务

#### Ⅴ、办理任务


## 四、管理流程定义


