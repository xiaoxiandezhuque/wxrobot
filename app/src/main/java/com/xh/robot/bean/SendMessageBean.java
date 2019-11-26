package com.xh.robot.bean;

public class SendMessageBean {

    private String wxid;
    private String content;

    public SendMessageBean(String wxid, String content) {
        this.wxid = wxid;
        this.content = content;
    }

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
