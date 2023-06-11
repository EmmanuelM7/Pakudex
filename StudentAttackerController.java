package edu.ufl.cise.cs1.controllers;
import game.controllers.AttackerController;
import game.models.*;
import java.awt.*;
import java.util.List;

public final class StudentAttackerController implements AttackerController {
	public void init(Game game) {
	}

	public void shutdown(Game game) {
	}

	public int update(Game game, long timeDue) {
		int action = -1;


        //Since we wanted to find the minimum distance from the power pills we set the max distance when initializing
		int distfromPowerPill = Integer.MAX_VALUE;
		Node closestPowerPill = null;

		//Initializing the variables
		List<Defender> defenders = game.getDefenders();
		List<Node> regularPillNodes = game.getPillList();
		List<Node> powerPillNodes = game.getPowerPillList();

		Attacker attacker = game.getAttacker();
		Defender closestDefender = (Defender) attacker.getTargetActor(defenders, true);
		int standsStill = attacker.getReverse();

		for (Node nodeOfPowerPill : powerPillNodes) {

			int temp = attacker.getLocation().getPathDistance(nodeOfPowerPill);
			//if temp is less than distance to power pill then we found a shorten path
			if (temp < distfromPowerPill)
				closestPowerPill = nodeOfPowerPill;
			distfromPowerPill = temp;

		}

		//if power pill does not equal null then there must be pills available
		if (closestPowerPill != null) {
			int distanceToDefender = Integer.MAX_VALUE;

			//Node nearestDefender = null;
			for (Defender defender : defenders) {
				//this lair time refers to when the defenders are not in the box
				if (defender.getLairTime() <= 0) {
					int temp = attacker.getLocation().getPathDistance(defender.getLocation());
					//if temp is less than distance to power pill then we found a shorten path
					if (temp < distanceToDefender) {
						//nearestDefender = defender.getLocation();
						distanceToDefender = temp;
					}
				}
			}


     /* Similar to what TA Marco in the video did I created an if-else chain that compared to the distance the attacker was from
     the defender to the distance the attacker was from the nearest powerpill
            */
			if (distanceToDefender <= 8) {
				action = attacker.getNextDir(closestPowerPill, true);
				//this return "returns" what the attacker needs to do
				return action;

			} else {
				if (distfromPowerPill <= 5) {
					//Remain Still
					action = standsStill;
					return action;

				} else {
					action = attacker.getNextDir(closestPowerPill, true);
				}
			}
		}

		    //if the closest defender is in vulnerable mode then we try to take action and capture it
			if (closestDefender.isVulnerable()) {
				Node locationOfClosestDefender = closestDefender.getLocation();
				action = attacker.getNextDir(locationOfClosestDefender, true);
				return action;
			}

			//this means there were no powerpills and left and so we focus on the regular pills
          if(closestPowerPill == null) {
			  for (Node node : regularPillNodes) {
				  action = attacker.getNextDir(node, true);
				  return action;
			  }

		  }
		if (action == -1) {
			action = attacker.getReverse();

		}
			return action;
		}
	}



