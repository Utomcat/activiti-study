package com.ranyk.b_helloworld;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:HelloWorld
 * Description:第一个流程定义对象
 *
 * @author ranyi
 * @date 2020-07-31 11:21
 * Version: V1.0
 */
@Slf4j
public class HelloWorld {


    /**
     * 流程引擎对象
     */
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 任务申请人
     */
    private static String applyAssignee = "张三";

    /**
     * 一审任务人
     */
    private static String firstInstanceAssignee = "李四";

    /**
     * 终审任务人
     */
    private static String finalJudgmentAssignee = "王五";



    public static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld();

        //调用流程部署的方法
        //helloWorld.deployProcess();

        //调用启动流程的方法
        //helloWorld.startProcess();



        //调用查询任务的方法
        List<Task> taskList = helloWorld.findTask(finalJudgmentAssignee);

        //打印任务
        helloWorld.printTaskList(taskList);

        //张三的任务ID 2505
        String zsTaskId = "2505";
        //李四的任务ID
        String lsTaskId = "5002";
        //王五的任务ID
        String wwTaskId = "7502";

        //完成指定的任务
        Map<String, Object> map = helloWorld.completeTask(taskList, wwTaskId);

        for (String key : map.keySet()) {
            System.out.println("map.get(" + key + ") = " + map.get(key));
        }


    }


    /**
     * 流程部署 方式一：
     * 直接将对应的资源文件添加到部署对象中
     */
    public void deployProcess() {
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
        //{id: HelloWorld:1:4, key: HelloWorld, name: HelloWorld }

    }


    /**
     * 启动流程的方法
     */
    public void startProcess() {

        //1. 获取RuntimeService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //2. 启动流程实例
        /**
         * 流程的ID
         */
        String processDefinitionId = "HelloWorld:1:4";
        /**
         * 流程的key
         */
        String processDefinitionKey = "HelloWorld";
        /**
         * 流程的name
         */
        String processDefinitionName = "HelloWorld";

        /**
         * 通过流程ID 启动流程实例
         */
        //ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId);
        /**
         * 通过流程key 启动流程实例
         */
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

        System.out.println("流程启动成功，得到的流程实例为 " + processInstance +
                "， 流程实例ID ==> " + processInstance.getId() +
                "， 流程实例name ==> " + processInstance.getName());
    }


    /**
     * 查询任务
     * @param assignee 任务办理人
     * @return List<Task> 返回该办理人的任务列表
     */
    public List<Task> findTask(String assignee) {

        //1. 获得 TaskService
        TaskService taskService = processEngine.getTaskService();

        /**
         * 查询指定人员的任务
         */
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).list();

        return list;

    }


    /**
     * 输出任务列表
     * @param taskList 需要打印的任务列表
     */
    public void printTaskList(List<Task> taskList){
        if (taskList != null && taskList.size() > 0) {
            int i = 1;
            for (Task task : taskList){
                System.out.println("##################### 任务  " + i + " ######################");
                System.out.println("任务ID: " + task.getId());
                System.out.println("任务执行实例ID: " + task.getExecutionId());
                System.out.println("任务流程定义ID: " + task.getProcessDefinitionId());
                System.out.println("任务流程实例ID: " + task.getProcessInstanceId());
                System.out.println("任务业务键ID: " + task.getBusinessKey());
                System.out.println("任务名称: " + task.getName());
                System.out.println("任务办理人: " + task.getAssignee());
                System.out.println("#######################################################");
                i++;
            }
        }else {

            System.out.println("没有任务！");
        }
    }


    /**
     * 完成任务
     * @param taskList 需要完成的任务列表
     * @param taskId 需要完成任务列表中的任务的任务ID
     * @return 返回完成执行的结果，1：成功; 0：失败;
     */
    public Map<String,Object> completeTask(List<Task> taskList, String taskId){


        Map<String,Object> resultMap = new HashMap<>(16);

        resultMap.put("message","执行成功");
        resultMap.put("result",0);
        resultMap.put("code",200);

        if (null == taskList || taskList.size() == 0){

            System.out.println("没有需要完成的任务！");
            resultMap.put("message","执行成功，没有需要完成的任务！");
            return resultMap;
        }


        taskList.forEach(task -> {
            if (taskId.equals(task.getId())){
                int i = completeTask(taskId);
                resultMap.put("result",i);
            }
        });

        /**
         * result key
         */
        String keyResult = "result";

        if ((Integer) resultMap.get(keyResult) == 0){
            resultMap.put("message","没有指定任务Id的任务需要执行或执行指定任务Id的任务时出现异常，请排查！");
            resultMap.put("result",3);
        }


        return resultMap;
    }



    /**
     * 完成任务
     * @param taskId 需要完成的任务ID
     * @return 返回完成执行的结果，1；成功; 0：失败;
     */
    public int completeTask(String taskId){
        //1. 获取TaskService对象
        TaskService taskService = processEngine.getTaskService();
        try {
            //2. 调用完成业务对象
            taskService.complete(taskId);
            System.out.println("任务完成");
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


}
