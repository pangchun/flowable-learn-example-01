package cn.pangchun.flowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;

import java.util.HashMap;

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


}
