/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ode.daohib.bpel.hobj;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Hibernate-managed table for keeping track of message exchanges.
 * 
 * @hibernate.class table="BPEL_MESSAGE_EXCHANGE" dynamic-update="true"
 */
public class HMessageExchange extends HObject {

    private String _channelName;

    private String _operationName;

    private String _state;

    private Date _insertTime;

    private String _portType;

    private HLargeData _endpoint;

    private HLargeData _callbackEndpoint;

    private HMessage _request;

    private HMessage _response;

    private HPartnerLink _partnerLink;

    private String _clientKey;

    private HProcessInstance _instance;

    private HProcess _process;

    private char _dir;

    private int _plinkModelId;

    private String _pattern;

    private String _corrstatus;

    private String _faultType;

    private String _faultExplanation;

    private String _callee;

    private HMessageExchange _p2pPeer;

    private Map<String, String> _properties = new HashMap<String, String>();

    private long _timeout;

    private String _istyle;

    private String _failureType;

    private String _mexId;

    /**
     * 
     */
    public HMessageExchange() {
        super();
    }

    /**
     * 
     * @hibernate.property
     * @hibernate.column name="MEXID" not-null="true" unique="true"
     */

    public String getMexId() {
        return _mexId;
    }

    public void setMexId(String mexId) {
        _mexId = mexId;
    }

    /**
     * @hibernate.property column="PORT_TYPE"
     */
    public String getPortType() {
        return _portType;
    }

    public void setPortType(String portType) {
        _portType = portType;
    }

    /**
     * @hibernate.property column="CHANNEL_NAME"
     */
    public String getChannelName() {
        return _channelName;
    }

    public void setChannelName(String channelName) {
        _channelName = channelName;
    }

    /**
     * @hibernate.property column="CLIENTKEY"
     */
    public String getClientKey() {
        return _clientKey;
    }

    public void setClientKey(String clientKey) {
        _clientKey = clientKey;
    }

    /**
     * @hibernate.many-to-one column="LDATA_EPR_ID" cascade="delete"
     */
    public HLargeData getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(HLargeData endpoint) {
        _endpoint = endpoint;
    }

    /**
     * @hibernate.many-to-one column="LDATA_CEPR_ID" cascade="delete"
     */
    public HLargeData getCallbackEndpoint() {
        return _callbackEndpoint;
    }

    public void setCallbackEndpoint(HLargeData endpoint) {
        _callbackEndpoint = endpoint;
    }

    /**
     * @hibernate.many-to-one column="REQUEST" cascade="delete"
     */
    public HMessage getRequest() {
        return _request;
    }

    public void setRequest(HMessage request) {
        _request = request;
    }

    /**
     * @hibernate.many-to-one column="RESPONSE" cascade="delete"
     */
    public HMessage getResponse() {
        return _response;
    }

    public void setResponse(HMessage response) {
        _response = response;
    }

    /**
     * @hibernate.property column="INSERT_DT"
     */
    public Date getInsertTime() {
        return _insertTime;
    }

    public void setInsertTime(Date insertTime) {
        _insertTime = insertTime;
    }

    /**
     * @hibernate.property column="OPERATION"
     */
    public String getOperationName() {
        return _operationName;
    }

    public void setOperationName(String operationName) {
        _operationName = operationName;
    }

    /**
     * @hibernate.property column="STATE"
     */
    public String getState() {
        return _state;
    }

    public void setState(String state) {
        _state = state;
    }

    /**
     * @hibernate.many-to-one column="PROCESS"
     */
    public HProcess getProcess() {
        return _process;
    }

    public void setProcess(HProcess process) {
        _process = process;
    }

    /**
     * @hibernate.many-to-one column="PIID"
     */
    public HProcessInstance getInstance() {
        return _instance;
    }

    public void setInstance(HProcessInstance instance) {
        _instance = instance;
    }

    public void setDirection(char dir) {
        _dir = dir;
    }

    /**
     * @hibernate.property column="DIR"
     */
    public char getDirection() {
        return _dir;
    }

    /**
     * @hibernate.property column="PLINK_MODELID"
     */
    public int getPartnerLinkModelId() {
        return _plinkModelId;
    }

    public void setPartnerLinkModelId(int id) {
        _plinkModelId = id;
    }

    /**
     * @hibernate.property column="PATTERN"
     */
    public String getPattern() {
        return _pattern;
    }

    public void setPattern(String pattern) {
        _pattern = pattern;

    }

    /**
     * @hibernate.property column="CORR_STATUS"
     * @return
     */
    public String getCorrelationStatus() {
        return _corrstatus;
    }

    public void setCorrelationStatus(String cstatus) {
        _corrstatus = cstatus;

    }

    /**
     * @hibernate.property column="FAULT_TYPE"
     * @return
     */
    public String getFault() {
        return _faultType;
    }

    public void setFault(String faultType) {
        _faultType = faultType;

    }

    /**
     * @hibernate.property column="FAULT_EXPL"
     * @return
     */
    public String getFaultExplanation() {
        return _faultExplanation;
    }

    public void setFaultExplanation(String faultExplanation) {
        if (faultExplanation != null && faultExplanation.length() > 255)
            faultExplanation = faultExplanation.substring(0, 254);
        _faultExplanation = faultExplanation;
    }

    /**
     * @hibernate.property column="CALLEE"
     */
    public String getCallee() {
        return _callee;
    }

    public void setCallee(String callee) {
        _callee = callee;
    }

    /**
     * @hibernate.map name="properties" table="BPEL_MEX_PROPS" lazy="false" cascade="delete"
     * @hibernate.collection-key column="MEX"
     * @hibernate.collection-index column="NAME" type="string"
     * @hibernate.collection-element column="VALUE" type="string" length="8000"
     */
    public Map<String, String> getProperties() {
        return _properties;
    }

    public void setProperties(Map<String, String> props) {
        _properties = props;
    }

    public void setPartnerLink(HPartnerLink link) {
        _partnerLink = link;
    }

    /**
     * @hibernate.many-to-one column="PARTNERLINK"
     */
    public HPartnerLink getPartnerLink() {
        return _partnerLink;
    }

    /**
     * @hibernate.property column="TIMEOUT"
     * 
     */
    public long getTimeout() {
        return _timeout;
    }

    public void setTimeout(long timeout) {
        _timeout = timeout;
    }

    /**
     * @hibernate.property column="ISTYLE"
     */
    public String getInvocationStyle() {
        return _istyle;
    }

    /**
     * @hibernate.many-to-one column="P2P_PEER"
     * @return
     */
    public HMessageExchange getPipedMessageExchange() {
        return _p2pPeer;
    }

    public void setPipedMesageExchange(HMessageExchange p2ppeer) {
        _p2pPeer = p2ppeer;
    }

    public void setFailureType(String failureType) {
        _failureType = failureType;
    }

    /**
     * @hibernate.property column="FAILURE_TYPE"
     * @return
     */
    public String getFailureType() {
        return _failureType;
    }

    public void setInvocationStyle(String invocationStyle) {
        _istyle = invocationStyle;
    }

}
