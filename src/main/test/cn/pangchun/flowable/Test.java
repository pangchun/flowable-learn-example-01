package cn.pangchun.flowable;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Before;

import java.util.HashMap;
import java.util.List;

public class Test {

    ProcessEngine engine = null;

    /**
     * 创建 流程引擎
     */
    @Before
    public void testCreateProcessEngine() {
        ProcessEngineConfiguration engineConfiguration = new StandaloneProcessEngineConfiguration();
        engineConfiguration
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable?userSSL=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&allowMultiQueries=true")
                .setJdbcUsername("root")
                .setJdbcPassword("112233")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = engineConfiguration.buildProcessEngine();
    }

    /**
     * 部署 流程定义
     * 1、存储xml到数据库
     * 2、流程定义转换为内部可执行对象模型，可用于启动流程实例
     */
    @org.junit.Test
    public void testDeploy() {
        RepositoryService rep = engine.getRepositoryService();
        Deployment dep = rep.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .name("请假流程")
                .deploy();
        System.out.println("dep.getId() = " + dep.getId());
        System.out.println("dep.getName() = " + dep.getName());

    }

    /**
     * 查询 流程部署
     */
    @org.junit.Test
    public void testDeployQuery() {
        RepositoryService rep = engine.getRepositoryService();
        Deployment dep = rep.createDeploymentQuery()
                .deploymentId("2501")
                .singleResult();
        System.out.println("dep.getId() = " + dep.getId());
        System.out.println("dep.getName() = " + dep.getName());

    }

    /**
     * 查询 流程定义
     */
    @org.junit.Test
    public void testProcessDefinitionQuery() {
        RepositoryService rep = engine.getRepositoryService();
        ProcessDefinition proc = rep.createProcessDefinitionQuery()
                .deploymentId("2501")
                .singleResult();
        System.out.println("proc.getId() = " + proc.getId());
        System.out.println("proc.getName() = " + proc.getName());
        System.out.println("proc.getDescription() = " + proc.getDescription());
        System.out.println("proc.getDeploymentId() = " + proc.getDeploymentId());

    }

    /**
     * 启动 流程实例
     */
    @org.junit.Test
    public void testRunProcess() {
        RuntimeService runtimeService = engine.getRuntimeService();
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("employee", "张三");
        variables.put("nrOfHolidays", 3);
        variables.put("description", "工作累了，想出去玩玩");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", variables);
        System.out.println("processInstance.getId() = " + processInstance.getId());
        System.out.println("processInstance.getProcessDefinitionId() = " + processInstance.getProcessDefinitionId());
        System.out.println("processInstance.getActivityId() = " + processInstance.getActivityId());
    }

    /**
     * 查询 任务
     */
    @org.junit.Test
    public void testQueryTask() {
        TaskService taskService = engine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey("holidayRequest")
                .taskAssignee("lisi")
                .list();
        for (Task task : tasks) {
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getName() = " + task.getName());
        }
    }

    /**
     * 完成 任务
     */
    @org.junit.Test
    public void testCompleteTask() {
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holidayRequest")
                .taskAssignee("lisi")
                .singleResult();
        // 添加流程变量
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("approved", false);
        taskService.complete(task.getId(), variables);
    }

    /**
     * 删除 流程
     */
    @org.junit.Test
    public void testDeleteProcess() {
        RepositoryService resp = engine.getRepositoryService();
        // 删除流程定义，如果该流程定义已经有了流程实例启动则删除时报错
        // resp.deleteDeployment("7501");
        // 设置为TRUE 级联删除流程定义，及时流程有实例启动，也可以删除，设置为false 非级联删除操作。
        resp.deleteDeployment("7501",true);
    }

    /**
     * 查询 历史
     */
    @org.junit.Test
    public void testQueryHistory() {
        HistoryService historyService = engine.getHistoryService();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processDefinitionId("holidayRequest:7:35003")
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();
        for (HistoricActivityInstance instance : list) {
            System.out.println("instance.getActivityId() = " + instance.getActivityId() + " - 花费时间 - " + instance.getDurationInMillis() + "毫秒");
        }
    }


}
