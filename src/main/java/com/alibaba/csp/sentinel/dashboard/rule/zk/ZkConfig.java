package com.alibaba.csp.sentinel.dashboard.rule.zk;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.util.IdGenerator;
import com.alibaba.csp.sentinel.dashboard.util.TimeIdGenerator;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConditionalOnProperty(value = "dynamic.rules.source.type", havingValue = "zookeeper")
@Configuration
@EnableConfigurationProperties(ZkProperties.class)
public class ZkConfig {
    private static Logger log = LoggerFactory.getLogger(ZkConfig.class);

    private static final int DEFAULT_ZK_SESSION_TIMEOUT = 30000;
    private static final int DEFAULT_ZK_CONNECTION_TIMEOUT = 10000;
    private static final int RETRY_TIMES = 3;
    private static final int SLEEP_TIME = 1000;


    public ZkConfig() {
        log.info("============== Use Zookeeper Dynamic Rules Source ===================");
    }

    @Bean
    public Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    @Bean
    public Converter<List<ParamFlowRuleEntity>, String> paramFlowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<ParamFlowRuleEntity>> paramFlowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, ParamFlowRuleEntity.class);
    }

    @Bean
    public Converter<List<AuthorityRuleEntity>, String> authorityRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<AuthorityRuleEntity>> authorityRuleEntityDecoder() {
        return s -> JSON.parseArray(s, AuthorityRuleEntity.class);
    }


    @Bean("flowRuleDefaultProvider")
    public FlowRuleZkProvider flowRuleZookeeperProvider() {
        return new FlowRuleZkProvider();
    }

    @Bean("flowRuleDefaultPublisher")
    public FlowRuleZkPublisher flowRuleZookeeperPublisher() {
        return new FlowRuleZkPublisher();
    }

    @Bean
    public ParamFlowRuleZkProvider paramFlowRuleZkProvider() {
        return new ParamFlowRuleZkProvider();
    }

    @Bean
    public ParamFlowRuleZkPublisher paramFlowRuleZkPublisher() {
        return new ParamFlowRuleZkPublisher();
    }

    @Bean
    public IdGenerator<Integer> idGenerator() {
        return new TimeIdGenerator();
    }


    /**
     * zk client
     */
    @Bean(destroyMethod = "close")
    public CuratorFramework zkClient(ZkProperties properties) {
        String connectString = properties.getConnectString();
        int sessionTimeout = DEFAULT_ZK_SESSION_TIMEOUT;
        int connectionTimeout = DEFAULT_ZK_CONNECTION_TIMEOUT;
        int idx = connectString.indexOf("//");
        if (idx > 0) {
            connectString = connectString.substring(idx + 2).trim();
        }
        if (properties.getSessionTimeout() > 0) {
            sessionTimeout = properties.getSessionTimeout();
        }
        if (properties.getConnectionTimeout() > 0) {
            connectionTimeout = properties.getConnectionTimeout();
        }

        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(connectString,
                sessionTimeout, connectionTimeout,
                new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES));
        zkClient.start();

        log.info("Initialize zk client CuratorFramework, connectString={}, sessionTimeout={}, connectionTimeout={}, retry=[sleepTime={}, retryTime={}]",
                connectString, sessionTimeout, connectionTimeout, SLEEP_TIME, RETRY_TIMES);
        return zkClient;
    }
}
