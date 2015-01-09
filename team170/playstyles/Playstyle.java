package team170.playstyles;

import battlecode.common.*;
import team170.*;

public abstract class Playstyle {
	
	public RobotBroadcaster broadcaster;
	
	public int[] barracksSpawnOrder;
	public int[] minerFactorySpawnOrder;
	public int[] tankFactorySpawnOrder;
	public int[] helipadSpawnOrder;
	public int[] aerospaceLabSpawnOrder;
	
	public double[] civicRatios;
	public double[] beaverRatios;
	public double[] minerRatios;
	public double[] soldierRatios;
	public double[] tankRatios;
		
	public Playstyle() {
				
		this.barracksSpawnOrder = new int[] {};
		this.minerFactorySpawnOrder = new int[] {};
		this.tankFactorySpawnOrder = new int[] {};
		this.helipadSpawnOrder = new int[] {};
		this.aerospaceLabSpawnOrder = new int[] {};
		
		this.civicRatios = new double[] {};
		this.beaverRatios = new double[] {};
		this.minerRatios = new double[] {};
		this.soldierRatios = new double[] {};
		this.tankRatios = new double[] {};
		
	}
	
	// MARK: Budgeting
	
	public void updateBudgeting(int oreMined) throws GameActionException {
		
		int buildOrderProgress = this.buildOrderProgress();
		if (buildOrderProgress == Integer.MAX_VALUE) buildOrderProgress = this.civicRatios.length;
		buildOrderProgress --;
		
		this.updateBudgetingForBuildOrderProgress(oreMined, buildOrderProgress);		
		
	}
	
	public void updateBudgetingForBuildOrderProgress(int oreMined, int progress) throws GameActionException {
        
        final double ratioThreshold = 0.05; // COULD POSSIBLY BE PARAMETER OF PLAYSTYLE
        int remainingOre = oreMined;
       
        // ALLOCATE CIVIC BUDGET
       
        double civicRatio = this.civicRatios[progress];
       
        int budget = (int)(remainingOre * civicRatio);
        this.broadcaster.incrementCivicBudget(budget);
        remainingOre -= budget;
       
        // MODIFY RATIOS BASED ON NEEDS
       
        double totalUnits = this.broadcaster.totalSpawnedRobotCount();
       
        RobotType[] robotTypes = { Beaver.type(), Miner.type(), Soldier.type(), Tank.type() };
        double[] desiredRatios = {
                        this.beaverRatios[progress],
                        this.minerRatios[progress],
                        this.soldierRatios[progress],
                        this.tankRatios[progress] }; // MUST BE SAME ORDER AS ROBOT TYPES (COULD BE HELPER METHOD OR NEW CLASS)
        double[] budgetRatios = { 0.0, 0.0, 0.0, 0.0 }; // DON'T USE THIS, VALUES ARE GENERATED BELOW
       
        int currentRobotCount;
        double actualRobotRatio;
        double desiredRobotRatio;
        boolean ratiosAreOff = false;
       
        double unitsPerDesiredPercentage;
        double maximumUnitsPerDesiredPercentage = 0; // example 50 units that are supposed to represent 0.50 would return 1, this stores the max of all types
        double projectedRobotCount;
        double robotsNeeded;
        double totalRobotsNeeded = 0;
       
        // FIND OUT IF BUDGET NEEDS TO BE ADJUSTED
       
        for (int i = 0; i < robotTypes.length && !ratiosAreOff; i++) {
               
            currentRobotCount = this.broadcaster.robotCountFor(robotTypes[i]);
            actualRobotRatio = currentRobotCount / totalUnits;
            desiredRobotRatio = desiredRatios[i];
           
            if (desiredRobotRatio > 0) { // if the ratio is set to 0 but there are units it'll throw the system off

                if (Math.abs(desiredRobotRatio - actualRobotRatio) > ratioThreshold) {
                       
                    ratiosAreOff = true;

                }

            }
               
        }
       
        if (ratiosAreOff) { // Otherwise just use the passed in ratios
           
            // FIND THE RATIO THAT IS THE MOST ABOVE THE DESIRED RATIO
           
            for (int i = 0; i < robotTypes.length; i++) {
                   
                currentRobotCount = this.broadcaster.robotCountFor(robotTypes[i]);
                actualRobotRatio = currentRobotCount / totalUnits;
                desiredRobotRatio = desiredRatios[i];
               
                if (desiredRobotRatio > 0) { // if the ratio is set to 0 but there are units it'll throw the system off

                    if (actualRobotRatio > desiredRobotRatio) {
                           
                        unitsPerDesiredPercentage = currentRobotCount / (desiredRobotRatio / 0.01);
                        if (unitsPerDesiredPercentage > maximumUnitsPerDesiredPercentage)
                                maximumUnitsPerDesiredPercentage = unitsPerDesiredPercentage;
                       
                        desiredRatios[i] = 0.0; // this type is above desired ratio so don't allocate money this turn
                           
                    }

                }
                   
            }
           
            // FIND THE PROJECTED AMOUNTS OF EACH TYPE
           
            // FIND THE TOTAL UNITS NEEDED
           
            for (int i = 0; i < robotTypes.length; i++) {
                   
                currentRobotCount = this.broadcaster.robotCountFor(robotTypes[i]);
                actualRobotRatio = currentRobotCount / totalUnits;
                desiredRobotRatio = desiredRatios[i];
               
                if (desiredRobotRatio > 0) { // if the ratio is set to 0 but there are units it'll throw the system off

                    if (actualRobotRatio < desiredRobotRatio) {
                           
                        projectedRobotCount = maximumUnitsPerDesiredPercentage * (desiredRobotRatio / 0.01);
                        robotsNeeded = projectedRobotCount - currentRobotCount;
                        totalRobotsNeeded += robotsNeeded;

                    }

                }
                   
            }
           
            // ALLOCATE RATIOS ACCORDINGLY (BASED ON HOW MANY ROBOTS OF THAT TYPE NEED TO BE CREATED VS TOTAL ROBOTS THAT NEED TO BE CREATED)
           
            for (int i = 0; i < robotTypes.length; i++) {
                   
                currentRobotCount = this.broadcaster.robotCountFor(robotTypes[i]);
                actualRobotRatio = currentRobotCount / totalUnits;
                desiredRobotRatio = desiredRatios[i];
               
                if (desiredRobotRatio > 0) { // if the ratio is set to 0 but there are units it'll throw the system off

                    if (actualRobotRatio < desiredRobotRatio) {
                       
                        projectedRobotCount = maximumUnitsPerDesiredPercentage * (desiredRobotRatio / 0.01);
                        robotsNeeded = projectedRobotCount - currentRobotCount;
                        desiredRatios[i] = robotsNeeded / totalRobotsNeeded;

                    }

                }
                   
            }
                                  
           
        }
       
        // CALCULATE BUDGET NEEDED FOR DESIRED RATIOS
       
        double totalBudgetWeight = 0;
        double robotBudgetWeight;
       
        // CALCULATE TOTAL BUDGET WEIGHT
       
        for (int i = 0; i < robotTypes.length; i++) {
        	
        	totalBudgetWeight += robotTypes[i].oreCost * desiredRatios[i];
        	
        }  
       
        // SET BUDGET RATIOS
       
        for (int i = 0; i < robotTypes.length; i++) {
               
            robotBudgetWeight = robotTypes[i].oreCost * desiredRatios[i];
            budgetRatios[i] = robotBudgetWeight / totalBudgetWeight;
               
        }
       
        // INCREMENT BUDGETS
       
        for (int i = 0; i < robotTypes.length; i++) {
               
            budget = (int)(remainingOre * budgetRatios[i]);
           
            if (remainingOre > budget) {
                   
                this.broadcaster.incrementBudget(robotTypes[i], budget);
                remainingOre -= budget;
           
            } else {
                   
                this.broadcaster.incrementBudget(robotTypes[i], remainingOre);
                remainingOre = 0;
                   
            }
               
        }
       
        this.broadcaster.incrementCivicBudget(remainingOre);
       
}
	
	// MARK: Buildings
	
	public RobotType nextBuildingType() throws GameActionException {
		
		int progress = this.buildOrderProgress();
		if (progress == Integer.MAX_VALUE) return null;
				
		for (int value : this.barracksSpawnOrder) if (value == progress) return Barracks.type();
		for (int value : this.minerFactorySpawnOrder) if (value == progress) return MinerFactory.type();
		for (int value : this.tankFactorySpawnOrder) if (value == progress) return TankFactory.type();
		for (int value : this.helipadSpawnOrder) if (value == progress) return Helipad.type();
		for (int value : this.aerospaceLabSpawnOrder) if (value == progress) return AerospaceLab.type();
		
		return null;
		
	}
	
	public int buildOrderProgress() throws GameActionException {
		
		int lowestNumber = Integer.MAX_VALUE;
		
		int totalBarracks = this.broadcaster.robotCountFor(Barracks.type());
		if (this.isBuildOrderLowest(totalBarracks, lowestNumber, this.barracksSpawnOrder)) {
			lowestNumber = this.barracksSpawnOrder[totalBarracks];
		}

		int totalMinerFactories = this.broadcaster.robotCountFor(MinerFactory.type());
		if (this.isBuildOrderLowest(totalMinerFactories, lowestNumber, this.minerFactorySpawnOrder)) {
			lowestNumber = this.minerFactorySpawnOrder[totalMinerFactories];
		}

		int totalTankFactories = this.broadcaster.robotCountFor(TankFactory.type());
		if (this.isBuildOrderLowest(totalTankFactories, lowestNumber, this.tankFactorySpawnOrder)) {
			lowestNumber = this.tankFactorySpawnOrder[totalTankFactories];
		}

		int totalHelipads = this.broadcaster.robotCountFor(Helipad.type());
		if (this.isBuildOrderLowest(totalHelipads, lowestNumber, this.helipadSpawnOrder)) {
			lowestNumber = this.helipadSpawnOrder[totalHelipads];
		}

		int totalAerospaceLabs = this.broadcaster.robotCountFor(AerospaceLab.type());
		if (this.isBuildOrderLowest(totalAerospaceLabs, lowestNumber, this.aerospaceLabSpawnOrder)) {
			lowestNumber = this.aerospaceLabSpawnOrder[totalAerospaceLabs];
		}
				
		return lowestNumber;
		
	}
	
	private Boolean isBuildOrderLowest(int totalUnits, int lowestNumber, int[] buildOrder) {
		
		if (totalUnits >= buildOrder.length) return false;
		int number = buildOrder[totalUnits];
		if (number < lowestNumber) return true;
		return false;
		
	}
	
	// MARK: Static Helpers

	public int identifierI() { return -1; }
	public static int identifierS() { return -1; }

}
