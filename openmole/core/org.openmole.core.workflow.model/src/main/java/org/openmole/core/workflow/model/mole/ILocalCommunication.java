/*
 *  Copyright (C) 2010 Romain Reuillon
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

package org.openmole.core.workflow.model.mole;

import java.util.Collection;
import org.openmole.core.workflow.model.data.IDataChannel;
import org.openmole.core.workflow.model.job.IContext;
import org.openmole.core.workflow.model.tools.IRegistryWithTicket;

import org.openmole.core.workflow.model.transition.IAggregationTransition;
import org.openmole.core.workflow.model.transition.ITransition;

public interface ILocalCommunication {
    IRegistryWithTicket<ITransition, IContext> getTransitionRegistry();
    IRegistryWithTicket<IAggregationTransition, Collection<IContext>> getAggregationTransitionRegistry();
    IRegistryWithTicket<IDataChannel, IContext> getDataChannelRegistry();
}
