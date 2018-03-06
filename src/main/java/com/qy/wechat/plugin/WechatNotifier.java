package com.qy.wechat.plugin;


import com.qy.wechat.Messages;
import com.qy.wechat.module.WechatInstallation;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;

/**
 * Created by Marvin on 16/8/25.
 */
public class WechatNotifier extends Notifier {

    private String wechatAppName;

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

    public String getMemberIds() {
        return memberIds;
    }

    public String getReportFile() {
        return reportFile;
    }

    public String getReportLink() {
        return reportLink;
    }

    public String getWechatAppName() {
        return wechatAppName;
    }

    @DataBoundConstructor
    public WechatNotifier(String wechatAppName, String link, String file, String memberIds,
                          String reportFile, String reportLink, boolean onStart, boolean onSuccess, boolean onFailed) {
        this.wechatAppName = wechatAppName;
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
        return new WechatServiceImpl(Jenkins.getInstance().getRootUrl() + build.getUrl(), wechatAppName,
                memberIds, file, link, reportFile, reportLink, onStart, onSuccess, onFailed, listener, build);

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

        public FormValidation doCheckWechatAppName(@QueryParameter String value){
            if (value.length() == 0){
                return FormValidation.error("Paramter wechatAppName must not be null");
            }
            return FormValidation.ok();
        }

        public FormValidation doCheckMemberIds(@QueryParameter String value){
            if (value.length() == 0){
                return FormValidation.error("Paramter memberIds must not be null");
            }
            return FormValidation.ok();
        }

        public ListBoxModel doFillWechatAppNameItems(){
            ListBoxModel items = new ListBoxModel();
            items.add(" - None - ");
            for(WechatInstallation installation: WechatGlobalConfig.get().getWechatInstallation()){
                items.add(installation.getWechatAppName());
            }
            return items;
        }
    }
}
