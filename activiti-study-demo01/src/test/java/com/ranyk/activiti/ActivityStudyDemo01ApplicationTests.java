package com.ranyk.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ActivityStudyDemo01ApplicationTests {

    DataSourceProperties dataSourceProperties = new DataSourceProperties();

    @Test
    void contextLoads() {
    }

    @Test
    void initTable(){
        ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        engineConfiguration.setJdbcDriver(dataSourceProperties.getDriverClassName());
        engineConfiguration.setJdbcUrl(dataSourceProperties.getUrl());
        engineConfiguration.setJdbcUsername(dataSourceProperties.getUsername());
        engineConfiguration.setJdbcPassword(dataSourceProperties.getPassword());
        engineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
        log.info(processEngine.toString());
    }

}
