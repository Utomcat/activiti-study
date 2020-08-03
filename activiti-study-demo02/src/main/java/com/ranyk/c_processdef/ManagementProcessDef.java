package com.ranyk.c_processdef;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;

import java.io.InputStream;
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
public class ManagementProcessDef {

    /**
     * 创建流程引擎对象
     */
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    public static void main(String[] args) {
        ManagementProcessDef managementProcessDef = new ManagementProcessDef();
        //部署流程
        //managementProcessDef.deployProcessMethod2();



    }


    /**
     * 流程部署 方式一：
     * 直接将对应的资源文件添加到部署对象中
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
     */
    public void queryProcessDef(){
        //获得资源服务对象
        RepositoryService repositoryService = this.processEngine.getRepositoryService();
        //获得流程部署查询对象
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();

        //对流程部署的查询

        //通过流程部署ID进行查询
        //deploymentQuery.deploymentId(String deploymentId);

        //通过流程部署名称进行查询



    }



}
