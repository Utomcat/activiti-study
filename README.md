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

### 2）、Database table names explained （activity流程数据库表名说明）

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