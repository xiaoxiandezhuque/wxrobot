package com.xh.robot.bean;

public class WxUserBean {

    private String field_nickname;
    private String field_username;
    private boolean isGroup;

    public WxUserBean(String field_nickname, String field_username, boolean isGroup) {
        this.field_nickname = field_nickname;
        this.field_username = field_username;
        this.isGroup = isGroup;
    }

    public String getField_nickname() {
        return field_nickname;
    }

    public void setField_nickname(String field_nickname) {
        this.field_nickname = field_nickname;
    }

    public String getField_username() {
        return field_username;
    }

    public void setField_username(String field_username) {
        this.field_username = field_username;
    }
}
