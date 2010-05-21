package org.openmole.core.execution.environmentprovider.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.openmole.core.execution.environmentprovider.IEnvironmentProvider;

public class Activator implements BundleActivator {

    private static BundleContext context;
  
    private ServiceRegistration reg;

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        Activator.context = context;

        IEnvironmentProvider provider = new EnvironmentProvider();
        reg = context.registerService(IEnvironmentProvider.class.getName(), provider, null);
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        Activator.context = null;
        reg.unregister();
    }
    
    private static BundleContext getContext() {
        return context;
    }
}
