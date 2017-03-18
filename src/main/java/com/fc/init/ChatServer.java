package com.fc.init;

import com.blade.kit.CollectionKit;
import com.blade.kit.StringKit;
import com.blade.kit.json.JSONKit;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.fc.model.Message;
import com.fc.model.OnlineState;
import com.fc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by biezhi on 2017/1/18.
 */
public class ChatServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatServer.class);

    private com.corundumstudio.socketio.Configuration config;

    public static SocketIOServer server;

    private Random random = new Random();

    // 聊天用户
    public static final Map<String, User> users = CollectionKit.newConcurrentHashMap();

    // 未匹配用户
    public static final List<String> unmatchuser = CollectionKit.newArrayList();

    private String host;
    private int port;

    public ChatServer(String address) {
        this.host = StringKit.split(address, ":")[0];
        this.port = Integer.valueOf(StringKit.split(address, ":")[1]);
    }

    private void initConfig() {
        config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
    }

    public void start() {
        this.initConfig();

        server = new SocketIOServer(config);

        // 单聊
        final SocketIONamespace chat1namespace = server.addNamespace("/single");

        // 发送消息
        chat1namespace.addEventListener("message", Message.class, (client, message, ackRequest) -> {
            LOGGER.info("request msg :" + JSONKit.toJSONString(message));
            try {
                String to_id = message.getTo_id();
                if (StringKit.isNotBlank(to_id)) {
                    User toUser = users.get(to_id);
                    chat1namespace.getClient(toUser.getUuid()).sendEvent("receiveMsg", message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 上线
        chat1namespace.addEventListener("upname", String.class, (client, nick_name, ackSender) -> {
            UUID uuid = client.getSessionId();
            String id = uuid.toString();
            User user = new User(id, uuid, nick_name);
            users.put(id, user);
            OnlineState onlineState = new OnlineState(user, "online", users.size());
            // 通知自己
            chat1namespace.getClient(uuid).sendEvent("online", onlineState);
            unmatchuser.add(id);
        });

        // 新连接
        chat1namespace.addConnectListener(client -> {
            chat1namespace.getBroadcastOperations().sendEvent("changeOnline", new OnlineState(null, "", users.size()));
        });

        // 下线
        chat1namespace.addDisconnectListener(client -> {
            UUID uuid = client.getSessionId();
            String id = uuid.toString();
            User user = users.get(id);
            users.remove(id);
            unmatchuser.remove(id);
            if (null != user) {
                OnlineState onlineState = new OnlineState(user, "offline", users.size());
                // 通知好友
                chat1namespace.getBroadcastOperations().sendEvent("changeOnline", onlineState);
            }
        });

        // 匹配一个好友
        chat1namespace.addEventListener("matach_friend", String.class, ((client, data, ackSender) -> {
            if (unmatchuser.size() > 1) {
                User mine, friend;
                while (true) {
                    String id = client.getSessionId().toString();
                    int pos = random.nextInt(unmatchuser.size());
                    String fid = unmatchuser.get(pos);
                    if (id.equals(fid)) {
                        continue;
                    }
                    mine = users.get(id);
                    friend = users.get(fid);
                    unmatchuser.remove(id);
                    unmatchuser.remove(fid);
                    break;
                }
                if (null != mine && null != friend) {
                    chat1namespace.getClient(client.getSessionId()).sendEvent("matched", friend);
                    chat1namespace.getClient(friend.getUuid()).sendEvent("matched", mine);
                }
            }
        }));

        server.start();
    }

    public static void stop() {
        if (null != server) {
            server.stop();
        }
    }

    public static void startup(){
        if(null != server){
            server.start();
        }
    }
}