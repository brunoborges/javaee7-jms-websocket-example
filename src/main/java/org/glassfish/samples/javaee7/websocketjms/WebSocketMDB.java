/**
 * Copyright © 2013, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.glassfish.samples.javaee7.websocketjms;

import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * This MDB will fire CDI events with the JMS payload, classified as
 * <code>@WSJMSMessage</code>
 *
 * @author Bruno Borges <bruno.borges at oracle.com>
 */
@MessageDriven(mappedName = "jms/myQueue")
public class WebSocketMDB implements MessageListener {

    @Inject
    @WSJMSMessage
    Event<Message> jmsEvent;

    @Override
    public void onMessage(Message msg) {
        jmsEvent.fire(msg);
    }
}
