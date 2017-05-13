package com.dcc668.demo.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by lhwarthas on 16/10/20.
 */
@Controller
public class UserInfoController {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

    private static String appid = resourceBundle.getString("appid");
    private static String appsecret = resourceBundle.getString("appsecret");



    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String getWXUserInfo(HttpServletRequest request, HttpServletResponse response, Model model,String code) throws Throwable {
        System.out.println(getIpAddress(request));

        Date start = new Date();
        System.out.println(code);

        //获取网页授权access_token
        String access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + appid + "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code";

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(access_token_url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String responseBody = EntityUtils.toString(httpEntity, "UTF-8");
        System.out.println(responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> resultMap = objectMapper.readValue(responseBody, Map.class);

        if (null != resultMap.get("errcode")) {
            response.getWriter().write(new String("您当前的登录已过期，请关闭窗口从微信重新打开此链接".getBytes(),"utf-8"));
            Date stop = new Date();
            System.out.println("接口调用时间为：" + (stop.getTime() - start.getTime()));
            return null;
        }

        String access_token = resultMap.get("access_token");
        String openid = resultMap.get("openid");


        //获取UserInfo
        String user_info_url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
        httpGet = new HttpGet(user_info_url);
        httpResponse = httpClient.execute(httpGet);
        httpEntity = httpResponse.getEntity();
        responseBody = EntityUtils.toString(httpEntity, "UTF-8");
        System.out.println(responseBody);

        resultMap = objectMapper.readValue(responseBody, Map.class);

        Date stop = new Date();

        System.out.println("接口调用时间为：" + (stop.getTime() - start.getTime()));

        model.addAttribute("userInfo", resultMap);
        // 跳转到userInfo.jsp
        return "/userInfo";
    }


    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     *
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     *
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
