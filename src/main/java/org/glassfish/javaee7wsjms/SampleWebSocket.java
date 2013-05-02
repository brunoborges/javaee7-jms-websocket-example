/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.javaee7wsjms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author bruno
 */
@Named
@ServerEndpoint("/websocket")
public class SampleWebSocket implements Serializable {

    private MessagenSenderSessionBean senderBean;
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @Inject
    public SampleWebSocket(MessagenSenderSessionBean sb) {
        this.senderBean = sb;
    }

    @OnOpen
    public void onOpen(final Session session) {
        try {
            session.getBasicRemote().sendText("session opened");
            sessions.add(session);
        } catch (Exception ex) {
            Logger.getLogger(SampleWebSocket.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(final String message, final Session client) {
        try {
            client.getBasicRemote().sendText("sending message to SessionBean...");
        } catch (IOException ex) {
            Logger.getLogger(SampleWebSocket.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (senderBean != null) {
            Logger.getLogger(SampleWebSocket.class.getName()).log(Level.INFO, "senderBean is not null");
            senderBean.sendMessage(message);
        } else {
            Logger.getLogger(SampleWebSocket.class.getName()).log(Level.INFO, "senderBean is null");
        }

    }

    @OnClose
    public void onClose(final Session session) {
        try {
            session.getBasicRemote().sendText("WebSocket Session closed");
            sessions.remove(session);
        } catch (Exception ex) {
            Logger.getLogger(SampleWebSocket.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public void onJMSMessage(@Observes @WebSocketJMSMessage Message msg) {
        Logger.getLogger(SampleWebSocket.class.getName()).log(Level.INFO, "Got JMS Message at WebSocket!");
        for (Session s : sessions) {
            try {
                s.getBasicRemote().sendText("message from JMS: " + msg.getBody(String.class));
            } catch (IOException | JMSException ex) {
                Logger.getLogger(SampleWebSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
