package edu.sru.thangiah.zeus.vrp.vrpcostfunctions;

import edu.sru.thangiah.zeus.core.*;
import edu.sru.thangiah.zeus.vrp.*;

/**
 * Cost functions specific to VRP Truck Linked List level
 * <p>Title: Zeus - A Unified Object Oriented Model for VRP's</p>
 * <p>Description: cost functions specific to VRP Truck Linked List level</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sam R. Thangiah
 * @version 2.0
 */
public class VRPTruckLLCostFunctions
    extends VRPAbstractCostFunctions implements
    java.io.Serializable  {

  public double getTotalCost(Object o) {
    VRPTruckLinkedList tLL = (VRPTruckLinkedList) o;
    setTotalCost(o);

    return tLL.getAttributes().getTotalCost();
  }

  /* public double getTotalConstraintCost(Object o) {
     VRPTruckLinkedList tLL = (VRPTruckLinkedList) o;
     setTotalConstraintCost(o);

     return tLL.getAttributes().totalConstraintCost;
   }*/

  /*public double getTotalCrossRoadPenaltyCost(Object o) {
    VRPTruckLinkedList tLL = (VRPTruckLinkedList) o;
    setTotalCrossRoadPenaltyCost(o);

    return tLL.getAttributes().totalCrossRoadPenaltyCost;
     }*/

  /*public double getTotalTurnAroundPenaltyCost(Object o) {
    VRPTruckLinkedList tLL = (VRPTruckLinkedList) o;
    setTotalTurnAroundPenaltyCost(o);

    return tLL.getAttributes().totalTurnAroundPenaltyCost;
     }*/

  public float getTotalDemand(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    setTotalDemand(o);

    return (int) truckLL.getAttributes().getTotalDemand();
  }

  public double getTotalDistance(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    setTotalDistance(o);

    return truckLL.getAttributes().getTotalDistance();
  }

  public double getTotalTravelTime(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    setTotalTravelTime(o);

    return truckLL.getAttributes().getTotalTravelTime();
  }

  public double getMaxTravelTime(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    setMaxTravelTime(o);

    return truckLL.getAttributes().getMaxTravelTime();
  }

  public double getAvgTravelTime(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    setAvgTravelTime(o);

    return truckLL.getAttributes().getAvgTravelTime();
  }

  public void setTotalCost(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    truckLL.getAttributes().setTotalCost(0);

    Truck t = truckLL.getHead();

    while (t != truckLL.getTail()) {
      if (!t.isEmptyMainNodes()) {
        truckLL.getAttributes().setTotalCost(truckLL.getAttributes().getTotalCost() + ProblemInfo.truckLevelCostF.
            getTotalCost(t));
      }

      t = t.getNext();
    }
  }

  /*public void setTotalConstraintCost(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    truckLL.getAttributes().totalConstraintCost = 0;

    Truck t = truckLL.getHead();

    while (t != getTail()) {
      if (!t.isEmpty()) {
        truckLL.getAttributes().totalConstraintCost += ProblemInfo.truckLevelCostF.
            getTotalConstraintCost(t);
      }

      t = t.getNext();
    }
     }*/

  /*public void setTotalCrossRoadPenaltyCost(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    truckLL.getAttributes().totalCrossRoadPenaltyCost = 0;

    Truck t = truckLL.getHead();

    while (t != getTail()) {
      if (!t.isEmpty()) {
        truckLL.getAttributes().totalCrossRoadPenaltyCost += ProblemInfo.
            truckLevelCostF.getTotalCrossRoadPenaltyCost(t);
      }

      t = t.getNext();
    }
     }*/

  /* public void setTotalTurnAroundPenaltyCost(Object o) {
     VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
     truckLL.getAttributes().totalTurnAroundPenaltyCost = 0;

     Truck t = truckLL.getHead();

     while (t != getTail()) {
       if (!t.isEmpty()) {
         truckLL.getAttributes().totalTurnAroundPenaltyCost += ProblemInfo.
             truckLevelCostF.getTotalTurnAroundPenaltyCost(t);
       }

       t = t.getNext();
     }
   }*/

  public void setTotalDemand(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    truckLL.getAttributes().setTotalDemand(0);

    Truck t = truckLL.getHead();

    while (t != truckLL.getTail()) {
      if (!t.isEmptyMainNodes()) {
        truckLL.getAttributes().setTotalDemand(truckLL.getAttributes().getTotalDemand() + ProblemInfo.truckLevelCostF.
            getTotalDemand(t));
      }

      t = t.getNext();
    }
  }

  public void setTotalDistance(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    truckLL.getAttributes().getTotalDistance();

    Truck t = truckLL.getHead();

    while (t != truckLL.getTail()) {
      if (!t.isEmptyMainNodes()) {
        truckLL.getAttributes().setTotalDistance(truckLL.getAttributes().getTotalDistance() + ProblemInfo.truckLevelCostF.
            getTotalDistance(t));
      }

      t = t.getNext();
    }
  }

  public void setTotalTravelTime(Object o) {
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    truckLL.getAttributes().setTotalTravelTime(0);

    Truck t = truckLL.getHead();

    while (t != truckLL.getTail()) {
      if (!t.isEmptyMainNodes()) {
        truckLL.getAttributes().setTotalTravelTime(truckLL.getAttributes().getTotalTravelTime() + ProblemInfo.truckLevelCostF.
            getTotalTravelTime(t));
      }

      t = t.getNext();
    }
  }

  public void setMaxTravelTime(Object o) {
    double max = 0;
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    Truck t = truckLL.getHead();

    while (t != truckLL.getTail()) {
      if (!t.isEmptyMainNodes()) {
        if (ProblemInfo.truckLevelCostF.getMaxTravelTime(t) > max) {
          max = ProblemInfo.truckLevelCostF.getMaxTravelTime(t);
        }
      }

      t = t.getNext();
    }

    truckLL.getAttributes().setMaxTravelTime(max);
  }

  public void setAvgTravelTime(Object o) {
    double avg = 0;
    VRPTruckLinkedList truckLL = (VRPTruckLinkedList) o;
    Truck t = truckLL.getHead();

    if ( (truckLL.getSize() != 0) &&
        (ProblemInfo.truckLLLevelCostF.getTotalDemand(truckLL) != 0)) {
      while (t != truckLL.getTail()) {
        if (!t.isEmptyMainNodes()) {
          avg +=
              (ProblemInfo.truckLevelCostF.getAvgTravelTime(t) *
               ProblemInfo.truckLevelCostF.getTotalDemand(t));
        }

        t = t.getNext();
      }

      truckLL.getAttributes().setAvgTravelTime(avg /
          ProblemInfo.truckLLLevelCostF.getTotalDemand(truckLL));
    }
    else {
      truckLL.getAttributes().setAvgTravelTime(0);
    }
  }

  /** @todo Might not need CrossRoadPenaltyCost and TurnAroundPenaltyCost */
  public void calculateTotalsStats(Object o) {
    setTotalDemand(o);
    setTotalDistance(o);
    setTotalTravelTime(o);
    setMaxTravelTime(o);
    setAvgTravelTime(o);
    //setTotalCrossRoadPenaltyCost(o);
    //setTotalTurnAroundPenaltyCost(o);
    setTotalCost(o);
    //setTotalConstraintCost(o);
  }
}
