package org.openmole.core.execution.jsaga.session.internal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.url.URLConstants;
import org.osgi.service.url.URLStreamHandlerService;
import org.openmole.core.execution.jsaga.session.IJSagaSessionService;
import org.openmole.misc.workspace.IWorkspace;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {


    static BundleContext context;
    static String JSAGAConfigFile = "jsaga-universe.xml";
    static String JSAGATimeOutFile = "jsaga-timeout.properties";
    private static IWorkspace workspace;
    private ServiceRegistration reg;

    @Override
    public void start(BundleContext context) throws Exception {
        //System.setProperty("jsaga.universe.create.if.missing", "true");
        this.context = context;

        System.setProperty("JSAGA_VAR", getWorkspace().newTmpDir().getAbsolutePath());
        System.setProperty("saga.factory", "fr.in2p3.jsaga.impl.SagaFactoryImpl");

        org.apache.log4j.Logger.getLogger(org.glite.security.util.FileEndingIterator.class.getName()).setLevel(org.apache.log4j.Level.FATAL);
        org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.FATAL);

        java.net.URL universe = this.getClass().getClassLoader().getResource(JSAGAConfigFile);

        if (universe != null) {
            System.setProperty("jsaga.universe", universe.toString());
        } else {
            Logger.getLogger(Activator.class.getName()).log(Level.WARNING, JSAGAConfigFile + " JSAGA config file not found.");
        }


       /* BufferedReader r =  new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(JSAGAConfigFile)));
        String s;
        while((s = r.readLine()) != null) {
            System.out.println(s);
        }*/


        java.net.URL timeout = this.getClass().getClassLoader().getResource(JSAGATimeOutFile);

        if (universe != null) {
            System.setProperty("jsaga.timeout", timeout.toString());
        } else {
            Logger.getLogger(Activator.class.getName()).log(Level.WARNING, JSAGAConfigFile + " JSAGA timeout file not found.");
        }

        initializeURLProtocol(context);

        reg = context.registerService(IJSagaSessionService.class.getName(), new JSagaSessionService(), null);


    }

    @Override
    public void stop(BundleContext context) throws Exception {
        reg.unregister();
    }

    public static BundleContext getContext() {
        return context;
    }



    public static IWorkspace getWorkspace() {
        if (workspace != null) {
            return workspace;
        }

        synchronized (Activator.class) {
            if (workspace == null) {
                ServiceReference ref = getContext().getServiceReference(IWorkspace.class.getName());
                workspace = (IWorkspace) getContext().getService(ref);
            }
        }
        return workspace;
    }


    @SuppressWarnings("unchecked")
    private void initializeURLProtocol(final BundleContext context) {
        String protocol = "httpg"; //$NON-NLS-1$
        //    URLStreamHandlerService svc = new HttpgURLStreamHandlerService();
        Hashtable properties = new Hashtable();
        properties.put(URLConstants.URL_HANDLER_PROTOCOL, new String[]{
                    protocol
                });
        context.registerService(URLStreamHandlerService.class.getName(),
                new HttpgURLStreamHandlerService(),
                properties);
    }
}
