package com.linwen.aop;

import com.linwen.comm.base.BaseController;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpRolePermitted {
    public static Object isRolePermitted(ProceedingJoinPoint pjp) {
        Object object = pjp.getTarget();
        if (object instanceof BaseController) {
            BaseController baseController = (BaseController) object;
            String globalIP = baseController.getGlobalIP();
            if (baseController.getHttpServletRequest() != null && globalIP != null && !"".equals(globalIP) && ipPermitted(globalIP, getIpAddr(baseController.getHttpServletRequest()))) {
                return baseController.JsonFailResult("", 4, "此ip禁止访问");
            }
        }
        return null;
    }

    public static void main(String[] args) {
        boolean ret = ipPermitted("127.1.1.*", "127.1.1.3");
        if (ret) {
            System.out.println("==============1");
        } else {
            System.out.println("==============2");
        }
    }

    public static String getIpAddr(HttpServletRequest request) {

        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 && !"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();

                // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
                if (ip != null && ip.length() > 15) { // "***.***.***.***".length()
                    // = 15
                    if (ip.indexOf(",") > 0) {
                        // ip = ip.substring(0, ip.indexOf(","));
                    }
                }
                if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress[] inet = null;
                    try {
                        inet = InetAddress.getAllByName(InetAddress
                                .getLocalHost().getHostName());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    for (InetAddress i : inet) {
                        if (i.getHostAddress().length() < 16)
                            ip += "," + i.getHostAddress();
                    }
                }
            }
        }
        return ip;
    }

    public static boolean ipPermitted(String ip, String ipTag) {
        boolean isboolean = false;
        String[] tagValue = ipTag.split("\\.");
        String[] ipStr = ip.split(",");
        for (int i = 0; i < ipStr.length; i++) {
            String ipTemp = ipStr[i];
            String[] temp = ipTemp.split("\\.");
            if (tagValue.length == temp.length) {
                boolean per = false;
                for (int j = 0; j < temp.length; j++) {
                    if (temp[j].equals(tagValue[j]) || temp[j].equals("*")) {
                        per = true;
                    } else {
                        per = false;
                        break;
                    }
                }
                if (per) {
                    isboolean = true;
                }
            }
            if (isboolean) {
                break;
            }
        }
        return isboolean;
    }
}
