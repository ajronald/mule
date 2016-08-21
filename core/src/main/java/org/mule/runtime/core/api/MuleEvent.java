/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.api;

import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.core.MessageExchangePattern;
import org.mule.runtime.core.api.connector.ReplyToHandler;
import org.mule.runtime.core.api.construct.FlowConstruct;
import org.mule.runtime.core.api.context.notification.FlowCallStack;
import org.mule.runtime.core.api.context.notification.ProcessorsTrace;
import org.mule.runtime.core.api.security.SecurityContext;
import org.mule.runtime.core.api.source.MessageSource;
import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.config.DefaultMuleConfiguration;
import org.mule.runtime.core.message.Correlation;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * Legacy implementation of {@link org.mule.runtime.api.message.MuleEvent}
 * <p/>
 * Holds a MuleMessage payload and provides helper methods for obtaining the data in a format that the receiving Mule component
 * understands. The event can also maintain any number of properties that can be set and retrieved by Mule components.
 *
 * @see org.mule.runtime.api.message.MuleEvent
 * @see MuleMessage
 * @deprecated Use {@link org.mule.runtime.api.message.MuleEvent} instead
 */
@Deprecated
public interface MuleEvent extends org.mule.runtime.api.message.MuleEvent {

  /**
   * @return the context applicable to all events created from the same root {@link MuleEvent} from a {@link MessageSource}.
   */
  MessageExecutionContext getExecutionContext();

  /**
   * Returns the correlation metadata of this message. See {@link Correlation}.
   * 
   * @return the correlation metadata of this message.
   */
  Correlation getCorrelation();

  /**
   * The returned value will depend on the {@link MessageSource} that created this event, and the flow that is executing the
   * event.
   * 
   * @return the correlation id to use for this event.
   */
  String getCorrelationId();

  /**
   * @return the {@link MuleEvent}
   */
  MuleEvent getParent();

  /**
   * Returns the message payload for this event
   * 
   * @return the message payload for this event
   */
  @Override
  MuleMessage getMessage();

  /**
   * Returns the contents of the message as a byte array.
   * 
   * @param muleContext the Mule node.
   * @return the contents of the message as a byte array
   * @throws MuleException if the message cannot be converted into an array of bytes
   */
  byte[] getMessageAsBytes(MuleContext muleContext) throws MuleException;

  /**
   * Transforms the message into the requested format. The transformer used is the one configured on the endpoint through which
   * this event was received.
   * 
   * @param outputType The requested output type.
   * @param muleContext the Mule node.
   * @return the message transformed into it's recognised or expected format.
   * @throws TransformerException if a failure occurs in the transformer
   * @see org.mule.runtime.core.api.transformer.Transformer if the transform fails or the outputtype is null
   */
  <T> T transformMessage(Class<T> outputType, MuleContext muleContext) throws TransformerException;

  /**
   * Transforms the message into the requested format. The transformer used is the one configured on the endpoint through which
   * this event was received.
   * 
   * @param outputType The requested output type.
   * @param muleContext the Mule node.
   * @return the message transformed into it's recognised or expected format.
   * @throws TransformerException if a failure occurs in the transformer
   * @see org.mule.runtime.core.api.transformer.Transformer if the transform fails or the outputtype is null
   */
  Object transformMessage(DataType outputType, MuleContext muleContext) throws TransformerException;

  /**
   * Returns the message transformed into it's recognised or expected format and then into a String. The transformer used is the
   * one configured on the endpoint through which this event was received. If necessary this will use the encoding set on the
   * event.
   * 
   * @param muleContext the Mule node.
   * @return the message transformed into it's recognised or expected format as a Strings.
   * @throws TransformerException if a failure occurs in the transformer
   * @see org.mule.runtime.core.api.transformer.Transformer
   */
  String transformMessageToString(MuleContext muleContext) throws TransformerException;

  /**
   * Returns the message contents as a string If necessary this will use the encoding set on the event
   * 
   * @param muleContext the Mule node.
   * @return the message contents as a string
   * @throws MuleException if the message cannot be converted into a string
   */
  String getMessageAsString(MuleContext muleContext) throws MuleException;

  /**
   * Returns the message contents as a string
   * 
   * @param encoding the encoding to use when converting the message to string
   * @param muleContext the Mule node.
   * @return the message contents as a string
   * @throws MuleException if the message cannot be converted into a string
   */
  String getMessageAsString(Charset encoding, MuleContext muleContext) throws MuleException;

  /**
   * Retrieves the service session for the current event
   * 
   * @return the service session for the event
   */
  MuleSession getSession();

  /**
   * Retrieves the service for the current event
   * 
   * @return the service for the event
   */
  FlowConstruct getFlowConstruct();

  String getFlowName();

  /**
   * Returns the muleContext for the Mule node that this event was received in
   * 
   * @return the muleContext for the Mule node that this event was received in
   */
  MuleContext getMuleContext();

  /**
   * Returns the message exchange pattern for this event
   */
  MessageExchangePattern getExchangePattern();

  /**
   * Returns true is this event is being processed in a transaction
   */
  boolean isTransacted();

  /**
   * Returns the {@link URI} of the MessageSource that recieved or generated the message being processed.
   */
  URI getMessageSourceURI();

  /**
   * Returns the message source name if it has one, otherwise returns toString() of the URI returned be getMessageSourceURI()
   */
  String getMessageSourceName();

  /**
   * Return the replyToHandler (if any) that will be used to perform async reply
   */
  ReplyToHandler getReplyToHandler();

  /**
   * Return the destination (if any) that will be passed to the reply-to handler.
   */
  Object getReplyToDestination();

  boolean isSynchronous();

  void setMessage(MuleMessage message);

  /**
   * Gets the data type for a given flow variable
   *
   * @param key the name or key of the variable. This must be non-null.
   * @return the property data type or null if the flow variable does not exist
   */
  DataType getFlowVariableDataType(String key);

  Set<String> getFlowVariableNames();

  void clearFlowVariables();

  /**
   * Indicates if notifications should be fired when processing this message.
   *
   * @return true if notifications are enabled, false otherwise
   */
  boolean isNotificationsEnabled();

  /**
   * Enables the firing of notifications when processing the message.
   *
   * @param enabled
   */
  void setEnableNotifications(boolean enabled);

  /**
   * Indicates if the current event allows non-blocking execution and IO.
   *
   * @return true if non-blocking execution and IO is allowed. False otherwise.
   */
  boolean isAllowNonBlocking();

  /**
   * Events have a stack of executed flows (same as a call stack), so that at any given instant an application developer can
   * determine where this event came from.
   * <p/>
   * This will only be enabled if {@link DefaultMuleConfiguration#isFlowTrace()} is {@code true}. If {@code false}, the stack will
   * always be empty.
   * 
   * @return the flow stack associated to this event.
   * 
   * @since 3.8.0
   */
  FlowCallStack getFlowCallStack();

  /**
   * Events have a list of message processor paths it went trough so that the execution path of an event can be reconstructed
   * after it has executed.
   * <p/>
   * This will only be enabled if {@link DefaultMuleConfiguration#isFlowTrace()} is {@code true}. If {@code false}, the list will
   * always be empty.
   * 
   * @return the message processors trace associated to this event.
   * 
   * @since 3.8.0
   */
  ProcessorsTrace getProcessorsTrace();

  /**
   * The security context for this session. If not null outbound, inbound and/or method invocations will be authenticated using
   * this context
   *
   * @return the context for this session or null if the request is not secure.
   */
  SecurityContext getSecurityContext();

  /**
   * The security context for this session. If not null outbound, inbound and/or method invocations will be authenticated using
   * this context
   *
   * @param context the context for this session or null if the request is not secure.
   */
  void setSecurityContext(SecurityContext context);
}
