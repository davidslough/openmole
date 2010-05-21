/*
 *
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
package org.openmole.plugin.domain.interval;

import java.util.ArrayList;
import java.util.List;
import org.openmole.core.workflow.implementation.domain.Interval;
import org.openmole.misc.exception.InternalProcessingError;
import org.openmole.misc.exception.UserBadDataError;
import org.openmole.core.workflow.implementation.tools.VariableExpansion;
import org.openmole.core.workflow.model.job.IContext;

public class RangeInteger extends UniformelyDiscretizedIntervalDomain<Integer> {

    public RangeInteger() {
        this("0","1");
    }

    public RangeInteger(String min, String max) {
        this(min, max, "1");
    }

    public RangeInteger(String min, String max, String step) {
        this(new IntegerInterval(min, max), step);
    }

    public RangeInteger(Interval<Integer> interval, String step) {
        super(interval, step);
    }



    @Override
    public List<Integer> computeValues(IContext context) throws InternalProcessingError, UserBadDataError {
        Integer min = getInterval().getMin(context);
        Integer max = getInterval().getMax(context);
        Integer step = new Integer(VariableExpansion.getInstance().expandData(context, getStep()));

        int size =  Math.abs(max - min) / step;

        List<Integer> val = new ArrayList<Integer>(size);

        Integer cur = min;

        for (int i = 0; i <= size; i++) {
            val.add(cur);
            cur += step;
        }
        return val;
    }

    @Override
    public Integer getCenter(IContext context) throws InternalProcessingError, UserBadDataError {
        Integer min = getInterval().getMin(context);
        return min + ((getInterval().getMax(context) - min) / 2);
    }

    @Override
    public Integer getRange(IContext context) throws InternalProcessingError, UserBadDataError {
        return getInterval().getMax(context) - getInterval().getMin(context);
    }
}
