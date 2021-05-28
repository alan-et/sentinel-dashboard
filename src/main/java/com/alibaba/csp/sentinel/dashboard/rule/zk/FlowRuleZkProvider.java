package com.alibaba.csp.sentinel.dashboard.rule.zk;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FlowRuleZkProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {

    @Autowired
    private CuratorFramework zkClient;

    @Autowired
    private Converter<String, List<FlowRuleEntity>> converter;

    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        byte[] data = zkClient.getData().forPath(ZkPathUtil.getFlowRuleZkPath(appName));
        if (data == null || data.length == 0) {
            return new ArrayList<>();
        }
        return converter.convert(new String(data, StandardCharsets.UTF_8));
    }
}
