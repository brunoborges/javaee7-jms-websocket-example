/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author bruno
 */
@Named
@LocalBean
@Stateless
public class MessagenSenderSessionBean {

    @Resource(mappedName = "jms/myQueue")
    private Queue myQueue;
    @Inject
    private JMSContext jmsContext;

    public void sendMessage(String message) {
        jmsContext.createProducer().send(myQueue, message);
    }
}
