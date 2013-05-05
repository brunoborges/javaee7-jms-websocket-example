/**
 * Copyright © 2013, 2013, Oracle and/or its affiliates. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glassfish.samples.javaee7.websocketjms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * This is the WebSocket server endpoint. It listens to CDI events classified
 * with
 * <code>@WSJMSMessage</code> at <code>onJMSMessage</code> and sends the payload
 * to all client Sessions
 * @author Bruno Borges <bruno.borges at oracle.com>
 */
@ServerEndpoint("/websocket")
public class WebSocketEndpoint implements Serializable {

    @Inject
    private QueueSenderSessionBean senderBean;
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(final Session session) {
        try {
            session.getBasicRemote().sendText("session opened");
            sessions.add(session);

            if (senderBean == null) {
                Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.INFO, "senderBean is null");
            }
        } catch (Exception ex) {
            Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public void onMessage(final String message, final Session client) {
        try {
            client.getBasicRemote().sendText("sending message to SessionBean...");
        } catch (IOException ex) {
            Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (senderBean != null) {
            senderBean.sendMessage(message);
        }
    }

    @OnClose
    public void onClose(final Session session) {
        try {
            session.getBasicRemote().sendText("WebSocket Session closed");
            sessions.remove(session);
        } catch (Exception ex) {
            Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onJMSMessage(@Observes @WSJMSMessage Message msg) {
        Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.INFO, "Got JMS Message at WebSocket!");
        try {
            for (Session s : sessions) {
                s.getBasicRemote().sendText("message from JMS: " + msg.getBody(String.class));
            }
        } catch (IOException | JMSException ex) {
            Logger.getLogger(WebSocketEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
