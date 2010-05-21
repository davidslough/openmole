/*
 *  Copyright (C) 2010 Mathieu Leclaire <mathieu.leclaire@openmole.fr>
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
package org.openmole.ui.workflow.provider;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JMenuItem;
import org.openmole.core.workflow.methods.task.JavaTask;
import org.openmole.core.workflow.model.task.IGenericTask;
import org.openmole.plugin.task.groovytask.GroovyTask;
import org.openmole.ui.workflow.action.AddTaskAction;
import org.openmole.ui.workflow.action.AddTaskCapsuleAction;
import org.openmole.ui.workflow.implementation.Preferences;
import org.openmole.ui.workflow.model.IMoleScene;

/**
 *
 * @author Mathieu Leclaire <mathieu.leclaire@openmole.fr>
 */
public class MoleSceneMenuProvider extends GenericMenuProvider {

    Collection<Class<? extends IGenericTask>> tasks = new ArrayList<Class<? extends IGenericTask>>();

    public MoleSceneMenuProvider(IMoleScene moleScene) {
        super();

        JMenuItem itemTCapsule = new JMenuItem("a Task Capsule");
        itemTCapsule.addActionListener(new AddTaskCapsuleAction(moleScene));

        Collection<JMenuItem> colTask = new ArrayList<JMenuItem>();
        for (Class c :Preferences.getInstance().getBusinessClasses()){
            JMenuItem it = new JMenuItem(c.toString());
            it.addActionListener(new AddTaskAction(moleScene,c));
            colTask.add(it);
        }

        Collection<JMenuItem> colI = new ArrayList<JMenuItem>();
        colI.add(itemTCapsule);
        colI.add(PopupMenuProviderFactory.addSubMenu("a Task ",colTask));

        menus.add(PopupMenuProviderFactory.addSubMenu("Add ",colI));
    }
}
