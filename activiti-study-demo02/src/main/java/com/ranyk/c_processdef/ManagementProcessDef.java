package com.ranyk.c_processdef;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * ClassName:ManagementProcessDef
 * Description:管理流程定义
 *
 * @author ranyi
 * @date 2020-07-31 17:02
 * Version: V1.0
 */
@Slf4j
@SuppressWarnings("all")
public class ManagementProcessDef {

    /**
     * 创建流程引擎对象
     */
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    public static void main(String[] args) {
        ManagementProcessDef managementProcessDef = new ManagementProcessDef();
        //部署流程
        //managementProcessDef.deployProcessMethod2();

        //流程部署的查询
        //managementProcessDef.queryProcessDeployment();

        //流程定义的查询
        //managementProcessDef.queryProcessDef();

        //查询流程图
        managementProcessDef.queryProcessImg();


    }


    /**
     * 流程部署 方式一：
     * 直接将对应的资源文件添加到部署对象中
     * <p>
     * 操作的数据表有：act_ge_bytearray , act_re_deployment , act_re_procdef , act_ge_property
     */
    public void deployProcessMethod1() {
        //1. 得到流程部署的Service
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //2. 部署流程，设置流程的名称、流程bpmn 和 png 文件 并发布
        Deployment deploy = repositoryService.createDeployment().name("请假流程001")
                .addClasspathResource("process/HelloWorld.bpmn")
                .addClasspathResource("process/HelloWorld.png")
                .deploy();

        log.debug("部署成功，创建的部署对象为 ==> " + deploy + "，" +
                "流程部署的ID为 ==> " + deploy.getId() + "，" +
                "流程部署名称为 ==> " + deploy.getName());

    }


    /**
     * 部署流程 方式二
     * 使用流程资源文件的压缩文件，采用该种方式的话，对应的压缩文件只能是 zip 格式的压缩文件
     * <p>
     * 操作的数据表有：act_ge_bytearray , act_re_deployment , act_re_procdef , act_ge_property
     */
    public void deployProcessMethod2() {
        //得到压缩资源的输入流对象
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/process/HelloWorld.zip");

        System.out.println("resourceAsStream" + resourceAsStream);

        //将输入流对象转换为zip输入流对象
        ZipInputStream zipInputStream = new ZipInputStream(resourceAsStream);
        //获得资源业务对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //创建部署对象并部署流程
        Deployment deploy = repositoryService.createDeployment().name("请假流程001").addZipInputStream(zipInputStream).deploy();

        System.out.println("流程部署成功，部署的流程ID为：" + deploy.getId());

    }

    /**
     * 查询流程部署信息
     * <p>
     * 查询的数据表为：act_re_deployment
     * <p>
     * 查询的内容为：查询条件，查询排序，查询的结果集
     */
    public void queryProcessDeployment() {
        //获得资源服务对象
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        //获得流程部署查询对象
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();

        /**
         * 对流程部署的查询
         */

        //通过流程部署ID进行查询
        //deploymentQuery.deploymentId(String deploymentId);

        //通过流程部署名称进行查询
        //deploymentQuery.deploymentName(String name);

        //通过流程部署名称进行模糊查询
        //deploymentQuery.deploymentNameLike(String nameLike);

        //通过流程部署的key进行查询
        //deploymentQuery.deploymentKey(String key);

        //根据tenantId查询
        //deploymentQuery.deploymentTenantId(String tenantId);

        //根据tenantId进行模糊查询
        //deploymentQuery.deploymentTenantIdLike(String tenantId);

        //根据category查询
        //deploymentQuery.deploymentCategory(String category);

        //根据category进行模糊查询
        //deploymentQuery.deploymentCategoryLike(String categoryLike);

        //根据category进行不匹配查询
        //deploymentQuery.deploymentCategoryNotEquals(String categoryNotEquals);

        /**
         * 对流程信息的排序查询
         */

        //查询部署信息，根据对应的部署ID排序，asc()：方法为升序排序; desc()：方法为降序排序;
        //deploymentQuery.orderByDeploymentId().desc(); //对根据部署ID进行查询的结果进行降序排序
        //deploymentQuery.orderByDeploymentId().asc(); //对根据部署ID进行查询的结果进行升序排序

        //根据指定的查询属性进行排序查询，queryProperty的值可选为：DEPLOYMENT_ID：RES.ID_ ; DEPLOYMENT_NAME：RES.NAME_ ; DEPLOYMENT_TENANT_ID: RES.TENANT_ID_ ; DEPLOY_TIME: RES.DEPLOY_TIME_ ;
        //deploymentQuery.orderBy(QueryProperty property);

        //查询部署信息，根据部署时间进行排序
        //deploymentQuery.orderByDeploymenTime();

        //查询部署信息，根据部署名称进行排序
        //deploymentQuery.orderByDeploymentName();


        /**
         * 获取查询流程信息的结果集
         */

        //插叙部署信息，返回结果集list
        //deploymentQuery.list();

        //分页查询,返回查询的结果集的list
        //deploymentQuery.listPage(int firstResult, int maxResults);

        //返回查询的单个部署信息对象
        //deploymentQuery.singleResult();

        /**
         * 对流程信息的数据量统计
         */

        //查询部署的数据量
        //deploymentQuery.count();

        //注意以上的内容均可以使用链式编程的写法

        /**
         * 示例一：查询部署信息，根据查询出来的部署ID进行排序
         */
        List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymentId().asc().list();

        for (Deployment deployment : list) {
            System.out.println(deployment.toString());
        }


    }


    /**
     * 查询流程部署定义
     * <p>
     * 查询的数据表为：act_re_procdef
     */
    public void queryProcessDef() {

        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        long count = repositoryService.createProcessDefinitionQuery()

                /**
                 * 查询条件
                 */
                //.deploymentId(String deploymentId) // 根据流程部署的ID 进行查询
                //.deploymentIds(Set<String> deploymentIds) // 根据流程部署的ID 集合进行查询 Set<String> deploymentIds
                //.processDefinitionId(String processDefinitionId) // 根据流程定义的ID 进行查询
                //.processDefinitionIds(Set<String> deploymentIds) // 根据流程定义的ID 集合进行查询 Set<String> deploymentIds
                //.processDefinitionCategory(String processDefinitionCategory) // 根据流程定义的 category 属性进行查询
                //.processDefinitionCategoryLike(String processDefinitionCategoryLike) // 根据流程定义的 category 属性进行模糊查询
                //.processDefinitionCategoryNotEquals(String categoryNotEquals) // 根据流程定义的 category 属性进行排除(不等于)查询
                //.processDefinitionName(String processDefinitionName) //根据流程定义的名称进行查询
                //.processDefinitionNameLike(String processDefinitionNameLike) //根据流程定义的名称进行模糊查询
                //.processDefinitionKey(String processDefinitionKey) // 根据流程定义的 Key 进行查询
                //.processDefinitionKeyLike(String processDefinitionKeyLike) // 根据流程定义的 key 进行模糊查询
                //.processDefinitionKeys(Set<String> processDefinitionKeys) // 根据流程定义的 key 的集合进行查询 Set<String> processDefinitionKeys
                //.processDefinitionResourceName(String resourceName) // 根据流程定义的 资源名称(bpmn文件) 进行查询
                //.processDefinitionResourceNameLike(String resourceNameLike) // 根据流程定义的 资源名称(bpmn文件) 进行模糊查询
                //.processDefinitionTenantId(String tenantId) // 根据流程定义的 tenantId 进行查询
                //.processDefinitionTenantIdLike(String tenantIdLike) // 根据流程定义的 tenantid 进行模糊查询
                //.processDefinitionVersion(Integer processDefinitionVersion) // 根据流程定义的版本进行查询
                //.processDefinitionVersionGreaterThan(Integer processDefinitionVersion) // 查询大于指定流程定义版本
                //.processDefinitionVersionGreaterThanOrEquals(Integer processDefinitionVersion) // 查询大于或等于指定流程定义的版本
                //.processDefinitionVersionLowerThan(Integer processDefinitionVersion) // 查询小于指定流程定义版本
                //.processDefinitionVersionLowerThanOrEquals(Integer processDefinitionVersion) // 查询小于或等于指定流程定义的版本
                //.latestVersion() // 查询最后的一个版本(最新版本的流程定义)

                /**
                 * 对流程定义查询结果的排序 asc()：升序; desc()：降序;
                 */
                //根据指定的属性名进行排序：参数值为：
                // PROCESS_DEFINITION_KEY  :  RES.KEY_ ;
                // PROCESS_DEFINITION_CATEGORY  :  RES.CATEGORY_ ;
                // PROCESS_DEFINITION_ID  :  RES.ID_ ;
                // PROCESS_DEFINITION_VERSION  :  RES.VERSION_ ;
                // PROCESS_DEFINITION_APP_VERSION  :  RES.APP_VERSION_ ;
                // PROCESS_DEFINITION_NAME  :  RES.NAME_ ;
                // DEPLOYMENT_ID  :  RES.DEPLOYMENT_ID_ ;
                // PROCESS_DEFINITION_TENANT_ID  :  RES.TENANT_ID_ ;
                //.orderBy(ProcessDefinitionQueryProperty.DEPLOYMENT_ID)

                //.orderByDeploymentId() // 根据部署ID进行排序
                //.orderByProcessDefinitionAppVersion() //根据流程定义的app_version 进行排序
                //.orderByProcessDefinitionCategory()//根据流程定义的category进行排序
                //.orderByProcessDefinitionKey()//根据流程定义的key进行排序
                //.orderByProcessDefinitionName()//根据流程定义的name进行排序
                //.orderByProcessDefinitionVersion()//根据流程定义的version进行排序
                //.orderByTenantId()//根据tenantid进行排序
                //.orderByProcessDefinitionId()//根据流程定义的ID进行排序

                /**
                 * 查询结果集
                 */
                //.list() //查询结果集list
                //.listPage(int firstResult, int maxResults) //分页查询
                //.singleResult() //查询单个结果集

                /**
                 * 计数
                 */
                .count();

        System.out.println("流程定义的计数 ===> " + count);


    }


    /**
     * 删除流程定义
     */
    public void deleteProcessDef(){
        RepositoryService repositoryService = this.processEngine.getRepositoryService();

        //根据流程部署的ID删除流程定义，注意执行该方法时，如果删除的流程定义存在正在执行的对象，那么该流程定义将会删除失败，程序抛错
        //repositoryService.deleteDeployment(String deploymentId);

        //根据指定的部署ID删除流程定义，后面的Boolean类型的参数是表示当存在执行的流程对象时，能否继续删除; true: 能删除; false: 不能删除;
        // 注意此时的删除是删除 act_ru_* 和 act_hi_* 中的数据表
        //repositoryService.deleteDeployment(String deploymentId, boolean cascade);

    }


    /**
     * 更新流程定义：在activiti 中没有直接修改流程定义的方法，因为在更新的时候可能存在之前的流程存在正在使用的情况，所以更新流程定义，
     * 其实是重新创建一个相同的 key 和 name 的流程定义，但是其版本号会加一，这样就可以在不影响原流程的情况下更新流程定义
     */
    public void updateProcessDef(){

    }


    /**
     * 查询流程图
     */
    public void queryProcessImg(){

        RepositoryService repositoryService = this.processEngine.getRepositoryService();

        String processDefinitionId = "HelloWorld:2:12504";
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
        String diagramResourceName = processDefinition.getDiagramResourceName();


        /**
         * 获得对应流程的bpmn文件
         */
        InputStream processModel = repositoryService.getProcessModel(processDefinitionId);
        String fileName = repositoryService.getProcessDefinition(processDefinitionId).getResourceName();
        //log.info("流程图名称为 ===> "+fileName);
        File bpmFile = new File("E:\\Work\\IdeaWorkSpace\\activiti\\activiti-study\\activiti-study-demo01\\model\\" + fileName);
        try {
            BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(bpmFile));
            int length = 0;
            byte[] b = new byte[1024];
            while ((length=processModel.read(b)) != -1){
                bufferedOutputStream2.write(b,0,length);
                bufferedOutputStream2.flush();
            }
            bufferedOutputStream2.close();
            processModel.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }

    }

}
