package org.apache.ode.bpel.rapi;

import org.apache.ode.bpel.iapi.ProcessConf;
import org.apache.ode.bpel.common.FaultException;
import org.apache.ode.bpel.extension.ExtensionBundleRuntime;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.Map;

public interface OdeRuntime {
    
    void init(ProcessConf pconf, ProcessModel pmodel);

    OdeRTInstance newInstance(Object state);

    Object getReplacementMap(QName processName);

    ProcessModel getModel();

    void clear();

    String extractProperty(Element msgData, PropertyAliasModel alias, String target) throws FaultException;

    void setExtensionRegistry(Map<String, ExtensionBundleRuntime> extensionRegistry);
}
