package com.fc.model;

/**
 * Created by biezhi on 2017/3/18.
 */
public class Message {

    /**
     * 我的id
     */
    private String id;

    /**
     * 我的昵称
     */
    private String nick_name;

    /**
     * 发送给对方的id
     */
    private String to_id;

    /**
     * 消息内容
     */
    private String msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
