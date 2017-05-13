package com.dcc668.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;
@Component
public class AccessTokenUtil {
    @Autowired
    private JedisClient jedisClient;

    private void fetchAccessToken() throws Exception {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=###APPID&secret=###APPSECRET";
        url = url.replace("###APPID", "wx5eaa517fba790d1e").replace("###APPSECRET", "bfbd8349e5da059621a7e8b143881c7d");
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String responseBody = EntityUtils.toString(httpEntity, "UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> resultMap = objectMapper.readValue(responseBody, Map.class);
        String access_token = resultMap.get("access_token");
        RedisUtil.setValue("wx_access_token", access_token, 7000, TimeUnit.SECONDS);
        RedisUtil.setValue("wx_access_token_object", resultMap, 7000, TimeUnit.SECONDS);
        jedisClient.set("wx_access_token_1", access_token, 7000);
        jedisClient.setObject("wx_access_token_1_object", resultMap, 7000);
    }

    public String getAccessToken() throws Exception {
        String access_token = RedisUtil.getValue("wx_access_token");
        while (StringUtils.isEmpty(access_token)) {
            fetchAccessToken();
            access_token = RedisUtil.getValue("wx_access_token");
        }
        return access_token;
    }
}
