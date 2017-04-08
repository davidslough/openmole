/*
 * Copyright (C) 11/03/13 Romain Reuillon
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

package org.openmole.plugin.source.file

import java.io.File

import monocle.macros.Lenses
import org.openmole.core.context.{ Context, Val }
import org.openmole.core.dsl._
import org.openmole.core.exception.UserBadDataError
import org.openmole.core.expansion.FromContext
import org.openmole.core.serializer._
import org.openmole.core.workflow.builder.{ InputOutputBuilder, InputOutputConfig }
import org.openmole.core.workflow.mole._

object LoadSource {

  implicit def isIO = InputOutputBuilder(LoadSource.config)

  def apply(file: FromContext[String], prototypes: Val[_]*)(implicit serializerService: SerializerService, name: sourcecode.Name) =
    new LoadSource(
      file,
      prototypes.toVector,
      config = InputOutputConfig(),
      serializerService = serializerService
    ) set (outputs += (prototypes: _*))

}

@Lenses case class LoadSource(
    file:              FromContext[String],
    prototypes:        Vector[Val[_]],
    config:            InputOutputConfig,
    serializerService: SerializerService
) extends Source {

  override protected def process(executionContext: MoleExecutionContext) = FromContext { parameters ⇒
    import parameters._
    val from = new File(file.from(context))
    val (loadedContext, fs) = serializerService.deserialiseAndExtractFiles[Context](from)
    fs.foreach(executionContext.services.fileService.deleteWhenGarbageCollected)
    context ++ prototypes.map(p ⇒ loadedContext.variable(p).getOrElse(throw new UserBadDataError(s"Variable $p has not been found in the loaded context")))
  }

}
