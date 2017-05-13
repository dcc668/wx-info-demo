package com.dcc668.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.dcc668.service.CoreService;
import com.dcc668.utils.AccessTokenUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 从微信公众平台接口消息指南中可以了解到，当用户向公众帐号发消息时，
 * 微信服务器会将消息通过POST方式提交给我们在接口配置信息中填写的URL，
 * 而我们就需要在URL所指向的请求处理类CoreServlet的doPost方法中接收消息、
 * 处理消息和响应消息。
 */
@Controller
@RequestMapping(value = "/weixin")
public class WXTestController {
    private static Logger log = Logger.getLogger(WXTestController.class);
    @Autowired
    private AccessTokenUtil accessTokenUtil;

    @RequestMapping(value = "/index.do", method = RequestMethod.GET)
    public Object serverDo(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        log.error("------------>验证中>>>>");
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        String token = "mytaken123";

        String arr[] = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);

        StringBuffer stringBuffer = new StringBuffer();

        for (String str : arr) {
            stringBuffer.append(str);
        }

        String sign = DigestUtils.sha1Hex(stringBuffer.toString());

        if (signature.equals(sign)) {
            response.getWriter().write(echostr);
        } else {
            response.getWriter().write("wrong");
            log.error("验证未通过！");
        }

        return null;
    }

    @RequestMapping(value = "/index.do", method = RequestMethod.POST)
    public Object serverDoPost(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        // 调用核心业务类接收消息、处理消息
        String respMessage = CoreService.processRequest(request);

        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(respMessage);
        out.close();
        return null;
    }

    @RequestMapping(value = "/accessToken", method = RequestMethod.GET)
    @ResponseBody
    public Object getAccessToken() throws Exception {
        return accessTokenUtil.getAccessToken();
    }

    @RequestMapping(value = "/shortUrl", method = RequestMethod.GET)
    @ResponseBody
    public Object getShortUrl(String originUrl) throws Exception {
        String access_token = accessTokenUtil.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=###ACCESS_TOKEN";
        url = url.replace("###ACCESS_TOKEN", access_token);
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        JSONObject requsetbodyJSON = new JSONObject();
        requsetbodyJSON.put("action", "long2short");
        requsetbodyJSON.put("long_url", originUrl);
        httpPost.setEntity(new StringEntity(requsetbodyJSON.toJSONString(), "UTF-8"));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        JSONObject json = JSONObject.parseObject(IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8"));
        //return json.getString("short_url");
        return json.toString();
    }
}
