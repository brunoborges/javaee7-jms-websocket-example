/**
 * Copyright © 2013, 2013, Oracle and/or its affiliates. All rights reserved. 
 */
package org.glassfish.javaee7wsjms;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSContext;
import javax.jms.Queue;

/**
 * This is the SessionBean used by the WebSocket server endpoint to dispatch 
 * incoming messages to the JMS Queue
 * 
 * @author Bruno Borges <bruno.borges at oracle.com>
 */
@Named
@LocalBean
@Stateless
public class QueueSenderSessionBean {

    @Resource(mappedName = "jms/myQueue")
    private Queue myQueue;
    @Inject
    private JMSContext jmsContext;

    public void sendMessage(String message) {
        jmsContext.createProducer().send(myQueue, message);
    }
}
