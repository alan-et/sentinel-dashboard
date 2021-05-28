package com.alibaba.csp.sentinel.dashboard.rule.zk;

public final class ZkPathUtil {
    public static final String GROUP_ID = "SENTINEL_GROUP";
    private static final String ZK_PATH_SEPARATOR = "/";

    private ZkPathUtil() {
    }

    /**
     * /groupId/{app}/authority
     *
     * @param app app name
     * @return zk path
     */
    public static String getAuthorityRuleZkPath(String app) {
        return    ZK_PATH_SEPARATOR + GROUP_ID
                + ZK_PATH_SEPARATOR + app
                + ZK_PATH_SEPARATOR + "authority"
                ;
    }

    /**
     * /groupId/{app}/flow
     *
     * @param app app name
     * @return zk path
     */
    public static String getFlowRuleZkPath(String app) {
        return    ZK_PATH_SEPARATOR + GROUP_ID
                + ZK_PATH_SEPARATOR + app
                + ZK_PATH_SEPARATOR + "flow"
                ;
    }

    /**
     * /groupId/{app}/param-flow
     *
     * @param app app name
     * @return zk path
     */
    public static String getParamFlowRuleZkPath(String app) {
        return    ZK_PATH_SEPARATOR + GROUP_ID
                + ZK_PATH_SEPARATOR + app
                + ZK_PATH_SEPARATOR + "param-flow"
                ;
    }
}
