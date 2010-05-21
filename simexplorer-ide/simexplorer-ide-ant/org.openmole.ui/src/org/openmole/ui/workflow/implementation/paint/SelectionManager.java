
package org.openmole.ui.workflow.implementation.paint;

import org.openmole.ui.commons.ApplicationCustomize;
import org.openmole.ui.workflow.model.IObjectViewUI;
import org.openmole.ui.workflow.model.ITaskViewUI;

/**
 *
 * @author mathieu
 */
public class SelectionManager {
    private IObjectViewUI selected = null;

    private static SelectionManager instance = null;

    public void setSelected(IObjectViewUI wi){
        if (selected != null) {
            selected.setDefaultBackgroundColor();
        }
        wi.setBackgroundColor(ApplicationCustomize.getInstance().getColor(ApplicationCustomize.TASK_SELECTION_COLOR));
        selected = wi;
    }

    public static SelectionManager getInstance(){
        if (instance == null){
            instance = new SelectionManager();
        }
        return instance;
}
}
