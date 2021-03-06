package com.test.websocketDemo.service;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author create by John on 2019/2/20
 * @version 1.0.0
 * @description
 */
@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {

  static Logger log = LoggerFactory.getLogger(WebSocketServer.class);

  private static int onlineCount = 0;

  private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

  private Session session;

  private String sid="";

  @OnOpen
  public void onOpen(Session session,@PathParam("sid") String sid) {
    this.session = session;
    this.sid=sid;
    webSocketSet.add(this); //加入set中
    addOnlineCount(); //在线数加1
    log.info("有新窗口开始监听:"+sid+",当前在线人数为" + getOnlineCount());
    try {
      sendMessage("连接成功");
    } catch (IOException e) {
      log.error("websocket IO异常");
    }
  }

  @OnClose
  public void onClose() {
    webSocketSet.remove(this); //从set中删除
    subOnlineCount(); //在线数减1
    log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    log.info("收到来自窗口" + sid + "的信息:" + message);
//群发消息
    for (WebSocketServer item : webSocketSet) {
      try {
        item.sendMessage(message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @OnError
  public void onError(Session session, Throwable error) {
    log.error("发生错误");
    error.printStackTrace();
  }


  /**
   * 群发自定义消息
   * */
  public static void sendInfo(@PathVariable("sid") String sid,String message) throws IOException {
    log.info("推送消息到窗口"+sid+"，推送内容:"+message);
    for (WebSocketServer item : webSocketSet) {
      try {
//这里可以设定只推送给这个sid的，为null则全部推送
        if(sid==null) {
          item.sendMessage(message);
        }else if(item.sid.equals(sid)){
          item.sendMessage(message);
        }
      } catch (IOException e) {
        continue;
      }
    }
  }


  public static synchronized int getOnlineCount() {
    return onlineCount;
  }

  public static synchronized void addOnlineCount() {
    WebSocketServer.onlineCount++;
  }

  public static synchronized void subOnlineCount() {
    WebSocketServer.onlineCount--;
  }
  public void sendMessage(String message) throws IOException {
    this.session.getBasicRemote().sendText(message);
  }

}
