/*
 *  Copyright (C) 2010 mathieu
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
package org.openmole.ui;

import java.awt.BorderLayout;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.lookup.Lookups;
import org.openmole.ui.control.ControlPanel;
import org.openmole.ui.workflow.implementation.MoleScene;
import org.openmole.ui.palette.PaletteSupport;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.openmole.ui//MoleSceneTopComponent//EN",
autostore = false)
public final class MoleSceneTopComponentTopComponent extends TopComponent {

    private static MoleSceneTopComponentTopComponent instance;
    private JComponent myView;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "MoleSceneTopComponentTopComponent";

    public MoleSceneTopComponentTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(MoleSceneTopComponentTopComponent.class, "CTL_MoleSceneTopComponentTopComponent"));
        setToolTipText(NbBundle.getMessage(MoleSceneTopComponentTopComponent.class, "HINT_MoleSceneTopComponentTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        //FIXME un meilleur endroit pour les inits??
       // TableMapping.getInstance().initialize();
       // Preferences.getInstance().initialize();

        MoleScene scene = new MoleScene();
        myView = scene.createView();

        moleSceneScrollPane.setViewportView(myView);
        try {
            // add(scene.createSatelliteView(), BorderLayout.WEST);
            add(new ControlPanel(), BorderLayout.WEST);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
        associateLookup(Lookups.fixed(new Object[]{PaletteSupport.createPalette()}));
     //   associateLookup(Lookups.fixed(new Object[]{new PropertySupport()}));

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        moleSceneScrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(moleSceneScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane moleSceneScrollPane;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized MoleSceneTopComponentTopComponent getDefault() {
        if (instance == null) {
            instance = new MoleSceneTopComponentTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the MoleSceneTopComponentTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized MoleSceneTopComponentTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(MoleSceneTopComponentTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof MoleSceneTopComponentTopComponent) {
            return (MoleSceneTopComponentTopComponent) win;
        }
        Logger.getLogger(MoleSceneTopComponentTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
}
