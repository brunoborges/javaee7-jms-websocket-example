/**
 * Copyright © 2013, 2013, Oracle and/or its affiliates. All rights reserved. 
 */
package org.glassfish.samples.javaee7.websocketjms;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * This is the CDI event classifier
 * 
 * @author Bruno Borges <bruno.borges at oracle.com>
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
public @interface WSJMSMessage {
}
