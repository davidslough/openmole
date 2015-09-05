package org.openmole.gui.misc.js

/*
 * Copyright (C) 13/01/15 // mathieu.leclaire@openmole.org
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

import fr.iscpif.scaladget.api.{BootstrapTags=> bs, ClassKeyAggregator}
import bs._
import rx._
import scalatags.JsDom.all._
import org.openmole.gui.misc.js.JsRxTags._
import scalatags.JsDom.{ tags ⇒ tags }

object Select {
  def apply[T <: Displayable with Identifiable](autoID: String,
                                                contents: Seq[(T, ClassKeyAggregator)],
                                                default: Option[T],
                                                key: ClassKeyAggregator = emptyCK,
                                                onclickExtra: () ⇒ Unit = () ⇒ {}) = new Select(autoID, Var(contents), default, key, onclickExtra)
}

class Select[T <: Displayable with Identifiable](autoID: String,
                                                 val contents: Var[Seq[(T, ClassKeyAggregator)]],
                                                 default: Option[T] = None,
                                                 key: ClassKeyAggregator = emptyCK,
                                                 onclickExtra: () ⇒ Unit = () ⇒ {}) {

  val content: Var[Option[T]] = Var(contents().size match {
    case 0 ⇒ None
    case _ ⇒ default match {
      case None ⇒ Some(contents()(0)._1)
      case _ ⇒
        val ind = contents().indexOf(default.get)
        if (ind != -1) Some(contents()(ind)._1) else Some(contents()(0)._1)
    }
  })

  val glyphMap = contents().toMap

  val selector = buttonGroup()(
    tags.span(
      `class` := "btn " + key.key + " dropdown-toggle", "data-toggle".attr := "dropdown", cursor := "pointer")(
        Rx {
          content().map { c ⇒ bs.glyph(glyphMap(c)) }
        },
        Rx {
          content().map {
            _.name
          }.getOrElse(contents()(0)._1.name) + " "
        },
        bs.span("caret")
      ).render,
    ul(`class` := "dropdown-menu", id := autoID)(
      Rx {
        for (c ← contents().zipWithIndex) yield {
          scalatags.JsDom.tags.li(cursor := "pointer", color := "black", onclick := { () ⇒
            content() = Some(contents()(c._2)._1)
            onclickExtra()
          })(c._1._1.name)
        }
      }
    )
  ).render
}
