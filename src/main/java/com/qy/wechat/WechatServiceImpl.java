package com.qy.wechat;

import com.alibaba.fastjson.JSONObject;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Marvin on 16/10/8.
 */
public class WechatServiceImpl implements WechatService {

    private Logger logger = LoggerFactory.getLogger(WechatService.class);

    private String buildurl;

    private String corpid;

    private String corpsecret;

    private String agentid;


    private String prefix;

    private String memberIds;

    private boolean onStart;

    private boolean onSuccess;

    private boolean onFailed;

    private TaskListener listener;

    private AbstractBuild build;

    private String link;

    private static final String updateTokenApi = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    private static final String sendMsgApi = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    private String token = "";

    private String currentTime;

    public WechatServiceImpl(String buildurl, String corpid, String corpsecret, String agentid, String link, String memberIds, boolean onStart, boolean onSuccess, boolean onFailed, TaskListener listener, AbstractBuild build) {
        this.buildurl = buildurl;
        this.corpid = corpid;
        this.corpsecret = corpsecret;
        this.memberIds = memberIds;
        this.agentid = agentid;
        this.onStart = onStart;
        this.onSuccess = onSuccess;
        this.onFailed = onFailed;
        this.listener = listener;
        this.link = link;
        this.build = build;
        // 记录当前时间
        SimpleDateFormat format = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss");
        currentTime = format.format(new Date());
    }

    private void sendMsg(String link, String content, String title){
        if (this.token.equals("")){
            updateToken();
        }
        logger.info("send link msg from " + listener.toString());
        listener.getLogger().println("send link msg from " + listener.toString());
        sendLinkMessage(link, content, title);
    }

    @Override
    public void start() {
        String title = String.format("%s%s开始构建", build.getProject().getDisplayName(), build.getDisplayName());
        String content = String.format("项目[%s%s]开始构建", build.getProject().getDisplayName(), build.getDisplayName());
        if (onStart) {
            sendMsg(buildurl, content, title);
        }

    }

    @Override
    public void success() {
        String title = String.format("%s%s构建成功", build.getProject().getDisplayName(), build.getDisplayName());
        String content = String.format("<div class=\"gray\">%s</div><div class=\"normal\">项目[%s%s]构建成功, summary:%s, duration:%s</div><div class=\"highlight\">点击查看测试报告或下载安装包</div>",
                currentTime, build.getProject().getDisplayName(), build.getDisplayName(), build.getBuildStatusSummary().message, build.getDurationString());
        if (onSuccess) {
            sendMsg(link, content, title);
        }
    }

    @Override
    public void failed() {
        String title = String.format("%s%s构建失败", build.getProject().getDisplayName(), build.getDisplayName());
        String content = String.format("<div class=\"gray\">%s</div><div class=\"normal\">项目[%s%s]构建失败, summary:%s, duration:%s</div>",
                currentTime, build.getProject().getDisplayName(), build.getDisplayName(), build.getBuildStatusSummary().message, build.getDurationString());
        if (onFailed) {
            sendMsg(buildurl, content, title);
        }
    }

    private void sendLinkMessage(String link, String msg, String title) {
        HttpClient client = getHttpClient();
        PostMethod post = new PostMethod(String.format(sendMsgApi, token));

        JSONObject body = new JSONObject();
        body.put("touser", memberIds);
        body.put("toparty", "");
        body.put("totag", "");
        body.put("msgtype", "textcard");
        body.put("agentid", agentid);
        body.put("safe", 0);

        JSONObject textcard = new JSONObject();
        textcard.put("title", title);
        textcard.put("description", msg);
        textcard.put("url", link);
        textcard.put("btntxt", "地址");

        body.put("textcard", textcard);
        try {
            logger.info("Send msg:" + body.toJSONString());
            listener.getLogger().println("Send msg:" + body.toJSONString());
            post.setRequestEntity(new StringRequestEntity(body.toJSONString(), "application/json", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("build request error", e);
            listener.getLogger().println("build request error: " + e);
        }
        try {
            client.executeMethod(post);
            logger.info(post.getResponseBodyAsString());
        } catch (IOException e) {
            listener.getLogger().println("build request error: " + e);
            logger.error("send msg error", e);
        }
        post.releaseConnection();
    }

    private void updateToken(){
        HttpClient client = getHttpClient();
        GetMethod get = new GetMethod(String.format(updateTokenApi, corpid, corpsecret));
        try {
            client.executeMethod(get);
            logger.info(get.getResponseBodyAsString());
            listener.getLogger().println(get.getResponseBodyAsString());
            String responseBody = get.getResponseBodyAsString();
            JSONObject object = JSONObject.parseObject(responseBody);
            this.token = (String)object.get("access_token");
        }catch (IOException e){
            listener.getLogger().println("build request error: " + e);
            logger.error("get token error", e);
        }
    }

    private HttpClient getHttpClient() {
        HttpClient client = client = new HttpClient();
        return client;
    }
}
