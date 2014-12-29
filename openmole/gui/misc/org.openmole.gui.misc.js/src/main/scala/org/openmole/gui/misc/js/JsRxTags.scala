package org.openmole.gui.misc.js

/*
 * Copyright (C) 21/07/14 // mathieu.leclaire@openmole.org
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

import scala.util.{ Failure, Success }
//import scalatags.JsDom.tags._
//import scalatags.JsDom.attrs._
//import scalatags.JsDom.tags2._
import scalatags.JsDom.all._
import rx._
import org.scalajs.dom.Element

/**
 * A minimal binding between Scala.Rx and Scalatags and Scala-Js-Dom
 */
object JsRxTags {

  /**
   * Wraps reactive strings in spans, so they can be referenced/replaced
   * when the Rx changes.
   */
  implicit def RxStr[T](r: Rx[T])(implicit f: T ⇒ Frag): Frag = {
    rxMod(Rx(span(r())))
  }

  /**
   * Sticks some Rx into a Scalatags fragment, which means hooking up an Obs
   * to propagate changes into the DOM via the element's ID. Monkey-patches
   * the Obs onto the element itself so we have a reference to kill it when
   * the element leaves the DOM (e.g. it gets deleted).
   */
  implicit def rxMod(r: Rx[HtmlTag]): Frag = {
    def rSafe = r.toTry match {
      case Success(v) ⇒ {
        v.render
      }
      case Failure(e) ⇒ {
        span(e.toString).render
      }
    }
    var last = rSafe
    Obs(r, skipInitial = true) {
      val newLast = rSafe
      last.parentElement.replaceChild(newLast, last)
      last = newLast
    }
    bindNode(last)
  }

  implicit def RxAttrValue[T: AttrValue] = new AttrValue[Rx[T]] {
    def apply(t: Element, a: Attr, r: Rx[T]): Unit = {
      Obs(r) {
        implicitly[AttrValue[T]].apply(t, a, r())
      }
    }
  }

  implicit def RxStyleValue[T: StyleValue] = new StyleValue[Rx[T]] {
    def apply(t: Element, s: Style, r: Rx[T]): Unit = {
      Obs(r) {
        implicitly[StyleValue[T]].apply(t, s, r())
      }
    }
  }

}
