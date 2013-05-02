/**
 * Copyright © 2013, 2013, Oracle and/or its affiliates. All rights reserved.
 */
package org.glassfish.samples.javaee7.websocketjms;

import java.util.logging.Level;
import javax.ejb.MessageDriven;
import javax.inject.Named;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Logger;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;

/**
 * This MDB will fire CDI events with the JMS payload, classified as
 *
 * @WSJMSMessage
 *
 * @author Bruno Borges <bruno.borges at oracle.com>
 */
@Named
@MessageDriven(mappedName = "jms/myQueue")
public class WebSocketMDB implements MessageListener {

    @Inject
    @WSJMSMessage
    Event<Message> jmsEvent;

    @Override
    public void onMessage(Message msg) {
        try {
            Logger.getLogger(WebSocketMDB.class.getName()).log(Level.INFO, "Message received [id={0}] [payload={1}]", new Object[]{msg.getJMSMessageID(), msg.getBody(String.class)});
            jmsEvent.fire(msg);
        } catch (JMSException ex) {
            Logger.getLogger(WebSocketMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
