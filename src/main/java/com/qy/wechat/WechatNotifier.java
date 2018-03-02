package com.qy.wechat;


import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Marvin on 16/8/25.
 */
public class WechatNotifier extends Notifier {

    private static final String reportpath = "/apks/api_report/";

    private String jenkinsUrl;

    private String corpid;

    private String corpsecret;

    private String agentid;

    private String link;

    private String file;

    private String memberIds;

    private boolean onStart;

    private boolean onSuccess;

    private boolean onFailed;

    public boolean isOnStart() {
        return onStart;
    }

    public boolean isOnSuccess() {
        return onSuccess;
    }

    public boolean isOnFailed() {
        return onFailed;
    }

    public String getLink() {
        return link;
    }

    public String getFile() {
        return file;
    }

    public String getCorpid() {
        return corpid;
    }

    public String getCorpsecret() {
        return corpsecret;
    }

    public String getAgentid() {
        return agentid;
    }

    public static String getReportpath() {
        return reportpath;
    }

    public String getMemberIds() {
        return memberIds;
    }

    @DataBoundConstructor
    public WechatNotifier(String corpid, String corpsecret, String agentid, String link, String file, String memberIds, boolean onStart, boolean onSuccess, boolean onFailed) {
        this.corpid = corpid;
        this.corpsecret = corpsecret;
        this.memberIds = memberIds;
        this.agentid = agentid;
        this.link = link;
        this.file = file;
        this.onStart = onStart;
        this.onSuccess = onSuccess;
        this.onFailed = onFailed;

    }


    public WechatService newWechatService(AbstractBuild build, TaskListener listener) {
        // 处理构建地址
        Jenkins instance = Jenkins.getInstance();
        jenkinsUrl =  instance.getRootUrl();
        String buildUrl = jenkinsUrl + build.getUrl();
        String link = "";

        // 处理链接
        if (file.endsWith(".html")){
            String buildId = build.getId();
            String report = file.equals("") ? "": file.replace("$BUILD_ID", buildId);
            link = this.link.endsWith("/") ? this.link + report :this.link + "/" + report;
        }
        else if(file.endsWith(".apk")){
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String apk = file.equals("") ? "": file.replace("$DATE", df.format(new Date()));
            link = this.link.endsWith("/") ? this.link + apk :this.link + "/" + apk;
        }

        return new WechatServiceImpl(buildUrl, corpid, corpsecret, agentid, link, memberIds, onStart, onSuccess, onFailed, listener, build);
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        return true;
    }


    @Override
    public DingdingNotifierDescriptor getDescriptor() {
        return (DingdingNotifierDescriptor) super.getDescriptor();
    }

    @Extension
    public static class DingdingNotifierDescriptor extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "企业微信配置";
        }

        public String getDefaultReportUrl() {
            Jenkins instance = Jenkins.getInstance();
            assert instance != null;
            if(instance.getRootUrl() != null){
                return  instance.getRootUrl();
            }else{
                return "";
            }
        }
    }
}
