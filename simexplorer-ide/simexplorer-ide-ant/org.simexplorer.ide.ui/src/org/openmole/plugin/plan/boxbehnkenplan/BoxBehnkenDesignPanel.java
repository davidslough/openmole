/*
 *  Copyright (c) 2008, Cemagref
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation; either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License along with this program; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston,
 *  MA  02110-1301  USA
 */
package org.openmole.plugin.plan.boxbehnkenplan;

import org.openide.util.lookup.ServiceProvider;
import org.simexplorer.core.workflow.methods.EditorPanel;

@ServiceProvider(service=EditorPanel.class)
public class BoxBehnkenDesignPanel extends EditorPanel<BoxBehnkenPlan> {

    /** Creates new form LHSPlanPanel */
    public BoxBehnkenDesignPanel() {
        super(BoxBehnkenPlan.class);
        initComponents();
    }

    @Override
    public void applyChanges() {
        super.applyChanges();
        getObjectEdited().setNbCenterPoint((Integer)nbCenterPointsjSpinner.getValue());
    }

    @Override
    public void setObjectEdited(BoxBehnkenPlan method) {
        super.setObjectEdited(method);
        nbCenterPointsjSpinner.setValue(getObjectEdited().getNbCenterPoint());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nbCenterPointsjSpinner = new javax.swing.JSpinner();

        setBorder(javax.swing.BorderFactory.createTitledBorder("BoxBehnken Design options"));
        setName("Central Composite design (CCD) options"); // NOI18N

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12));
        jLabel2.setText("Replication");

        jLabel5.setText("number of center points");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(141, 141, 141))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                .addComponent(nbCenterPointsjSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(nbCenterPointsjSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSpinner nbCenterPointsjSpinner;
    // End of variables declaration//GEN-END:variables

}
