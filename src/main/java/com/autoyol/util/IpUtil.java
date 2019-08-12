package com.autoyol.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;


public class IpUtil {
    /**
     * 获取用户真实IP地址
     *
     * 四层负载均衡（TCP协议）服务可以直接在后端ECS上获取客户端的真实IP地址，无需进行额外的配置。
     * 七层负载均衡（HTTP/HTTPS协议）服务需要对应用服务器进行配置，然后使用X-Forwarded-For的方式获取客户端的真实IP地址。
     * 真实的客户端IP会被负载均衡放在HTTP头部的X-Forwareded-For字段，格式如下： X-Forwarded-For: 用户真实IP,
     * 代理服务器1-IP， 代理服务器2-IP，...
     **/
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (StringUtils.isNotBlank(ip) && StringUtils.indexOf(ip, ",") != -1) {
            ip = ip.split(",")[0];
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
