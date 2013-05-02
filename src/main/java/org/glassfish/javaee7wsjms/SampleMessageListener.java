/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.javaee7wsjms;

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
 *
 * @author bruno
 */
@Named
@MessageDriven(mappedName = "jms/myQueue")
public class SampleMessageListener implements MessageListener {

    @Inject
    @WebSocketJMSMessage
    Event<Message> jmsEvent;

    @Override
    public void onMessage(Message msg) {
        try {
            Logger.getLogger(SampleMessageListener.class.getName()).log(Level.INFO, "Message received [id={0}] [payload={1}]", new Object[]{msg.getJMSMessageID(), msg.getBody(String.class)});
            jmsEvent.fire(msg);
        } catch (JMSException ex) {
            Logger.getLogger(SampleMessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
