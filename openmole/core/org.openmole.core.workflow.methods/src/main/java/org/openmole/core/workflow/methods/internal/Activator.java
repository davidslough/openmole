/*
 *  Copyright (C) 2010 reuillon
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openmole.core.workflow.methods.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
//import org.openmole.misc.backgroundexecutor.ITransferMonitor;
import org.openmole.misc.workspace.IWorkspace;

public class Activator implements BundleActivator {
	
	static BundleContext context;
	private static IWorkspace workspace;
	//private static ITransferMonitor transferMonitor;
    
	
	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		context = null;
		
	}
	
	public synchronized static IWorkspace getWorkspace() {
		if(workspace  == null) {
			ServiceReference ref = getContext().getServiceReference(IWorkspace.class.getName());
			workspace = (IWorkspace) getContext().getService(ref);
		}
		return workspace;
	}



	private static BundleContext getContext() {
		return context;
	}
}
