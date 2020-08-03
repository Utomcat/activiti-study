package com.ranyk;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * ClassName:InitTable
 * Description:初始化数据库
 *
 * @author ranyi
 * @date 2020-07-29 16:30
 * Version: V1.0
 */
@Slf4j
@SuppressWarnings("all")
public class InitTable {

    public static void main(String[] args) {
        InitTable initTable = new InitTable();

        //initTable.createTableMethodOne();
        //initTable.createTableMethodTwo();
        initTable.createTableMethodThree();

    }

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
