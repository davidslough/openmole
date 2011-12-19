/*
 * Copyright (C) 2011 Mathieu leclaire <mathieu.leclaire at openmole.org>
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

package org.openmole.ide.core.implementation.workflow

import scala.collection.mutable.HashMap
import org.apache.commons.collections15.bidimap.DualHashBidiMap
import org.openmole.ide.core.model.commons.TransitionType
import org.openmole.ide.core.model.workflow.ICapsuleUI
import org.openmole.ide.core.model.workflow.IInputSlotWidget
import org.openmole.ide.core.model.workflow.IMoleSceneManager
import org.openmole.ide.core.model.commons.Constants._
import org.openmole.ide.core.model.workflow.ITransitionUI
import scala.collection.JavaConversions._
import scala.collection.mutable.HashSet

class MoleSceneManager(var startingCapsule: Option[ICapsuleUI]= None) extends IMoleSceneManager{
  
  var capsules= new DualHashBidiMap[String, ICapsuleUI]
  var transitionMap= new DualHashBidiMap[String, ITransitionUI]
  var capsuleConnections= new HashMap[ICapsuleUI, HashSet[ITransitionUI]]
  var nodeID= 0
  var edgeID= 0
  var name: Option[String]= None
  
  override def setStartingCapsule(stCapsule: ICapsuleUI) = {
    startingCapsule match {
      case Some(x: ICapsuleUI)=> x.defineAsStartingCapsule(false)
      case None=>
    }
    startingCapsule= Some(stCapsule)
    startingCapsule.get.defineAsStartingCapsule(true)
  }
  
  override def getNodeID: String= "node" + nodeID
  
  def getEdgeID: String= "edge" + edgeID
  
  override def registerCapsuleUI(cv: ICapsuleUI) = {
    nodeID+= 1
    capsules.put(getNodeID,cv)
    if (capsules.size == 1) {
      startingCapsule= Some(cv)}
    capsuleConnections+= cv-> HashSet.empty[ITransitionUI]
  }
  
  override def removeCapsuleUI(nodeID: String) = {
    startingCapsule match {
      case None=>
      case Some(caps)=> if (capsules.get(nodeID) == caps) startingCapsule = None
    }
    
    //remove following transitionMap
    capsuleConnections(capsules.get(nodeID)).foreach(transitionMap.removeValue(_))
    
    //remove incoming transitionMap
    removeIncomingTransitions(capsules.get(nodeID))
    
    capsules.remove(nodeID)
  }
  
  override def capsuleID(cv: ICapsuleUI) = capsules.getKey(cv)
  
  override def transitions= transitionMap.values 
  
  override def transition(edgeID: String) = transitionMap.get(edgeID)
  
  private def removeIncomingTransitions(capsule: ICapsuleUI) = transitionMap.foreach(t => {if (t._2.target.capsule.equals(capsule)) removeTransition(t._1)})
  
  
  override def removeTransition(edge: String) = transitionMap.remove(edge)
  
  override def registerTransition(s: ICapsuleUI, t:IInputSlotWidget,transitionType: TransitionType.Value,cond: Option[String]): Boolean = {
    edgeID+= 1
    registerTransition(getEdgeID,s,t,transitionType,cond)
  }
  
  override def registerTransition(edgeID: String,s: ICapsuleUI, t:IInputSlotWidget,transitionType: TransitionType.Value,cond: Option[String]): Boolean = {
   if (!transitionMap.keys.contains(edgeID)) { 
      val transition = new TransitionUI(s,t,transitionType,cond)
      transitionMap.put(edgeID, transition)
      capsuleConnections(transition.source)+= transition
      return true
    }
    false
  }
  
  private def removeTransitonsBeforeStartingCapsule = {
    val l = new HashSet[String]
    transitionMap.foreach{t=> 
      if (t._2.target.capsule.equals(startingCapsule.get)) l += t._1}
    l.foreach{removeTransition(_)}
    l
  } 
}
