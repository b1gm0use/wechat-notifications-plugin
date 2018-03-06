package com.qy.wechat.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qy.wechat.Messages;
import com.qy.wechat.module.Text;
import com.qy.wechat.module.TextMessage;
import com.qy.wechat.module.WechatInstallation;
import com.qy.wechat.util.WechatConnector;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Marvin on 16/10/8.
 */
public class WechatServiceImpl implements WechatService {

    private Logger logger = LoggerFactory.getLogger(WechatService.class);

    private String buildurl;

    private WechatInstallation installation;

    private String memberIds;

    private String file;

    private String link;

    private String reportFile;

    private String reportLink;

    private boolean onStart;

    private boolean onSuccess;

    private boolean onFailed;

    private TaskListener listener;

    private AbstractBuild build;

    private String token = "";

    private String proName;

    private String displayName;

    private String currentTime = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss").format(new Date());

    public WechatServiceImpl(String buildurl, String wechatAppName, String memberIds, String file,
                             String link, String reportFile, String reportLink, boolean onStart, boolean onSuccess, boolean onFailed, TaskListener listener,
                             AbstractBuild build) {
        this.buildurl = buildurl;
        this.memberIds = memberIds;
        this.file = file;
        this.link = link;
        this.reportFile = reportFile;
        this.reportLink = reportLink;
        this.onStart = onStart;
        this.onSuccess = onSuccess;
        this.onFailed = onFailed;
        this.listener = listener;
        this.build = build;

        this.proName = build.getProject().getDisplayName();
        this.displayName = build.getDisplayName();
        this.installation = WechatGlobalConfig.get().getWechatInstallationByName(wechatAppName);
    }

    /**
     * 获取测试报告路径
     * 现在测试报告路径以打包的ID分辨,所以这里通过替换$BUILD_ID生成报告路径
     * @return
     */
    private String getReportPath(){
        return reportLink.endsWith("/") ?
                reportLink + reportFile.trim().replace("$BUILD_ID", build.getId()) :
                reportLink + "/" + reportFile.trim().replace("$BUILD_ID", build.getId());
    }

    /**
     * 获取下载路径
     * 现在安卓端打包的后缀都是年月日,所以在这里通过替换$DATE生成文件路径
     * @return
     */
    private String getDownloadPath(){
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return link.endsWith("/") ?
                link + file.trim().replace("$DATE", date) :
                link + "/" + file.trim().replace("$DATE", date);
    }

    @Override
    public void start() {
        if (onStart) {
            sendMessage(
                    String.format(Messages.WechatNotifier_startContent(), proName, displayName));
        }
    }

    @Override
    public void success() {
        if (onSuccess) {
            sendMessage(
                    String.format(Messages.WechatNotifier_successContent(),
                            currentTime, build.getProject().getDisplayName(), displayName, build.getBuildStatusSummary().message, build.getDurationString(),
                            getDownloadPath(), getReportPath()));
        }
    }

    @Override
    public void failed() {
        if (onFailed) {
            sendMessage(
                    String.format(Messages.WechatNotifier_failedContent(),
                            currentTime, build.getProject().getDisplayName(),displayName, build.getBuildStatusSummary().message, build.getDurationString()));
        }
    }

    /**
     * 群发消息
     * @param msg
     */
    private void sendMessage(String msg) {
        listener.getLogger().print(String.format("Send message: %s to [%s]", memberIds, msg));
        TextMessage textMessage = new TextMessage(memberIds, "", "", "text",
                installation.getAgentid(), "0", new Text(msg));

        WechatConnector connector = new WechatConnector(installation.getCorpid(), installation.getCorpsecret());
        try{
            JSONObject responseObject = connector.sendMessage(textMessage);
            listener.getLogger().print(responseObject);
        }catch (IOException e){
            listener.getLogger().print(e.getMessage());
        }
    }
}
