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
package org.apache.ode.extension.e4x;

import javax.xml.namespace.QName;

import org.apache.ode.bpel.common.FaultException;
import org.apache.ode.bpel.eapi.ExtensionContext;
import org.apache.ode.bpel.eapi.ExtensionOperation;
import org.apache.ode.utils.SerializableElement;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.xml.XMLLib;
import org.mozilla.javascript.xml.XMLLib.Factory;

/**
 * Implementation of a Javascript extension assign operation.
 * 
 * @author Tammo van Lessen (University of Stuttgart)
 */
public class JSExtensionOperation implements ExtensionOperation {
	
	public void run(ExtensionContext context, SerializableElement element) throws FaultException {

		ContextFactory contextFactory = new ContextFactory() {
			//Enforce usage of plain DOM
			protected Factory getE4xImplementationFactory() {
				return XMLLib.Factory.create("org.mozilla.javascript.xmlimpl.XMLLibImpl");
			}
		};

		ContextFactory.initGlobal(contextFactory);
		Context ctx = Context.enter();
		try {
			Scriptable scope = ctx.initStandardObjects();
			ScriptableObject.defineClass(scope, ExtensionContextWrapper.class);
			Scriptable wrappedContext = ctx.newObject(scope, "ExtensionContext", new Object[] {context, ctx});
			ScriptableObject.putProperty(scope, "context", wrappedContext);
			String source = element.getElement().getTextContent();
			ctx.evaluateString(scope, source, context.getActivityName(), 1, null);
		} catch (Exception e) {
			throw new FaultException(new QName("ExtensionEvaluationFault", JSExtensionBundle.NS), e.getMessage());
		} finally {
			Context.exit();
		}
	}
}
