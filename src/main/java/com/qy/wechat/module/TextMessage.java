package com.qy.wechat.module;

/**
 * Created by Administrator on 2018/3/5.
 */
public class TextMessage {

    private String touser;

    private String toparty;

    private String totag;

    private String msgtype;

    private String agentid;

    private String safe;

    private Text text;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getSafe() {
        return safe;
    }

    public void setSafe(String safe) {
        this.safe = safe;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public TextMessage(String touser, String toparty, String totag, String msgtype, String agentid, String safe, Text text) {
        this.touser = touser;
        this.toparty = toparty;
        this.totag = totag;
        this.msgtype = msgtype;
        this.agentid = agentid;
        this.safe = safe;
        this.text = text;
    }
}
