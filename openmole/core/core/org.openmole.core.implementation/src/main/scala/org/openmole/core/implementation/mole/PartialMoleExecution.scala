/*
 * Copyright (C) 10/06/13 Romain Reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openmole.core.implementation._

import org.openmole.core.model.mole._
import org.openmole.core.implementation.mole._
import org.openmole.misc.workspace._
import org.openmole.core.model.data._

case class PartialMoleExecution(mole: IMole,
                                sources: Source = Sources.empty,
                                hooks: Hooks = Hooks.empty,
                                selection: Map[ICapsule, EnvironmentSelection] = Map.empty,
                                grouping: Map[ICapsule, Grouping] = Map.empty,
                                profiler: Profiler = Profiler.empty,
                                seed: Long = Workspace.newSeed) extends IPartialMoleExecution {
  def complete(implicit implicits: Context = Context.empty, moleExecutionContext: ExecutionContext = ExecutionContext.local) = {
    new MoleExecution(mole,
      sources,
      hooks,
      selection,
      grouping,
      profiler,
      seed)
  }
}
