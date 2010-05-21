/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmole.core.workflow.model.domain;

import org.openmole.core.workflow.model.job.IContext;
import org.openmole.misc.exception.InternalProcessingError;
import org.openmole.misc.exception.UserBadDataError;

/**
 *
 * @author Romain Reuillon <romain.reuillon at openmole.org>
 */
public interface IDomainWithRange<T>  {
    T getRange(IContext context) throws InternalProcessingError, UserBadDataError;
}
