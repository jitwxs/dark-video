package jit.wxs.dv.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/ws/{sessionId}")
@Component
public class WebSocketServer {

    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<>();

    private String httpSessionId;

    /**
     * 连接建立成功
     * @author jitwxs
     * @since 2018/10/10 9:44
     */
    @OnOpen
    public void onOpen(@PathParam("sessionId") String sessionId, Session session) {
        this.httpSessionId = sessionId;
        webSocketMap.put(sessionId, session);
//        log.info("【WebSocket】客户端：{} 加入连接！", sessionId);
    }

    /**
     * 连接关闭
     * @author jitwxs
     * @since 2018/10/10 9:45
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(this.httpSessionId);
//        log.info("【WebSocket】客户端：{} 关闭连接！", this.httpSessionId);

    }

    /**
     * 收到客户端消息
     * @author jitwxs
     * @since 2018/10/10 9:45
     */
    @OnMessage
    public void onMessage(String message) {
        // 如果客户端发送心跳包，返回心跳包
        if("ping".equals(message)) {
            sendMessage("pong");
        }
    }

    /**
     * 发生错误
     * @author jitwxs
     * @since 2018/10/10 9:46
     */
    @OnError
    public void onError(Throwable error) {
        log.info("【WebSocket】客户端：{} 发生错误，错误信息：", this.httpSessionId, error);
    }


    /**
     * 对当前客户端发送消息
     * @author jitwxs
     * @since 2018/10/10 9:49
     */
    public void sendMessage(String message) {
        Session session = webSocketMap.get(this.httpSessionId);

        session.getAsyncRemote().sendText(message);
    }

    public void sendMessage(String message, String httpSessionId) {
        Session session = webSocketMap.get(httpSessionId);

        session.getAsyncRemote().sendText(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebSocketServer that = (WebSocketServer) o;
        return Objects.equals(httpSessionId, that.httpSessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpSessionId);
    }
}