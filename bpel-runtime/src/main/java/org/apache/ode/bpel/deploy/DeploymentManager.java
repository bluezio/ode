package org.apache.ode.bpel.deploy;

import java.io.File;
import java.util.Collection;

import org.apache.ode.bpel.iapi.DeploymentUnit;

/**
 * Interface to the deployment manager. This is a mechanism for keeping track (persistently) of the deployment 
 * units. 

 * @author Maciej Szefler - m s z e f l e r @ g m a i l . c o m
 *
 */
public interface DeploymentManager {
    
    /**
     * Create a deployment unit based on a given location (and remember it).  
     * @param location location of the deployment unit
     * @return instance of the {@link DeploymentUnit} interface representing the newly created deployment unit
     */
    DeploymentUnitImpl createDeploymentUnit(String location);
    
    DeploymentUnitImpl createDeploymentUnit(File deploymentUnitDirectory);

    /**
     * Remove a deployment unit previously created using {@link #createDeploymentUnit(String)} method. The removal
     * is permanent--that is the persistent representation of the deployment unit can be removed.
     * @param du
     */
    void remove(DeploymentUnitImpl du);
    
    /**
     * Get the collection of deployment units created with the {@link #createDeploymentUnit(String)} method
     * that have not been {@link #remove(DeploymentUnit)}ed. 
     * @return
     */
    Collection<DeploymentUnitImpl> getDeploymentUnits();

    
}
