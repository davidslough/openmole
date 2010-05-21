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

package org.openmole.core.workflow.implementation.transition;

import java.util.HashSet;
import java.util.Set;
import org.openmole.core.workflow.model.transition.ITransition;
import org.openmole.core.workflow.model.transition.ITransitionSlot;

public class TransitionSlotSet {

    Set<ITransitionSlot> slots = new HashSet<ITransitionSlot>();

    public void addInputTransitionGroup(ITransitionSlot group) {
        slots.add(group);
    }

    public int nbGroups() {
        return slots.size();
    }

    public Set<ITransitionSlot> getGroups() {
        return slots;
    }
}
