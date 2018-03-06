package com.qy.wechat.module;

import com.alibaba.fastjson.JSONObject;
import com.qy.wechat.util.WechatConnector;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;

/**
 * Created by Administrator on 2018/3/6.
 */
public class WechatInstallation extends AbstractDescribableImpl<WechatInstallation> {

    private String wechatAppName;

    private String corpid;

    private String corpsecret;

    private String agentid;

    public String getWechatAppName() {
        return wechatAppName;
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

    @DataBoundConstructor
    public WechatInstallation(String wechatAppName, String corpid, String corpsecret, String agentid) {
        this.wechatAppName = wechatAppName;
        this.corpid = corpid;
        this.corpsecret = corpsecret;
        this.agentid = agentid;
    }

    @Extension
    public static class WechatInstallationDescriptor extends Descriptor<WechatInstallation>{
        @Override
        public String getDisplayName() {
            return "企业微信Installation";
        }

        public FormValidation doTestConnection(
                @QueryParameter("corpid") final String corpid,
                @QueryParameter("corpsecret") final String corpsecret){

            if (corpid.trim().equals("") || corpid == null
                    || corpsecret.trim().equals("") || corpsecret == null
                    ){
                return FormValidation.error("Unable to make wechat request: corpid, corpsecret must not be null");
            }

            WechatConnector connector = new WechatConnector(corpid, corpsecret);
            try{
                JSONObject object = connector.updateToken();
                int code = (Integer) object.get("errcode");
                if (code != 0){
                    return FormValidation.error("Unable to make wechat request: " + object.get("errmsg"));
                }
            }catch (IOException e){
                return FormValidation.error("Make wechat request exception: " + e.getMessage());
            }
            return FormValidation.ok("Make wechat request success!");
        }
    }
}
