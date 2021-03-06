/**
 * Created by Romain Reuillon on 06/05/16.
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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmole.core.workflow.task

import monocle.macros.Lenses
import org.openmole.core.context.Context
import org.openmole.core.expansion.FromContext
import org.openmole.core.workflow.builder._
import org.openmole.tool.random._

object ClosureTask {

  implicit def isBuilder: InputOutputBuilder[ClosureTask] = InputOutputBuilder(ClosureTask.config)
  implicit def isInfo: InfoBuilder[ClosureTask] = InfoBuilder(ClosureTask.info)

  def apply(className: String)(closure: (Context, RandomProvider, TaskExecutionContext) ⇒ Context)(implicit name: sourcecode.Name, definitionScope: DefinitionScope): ClosureTask = new ClosureTask(
    closure,
    className = className,
    config = InputOutputConfig(),
    info = InfoConfig()
  )
}

@Lenses case class ClosureTask(
  closure:                (Context, RandomProvider, TaskExecutionContext) ⇒ Context,
  override val className: String,
  config:                 InputOutputConfig,
  info:                   InfoConfig
) extends Task {
  override protected def process(executionContext: TaskExecutionContext): FromContext[Context] = FromContext { p ⇒
    closure(p.context, p.random, executionContext)
  }
}
