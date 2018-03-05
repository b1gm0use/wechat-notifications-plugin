package com.qy.wechat.plugin;


import com.qy.wechat.Messages;
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

/**
 * Created by Marvin on 16/8/25.
 */
public class WechatNotifier extends Notifier {

    private String corpid;

    private String corpsecret;

    private String agentid;

    private String link;

    private String file;

    private String memberIds;

    private String reportFile;

    private String reportLink;

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

    public String getMemberIds() {
        return memberIds;
    }

    public String getReportFile() {
        return reportFile;
    }

    public String getReportLink() {
        return reportLink;
    }

    @DataBoundConstructor
    public WechatNotifier(String corpid, String corpsecret, String agentid, String link, String file, String memberIds,
                          String reportFile, String reportLink, boolean onStart, boolean onSuccess, boolean onFailed) {
        this.corpid = corpid;
        this.corpsecret = corpsecret;
        this.agentid = agentid;
        this.link = link;
        this.file = file;
        this.memberIds = memberIds;
        this.reportFile = reportFile;
        this.reportLink = reportLink;
        this.onStart = onStart;
        this.onSuccess = onSuccess;
        this.onFailed = onFailed;
    }

    public WechatService newWechatService(AbstractBuild build, TaskListener listener) {
        return new WechatServiceImpl(Jenkins.getInstance().getRootUrl() + build.getUrl(), corpid, corpsecret, agentid, memberIds, file,
                link, reportFile, reportLink, onStart, onSuccess, onFailed, listener, build);

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
    public WechatNotifierDescriptor getDescriptor() {
        return (WechatNotifierDescriptor) super.getDescriptor();
    }

    @Extension
    public static class WechatNotifierDescriptor extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.WechatNotifier_pluginName();
        }

        public String getDefaultLink() {
            Jenkins instance = Jenkins.getInstance();
            return instance.getRootUrl() != null ? instance.getRootUrl() : "";
        }
    }
}
