package cn.pangchun.flowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

public class Main {
    public static void main(String[] args) {
        ProcessEngineConfiguration engineConfiguration = new StandaloneProcessEngineConfiguration();
        engineConfiguration
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable?userSSL=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&allowMultiQueries=true")
                .setJdbcUsername("root")
                .setJdbcPassword("112233")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine engine = engineConfiguration.buildProcessEngine();

    }
}
