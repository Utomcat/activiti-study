package com.ranyk;

import lombok.extern.log4j.Log4j2;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * ClassName:InitTable
 * Description:初始化数据库
 *
 * @author ranyi
 * @date 2020-07-29 16:30
 * Version: V1.0
 */
@Log4j2
@SuppressWarnings("all")
public class InitTable {

    public static void main(String[] args) {
        InitTable initTable = new InitTable();

        initTable.createTableMethodOne();

    }

    /**
     * 初始化流程引擎所需要的数据表  方式一
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
        log.warn("得到的流程引擎对象为=", processEngine);
        log.info("结束时间",System.nanoTime());
    }


}
