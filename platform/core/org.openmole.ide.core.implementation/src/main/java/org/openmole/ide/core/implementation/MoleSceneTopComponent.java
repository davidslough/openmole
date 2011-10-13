/*
 * Copyright (C) 2011 <mathieu.leclaire at openmole.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openmole.ide.core.implementation;

import java.awt.BorderLayout;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openmole.ide.core.implementation.palette.PaletteSupport;
import org.openmole.ide.core.implementation.control.TopComponentsManager;
import org.openmole.ide.core.implementation.action.EnableTaskDetailedViewAction;
import org.netbeans.spi.palette.PaletteController;
import org.openide.awt.ActionID;
import org.openide.util.ImageUtilities;
import org.openmole.ide.core.implementation.control.ExecutionBoard;
import org.openmole.ide.core.implementation.action.BuildExecutionAction;
import org.openmole.ide.core.implementation.action.CleanAndBuildExecutionAction;
import org.openmole.ide.core.implementation.display.Displays;
import org.openmole.ide.core.implementation.display.PropertyPanel;
import org.openmole.ide.core.model.workflow.IMoleScene;
import org.openmole.ide.misc.widget.ToolBarButton;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.openmole.ide.core.implementation//MoleScene//EN",
autostore = false)
@TopComponent.Description(preferredID = "MoleSceneTopComponent", iconBase = "img/addMole.png", persistenceType = TopComponent.PERSISTENCE_NEVER)
//iconBase="SET/PATH/TO/ICON/HERE", 
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "File", id = "org.openmole.ide.core.implementation.MoleSceneTopComponent")
//@ActionReferences({
// @ActionReference(path = "Menu/File", position = 30),
// @ActionReference(path = "Toolbars/File", position = 10),
// @ActionReference(path = "Shortcuts", name = "D-N")
//})
@TopComponent.OpenActionRegistration(displayName = "Add Mole")
public final class MoleSceneTopComponent extends CloneableTopComponent {

    private ToolBarButton buildButton;
    private ToolBarButton cleanAndBuildButton;
    private JToggleButton detailedViewButton;
    private final InstanceContent ic = new InstanceContent();
    private PaletteController palette;
    private ExecutionTopComponent etc = ((ExecutionTopComponent) WindowManager.getDefault().findTopComponent("ExecutionTopComponent"));
    private IMoleScene ms;

    public MoleSceneTopComponent(IMoleScene clonedMoleScene) {
        initialize(clonedMoleScene);
    }

    public MoleSceneTopComponent() {
        this(TopComponentsManager.buildMoleScene());
    }

    private void initialize(IMoleScene m) {
        ms = m;
        associateLookup(new AbstractLookup(ic));
        addPalette();
        initComponents();
        setToolTipText(NbBundle.getMessage(MoleSceneTopComponent.class, "HINT_MoleSceneTopComponent"));

        TopComponentsManager.registerTopComponent(this);
        detailedViewButton = new JToggleButton(new ImageIcon(ImageUtilities.loadImage("img/detailedView.png")));
        detailedViewButton.addActionListener(new EnableTaskDetailedViewAction());

        buildButton = new ToolBarButton(new ImageIcon(ImageUtilities.loadImage("img/build.png")),
                "Build the workflow",
                new BuildExecutionAction(this));
        cleanAndBuildButton = new ToolBarButton(new ImageIcon(ImageUtilities.loadImage("img/cleanAndBuild.png")),
                "Clean and build the workflow",
                new CleanAndBuildExecutionAction(this));

        toolBar.add(detailedViewButton);
        toolBar.add(new JToolBar.Separator());
        toolBar.add(buildButton.peer());
        toolBar.add(cleanAndBuildButton.peer());
        toolBar.add(ExecutionBoard.peer());
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        setDisplayName(ms.manager().name().get());
        setName(ms.manager().name().get());
        add(new JScrollPane(ms.graphScene().createView()), BorderLayout.CENTER);
        //  palette.refresh();
        etc.close();
        repaint();
        ((EntityPropertyTopComponent) WindowManager.getDefault().findTopComponent("EntityPropertyTopComponent")).open();
    }

    public IMoleScene getMoleScene() {
        return ms;
    }

    public void addPalette() {
        palette = PaletteSupport.createPalette(ms.moleSceneType());
        ic.add(palette);
    }

    public void refresh(Boolean b) {
        ic.remove(palette);
        addPalette();
        palette.refresh();
        repaint();
        buildMode(b);
    }

    public void buildMode(Boolean b) {
        buildButton.visible_$eq(b);
        cleanAndBuildButton.visible_$eq(b);
        ExecutionBoard.activate(!b);
        detailedViewButton.setEnabled(b);
        if (b) {
            etc.close();
        } else {
            etc.open();
        }
        Displays.propertyPanel().editable_$eq(b);
        repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolBar = new javax.swing.JToolBar();

        toolBar.setRollover(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(toolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addGap(264, 264, 264))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables

    public Set<TopComponent> getOpened() {
        return getRegistry().getOpened();
    }

    @Override
    public void componentActivated() {
        PaletteSupport.setCurrentMoleSceneTopComponent(this);
        refresh(ms.isBuildScene());
        Displays.propertyPanel().cleanViewport();
        TopComponentsManager.displayExecutionView(ms);
        if (!ms.isBuildScene()) {
            etc.open();
        }
        PaletteSupport.modified_$eq(false);
    }

    @Override
    public void componentOpened() {
        PaletteSupport.setCurrentMoleSceneTopComponent(this);
    }

    @Override
    public void componentClosed() {
        if (ms.isBuildScene()) {
            TopComponentsManager.removeAllExecutionTopComponent(this);
        }
        TopComponentsManager.removeTopComponent(this);
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
