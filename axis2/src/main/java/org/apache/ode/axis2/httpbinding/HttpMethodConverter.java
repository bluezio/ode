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

package org.apache.ode.axis2.httpbinding;

import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.ode.axis2.Properties;
import org.apache.ode.axis2.util.URLEncodedTransformer;
import org.apache.ode.axis2.util.UrlReplacementTransformer;
import org.apache.ode.bpel.iapi.PartnerRoleMessageExchange;
import org.apache.ode.utils.DOMUtils;
import org.apache.ode.utils.wsdl.Messages;
import org.apache.ode.utils.wsdl.WsdlUtils;
import org.apache.ode.il.epr.MutableEndpoint;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFault;
import org.w3c.dom.Element;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.Fault;
import javax.wsdl.extensions.http.HTTPOperation;
import javax.wsdl.extensions.mime.MIMEContent;
import javax.xml.namespace.QName;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Collection;

public class HttpMethodConverter {

    private static final String CONTENT_TYPE_TEXT_XML = "text/xml";
    private static final Log log = LogFactory.getLog(HttpClientHelper.class);

    protected static final Messages msgs = Messages.getMessages(Messages.class);
    protected Binding binding;

    public HttpMethodConverter(Binding binding) {
        this.binding = binding;
    }

    public HttpMethod createHttpRequest(PartnerRoleMessageExchange odeMex, HttpParams params) throws UnsupportedEncodingException {
        Operation operation = odeMex.getOperation();
        BindingOperation bindingOperation = binding.getBindingOperation(operation.getName(), operation.getInput().getName(), operation.getOutput().getName());

        // message to be sent
        Element message = odeMex.getRequest().getMessage();
        Message msgDef = operation.getInput().getMessage();

        // base url
        String url = ((MutableEndpoint) odeMex.getEndpointReference()).getUrl();

        // extract part values into a map and check that all parts are assigned a value
        Map<String, Element> partElements = extractPartElements(msgDef, message);

        // http method type
        // the operation may override the verb, this is an extension for RESTful BPEL
        String verb = WsdlUtils.resolveVerb(binding, bindingOperation);

        // build the http method itself
        HttpMethod method = prepareHttpMethod(bindingOperation, verb, partElements, url, params);
        return method;
    }

    public void parseHttpResponse(org.apache.ode.bpel.iapi.Message odeMessage, HttpMethod response, Operation op) {

    }


    /**
     * create and initialize the http method.
     * Http Headers that may been passed in the params are not set in this method.
     * Headers will be automatically set by HttpClient.
     * See usages of HostParams.DEFAULT_HEADERS
     * See org.apache.commons.httpclient.HttpMethodDirector#executeMethod(org.apache.commons.httpclient.HttpMethod)
     */
    protected HttpMethod prepareHttpMethod(BindingOperation bindingOperation, String verb, Map<String, Element> partValues,
                                           final String rootUri, HttpParams params) throws UnsupportedEncodingException {
        if (log.isDebugEnabled()) log.debug("Preparing http request...");
        // convenience variables...
        BindingInput bindingInput = bindingOperation.getBindingInput();
        HTTPOperation httpOperation = (HTTPOperation) WsdlUtils.getOperationExtension(bindingOperation);
        MIMEContent content = WsdlUtils.getMimeContent(bindingInput.getExtensibilityElements());
        String contentType = content == null ? "" : content.getType();
        boolean useUrlEncoded = WsdlUtils.useUrlEncoded(bindingInput) || PostMethod.FORM_URL_ENCODED_CONTENT_TYPE.equalsIgnoreCase(contentType);
        boolean useUrlReplacement = WsdlUtils.useUrlReplacement(bindingInput);

        // the http method to be built and returned
        HttpMethod method = null;

        // the 4 elements the http method may be made of
        String relativeUri = httpOperation.getLocationURI();
        String queryPath = null;
        RequestEntity requestEntity;
        String encodedParams = null;

        // ODE supports uri template in both port and operation location.
        // so assemble the final url *before* replacement
        String completeUri = rootUri;
        if (StringUtils.isNotEmpty(relativeUri)) {
            completeUri = completeUri + (completeUri.endsWith("/") || relativeUri.startsWith("/") ? "" : "/") + relativeUri;
        }

        if (useUrlReplacement) {
            // insert part values in the url
            completeUri = new UrlReplacementTransformer().transform(completeUri, partValues);
        } else if (useUrlEncoded) {
            // encode part values
            encodedParams = new URLEncodedTransformer().transform(partValues);
        }

        // http-client api is not really neat
        // something similar to the following would save some if/else manipulations.
        // But we have to deal with it as-is.
        //
        //  method = new Method(verb);
        //  method.setRequestEnity(..)
        //  etc...
        if ("GET".equalsIgnoreCase(verb) || "DELETE".equalsIgnoreCase(verb)) {
            if ("GET".equalsIgnoreCase(verb)) {
                method = new GetMethod();
            } else if ("DELETE".equalsIgnoreCase(verb)) {
                method = new DeleteMethod();
            }
            if (useUrlEncoded) {
                queryPath = encodedParams;
            }

            // Let http-client manage the redirection
            // see org.apache.commons.httpclient.params.HttpClientParams.MAX_REDIRECTS
            // default is 100
            method.setFollowRedirects(true);
        } else if ("POST".equalsIgnoreCase(verb) || "PUT".equalsIgnoreCase(verb)) {

            if ("POST".equalsIgnoreCase(verb)) {
                method = new PostMethod();
            } else if ("PUT".equalsIgnoreCase(verb)) {
                method = new PutMethod();
            }

            // some body-building...
            if (useUrlEncoded) {
                requestEntity = new StringRequestEntity(encodedParams, PostMethod.FORM_URL_ENCODED_CONTENT_TYPE, method.getParams().getContentCharset());
            } else if (contentType.endsWith(CONTENT_TYPE_TEXT_XML)) {
                // get the part to be put in the body
                Part part = bindingOperation.getOperation().getInput().getMessage().getPart(content.getPart());
                Element partValue = partValues.get(part.getName());
                // if the part has an element name, we must take the first element
                if (part.getElementName() != null) {
                    partValue = DOMUtils.getFirstChildElement(partValue);
                }
                String xmlString = DOMUtils.domToString(partValue);
                requestEntity = new ByteArrayRequestEntity(xmlString.getBytes(), contentType);
            } else {
                // should not happen because of HttpBindingValidator, but never say never
                throw new IllegalArgumentException("Unsupported content-type!");
            }

            // cast safely, PUT and POST are subclasses of EntityEnclosingMethod
            final EntityEnclosingMethod enclosingMethod = (EntityEnclosingMethod) method;
            enclosingMethod.setRequestEntity(requestEntity);
            enclosingMethod.setContentChunked(params.getBooleanParameter(Properties.PROP_HTTP_REQUEST_CHUNK, false));

        } else {
            // should not happen because of HttpBindingValidator, but never say never
            throw new IllegalArgumentException("Unsupported HTTP method: " + verb);
        }

        // link params together
        method.getParams().setDefaults(params);
        method.setPath(completeUri); // assumes that the path is properly encoded (URL safe).
        method.setQueryString(queryPath);
        return method;
    }


    protected Map<String, Element> extractPartElements(Message msgDef, Element message) {
        Map<String, Element> partValues = new HashMap<String, Element>();
        for (Iterator iterator = msgDef.getParts().values().iterator(); iterator.hasNext();) {
            Part part = (Part) iterator.next();
            Element partEl = DOMUtils.findChildByName(message, new QName(null, part.getName()));
            if (partEl == null)
                throw new IllegalArgumentException(msgs.msgOdeMessageMissingRequiredPart(part.getName()));
            partValues.put(part.getName(), partEl);
        }
        return partValues;
    }


}