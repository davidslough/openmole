/*
 * Copyright (C) 2010 Romain Reuillon
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
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openmole.core.workflow.transition

import org.openmole.core.context.{ Context, Variable }
import org.openmole.core.exception.InternalProcessingError
import org.openmole.core.workflow.mole._

import scala.collection.mutable.ListBuffer

object DataChannel {
  def levelDelta(mole: Mole)(dataChannel: DataChannel): Int =
    mole.level(dataChannel.end.capsule) - mole.level(dataChannel.start)

  def apply(start: Capsule, end: Slot, filter: BlockList) = new DataChannel(start, end, filter)

  /**
   * Consums the provided variables and construct a context for them.
   *
   * @param ticket the ticket of the current execution
   * @param moleExecution the current mole execution
   * @return the variables which have been transmitted through this data channel
   */
  def consums(dataChannel: DataChannel, ticket: Ticket, moleExecution: MoleExecution): Iterable[Variable[_]] = {
    val delta = dataChannel.levelDelta(moleExecution.mole)
    val dataChannelRegistry = moleExecution.dataChannelRegistry

    val vars =
      if (delta <= 0) dataChannelRegistry.remove(dataChannel, ticket).getOrElse(new ListBuffer[Variable[_]])
      else {
        val workingOnTicket = (0 until delta).foldLeft(ticket) {
          (c, e) ⇒ c.parent.getOrElse(throw new InternalProcessingError("Bug should never get to root."))
        }
        dataChannelRegistry.consult(dataChannel, workingOnTicket) getOrElse (new ListBuffer[Variable[_]])
      }

    vars.toVector
  }

  /**
   * Provides the variable for future consuption by the matching execution of
   * the ending task.
   *
   * @param fromContext the context containing the variables
   * @param ticket the ticket of the current execution
   * @param moleExecution the current mole execution
   */
  def provides(dataChannel: DataChannel, fromContext: Context, ticket: Ticket, moleExecution: MoleExecution): Unit = {
    val delta = dataChannel.levelDelta(moleExecution.mole)
    val dataChannelRegistry = moleExecution.dataChannelRegistry

    if (delta >= 0) {
      val toContext = ListBuffer() ++ fromContext.values.filterNot(v ⇒ dataChannel.filter(v.prototype))
      dataChannelRegistry.register(dataChannel, ticket, toContext)
    }
    else {
      val workingOnTicket = (delta until 0).foldLeft(ticket) {
        (c, e) ⇒ c.parent.getOrElse(throw new InternalProcessingError("Bug should never get to root."))
      }
      val toContext = dataChannelRegistry.getOrElseUpdate(dataChannel, workingOnTicket, new ListBuffer[Variable[_]])
      toContext ++= fromContext.values.filterNot(v ⇒ dataChannel.filter(v.prototype))
    }
  }

}

/**
 * A data channel allow to transmit data between remotes task capsules within a mole.
 * Two capsules could be linked with a {@link DataChannel} if:
 *      - they belong to the same mole,
 *      - there is no capsule with more than one input slot in a path between
 *        the two capsules.
 *
 * @param start the capsule from which the data channel starts
 * @param end the capsule to which the data channel ends
 * @param filter the filter of the variable transported by this data channel
 */
class DataChannel(
  val start:  Capsule,
  val end:    Slot,
  val filter: BlockList
) {

  /**
   *
   * Get the set of data of that will actually be transmitted as input to the
   * ending task capsule. This is computed by intersecting the set of variable
   * names transported by this data channel and the set of input of the ending
   * task.
   *
   * @return the transmitted data
   */
  def data(mole: Mole, sources: Sources, hooks: Hooks) =
    start.outputs(mole, sources, hooks).filterNot(d ⇒ filter(d))

  def levelDelta(mole: Mole): Int = DataChannel.levelDelta(mole)(this)

  override def toString = s"$start oo $end"

}
