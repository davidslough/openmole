/*
 *  Copyright (C) 2010 Cemagref
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
package org.simexplorer.openmole.plugin.task.sensitivitytask;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nuiton.j2r.RException;
import org.openmole.core.workflow.implementation.data.Prototype;
import org.openmole.core.workflow.implementation.task.Task;
import org.openmole.core.workflow.model.execution.IProgress;
import org.openmole.core.workflow.model.job.IContext;
import org.openmole.core.workflow.model.mole.IExecutionContext;
import org.openmole.misc.exception.InternalProcessingError;
import org.openmole.misc.exception.UserBadDataError;

/**
 *
 * @author Nicolas Dumoulin <nicolas.dumoulin@cemagref.fr>
 */
public class TellTask extends Task {

    transient final static String RVariableName = "sa";
    private transient final static String COMMAND_FOR_V = new StringBuilder().append(RVariableName).append("[['V']]").toString();
    private transient final static String COMMAND_FOR_D1 = new StringBuilder().append(RVariableName).append("[['D1']]").toString();
    private transient final static String COMMAND_FOR_I1 = new StringBuilder().append(RVariableName).append("[['D1']] / ").append(RVariableName).append("[['V']] ").toString();
    private transient final static String COMMAND_FOR_IT = new StringBuilder().append("1 - ").append(RVariableName).append("[['Dt']] / ").append(RVariableName).append("[['V']] ").toString();
    private transient final static String COMMAND_FOR_DT = new StringBuilder().append(RVariableName).append("[['Dt']]").toString();
    private transient final static String ModelOutputVariableName = "y";
    private Prototype<Double> modelOutputPrototype = new Prototype("y", Double.class);
    private Prototype analysisVPrototype = new Prototype("V", Object.class);
    private Prototype analysisD1Prototype = new Prototype("D1", Object.class);
    private Prototype analysisDtPrototype = new Prototype("Dt", Object.class);
    private Prototype analysisI1Prototype = new Prototype("I1", Object.class);
    private Prototype analysisItPrototype = new Prototype("It", Object.class);

    public TellTask(String name) throws UserBadDataError, InternalProcessingError {
        super(name);
        this.addInput(modelOutputPrototype.array());
        this.addOutput(analysisVPrototype);
        this.addOutput(analysisD1Prototype);
        this.addOutput(analysisDtPrototype);
        this.addOutput(analysisI1Prototype);
        this.addOutput(analysisItPrototype);
    }

    public Prototype getAnalysisVPrototype() {
        return analysisVPrototype;
    }

    public Prototype getAnalysisD1Prototype() {
        return analysisD1Prototype;
    }

    public Prototype getAnalysisDtPrototype() {
        return analysisDtPrototype;
    }

    public Prototype getAnalysisI1Prototype() {
        return analysisI1Prototype;
    }

    public Prototype getAnalysisItPrototype() {
        return analysisItPrototype;
    }

    public Prototype<Double> getModelOutputPrototype() {
        return modelOutputPrototype;
    }

    @Override
    protected void process(IContext context, IExecutionContext executionContext, IProgress progress) throws UserBadDataError, InternalProcessingError, InterruptedException {
        Double[] V, D1, Dt, I1, It;
        StringBuilder command = new StringBuilder(200);
        // put the model output into R
        Collection values = context.getLocalValue(modelOutputPrototype.array());
        boolean first = true;
        command.append(ModelOutputVariableName).append(" <- c(");
        for (Object value : values) {
            if (first) {
                first = false;
            } else {
                command.append(",");
            }
            command.append(value);
        }
        command.append(")");
        try {
            R.voidEval(command.toString());
            // Invoke tell
            command = new StringBuilder();
            command.append("tell(").append(RVariableName).append(",");
            command.append(ModelOutputVariableName).append(")");
            R.voidEval(command.toString());
            // Get result
            V = (Double[]) R.eval(COMMAND_FOR_V);
            D1 = (Double[]) R.eval(COMMAND_FOR_D1);
            Dt = (Double[]) R.eval(COMMAND_FOR_DT);
            // compute indices
            I1 = (Double[]) R.eval(COMMAND_FOR_I1);
            It = (Double[]) R.eval(COMMAND_FOR_IT);
        } catch (RException ex) {
            Logger.getLogger(FastPlan.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalProcessingError(ex, "Problem during the communication with R");
        }
        context.putVariable(analysisVPrototype, V);
        context.putVariable(analysisD1Prototype, D1);
        context.putVariable(analysisDtPrototype, Dt);
        context.putVariable(analysisI1Prototype, I1);
        context.putVariable(analysisItPrototype, It);
    }
}
