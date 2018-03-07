package com.qy.wechat.plugin;

import com.qy.wechat.module.WechatInstallation;
import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 *
 */

@Extension
public class WechatGlobalConfig extends GlobalConfiguration {

    private List<WechatInstallation> wechatInstallations = new ArrayList<>();

    public List<WechatInstallation> getWechatInstallation() {
        return wechatInstallations;
    }

    public void setWechatInstallation(List<WechatInstallation> wechatInstallations) {
        this.wechatInstallations = wechatInstallations;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this, json);
        save();
        return true;
    }

    public static WechatGlobalConfig get(){
        return GlobalConfiguration.all().get(WechatGlobalConfig.class);
    }

    public WechatInstallation getWechatInstallationByName(String name){
        for(WechatInstallation installation: wechatInstallations){
            if (installation.getWechatAppName().equals(name)){
                return installation;
            }
        }
        return null;
    }
}
