package com.qy.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qy.wechat.Messages;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;

/**
 * Created by Administrator on 2018/3/6.
 */
public class WechatConnector {

    private String token = "";

    private HttpClient client;

    private String corpid;

    private String corpsecret;

    public WechatConnector(String corpid, String corpsecret) {
        this.corpid = corpid;
        this.corpsecret = corpsecret;
        this.client = getHttpClient();
    }

    /**
     * 发送信息
     * @param content
     * @param <T>
     * @throws IOException
     */
    public <T> JSONObject sendMessage(T content) throws IOException{
        if (this.token.equals("")){
            updateToken();
        }
        String api = String.format(Messages.WechatNotifier_sendMsgApi(), token);
        PostMethod post = new PostMethod(api);
        String body = JSON.toJSONString(content);
        post.setRequestEntity(new StringRequestEntity(body, "application/json", "UTF-8"));
        client.executeMethod(post);
        JSONObject responseObject = JSONObject.parseObject(post.getResponseBodyAsString());
        post.releaseConnection();
        return responseObject;
    }

    /**
     * 更新access_token
     * @throws IOException
     */
    public JSONObject updateToken() throws IOException{
        String api = String.format(Messages.WechatNotifier_updateTokenApi(), corpid, corpsecret);
        GetMethod get = new GetMethod(api);
        client.executeMethod(get);
        JSONObject responseObject = JSONObject.parseObject(get.getResponseBodyAsString());
        this.token = (String)responseObject.get("access_token");
        get.releaseConnection();
        return responseObject;
    }

    public HttpClient getHttpClient(){
        return new HttpClient();
    }
}
