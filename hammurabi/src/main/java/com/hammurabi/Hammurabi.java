package com.hammurabi;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Hammurabi {
    Random rand = new Random();
    Scanner scanner = new Scanner(System.in);

	int year = 1;
	int starved = 0;
	int immigrants = 5;
	int population = 100;
	int harvest = 3000;
	int grainPerAcre = 3;
	int ratsDestroy = 200;
	int bushelsOwned = 2800;
	int acres = 1000;
	int price = 19;
	int totalStarved = 0;
	int totalImmigrants = 0;
	int totalLandBought = 0;
	int acresPerPerson = 0;
	int totalBushelsHarvested = 0;
	int score;


    public void playGame() {
		printRules();

		while (true) {
			printSummary();
			int buyAcres = askHowManyAcresToBuy(this.price, this.bushelsOwned);
			this.totalLandBought += buyAcres;
			if (buyAcres == 0) {
				askHowManyAcresToSell();
			}
			int bushelsFedToPeople = askHowMuchGrainToFeedPeople(this.bushelsOwned);
			int acresPlanted = askHowManyAcresToPlant();
			int plagueDeaths = plagueMessage(this.population);
			this.population -= plagueDeaths;
			int starvationDeaths = starvationDeaths(this.population, bushelsFedToPeople);
			this.starved = starvationDeaths;
			this.totalStarved += this.starved;
			this.population -= this.starved;
			Boolean uprising = uprising(this.population, starvationDeaths);
			if (uprising) {
				System.out.println(	"O Great Hammurabi! Too many people have starved to death!\n" + 
									"The people are uprising!");
				System.out.println("\nYou have been kicked out of office.");
				break;
			}
			if (starvationDeaths == 0) {
				this.immigrants = immigrants(this.population, this.acres, this.bushelsOwned);
				this.population += this.immigrants;
				this.totalImmigrants += this.immigrants;
			} else {
				this.immigrants = 0;
			}
			this.harvest = harvest(acresPlanted, acresPlanted*2);
			this.totalBushelsHarvested += this.harvest;
			this.bushelsOwned += harvest;
			this.ratsDestroy = grainEatenByRats(this.bushelsOwned); 
			this.price = newCostOfLand();
			this.year++;
			if (this.year > 10) {
				this.acresPerPerson = this.acres/this.population;
				this.score += (this.acresPerPerson * 10) - this.totalStarved;
				finalSummary();
				break;
			}
		}
    }

	public void printRules() {
		System.out.println("Rules: ");
		System.out.println(	"\nCongratulations, you are the newest ruler of ancient Sumer, elected for a ten year term of office." + 
							"Your duties are to dispense food, direct farming, and buy and sell land as needed to support your people. " + 
							"Watch out for rat infestiations and the plague! Grain is the general currency, measured in bushels. " +
							"The following will help you in your decisions:\n\n" +
								"\tEach person needs at least 20 bushels of grain per year to survive\n" +
								"\tEach person can farm at most 10 acres of land\n" +
								"\tIt takes 2 bushels of grain to farm an acre of land\n" +
								"\tThe market price for land fluctuates yearly\n\n" +
							"Rule wisely and you will be showered with appreciation at the end of your term. " +
							"Rule poorly and you will be kicked out of office!\n");
		System.out.println("Game Start:");
	}

	public void printSummary () {
		System.out.println(	"\nO great Hammurabi!\n" +
							"You are in year " + this.year + " of your ten year rule.\n" +
							"In the previous year " + this.starved + " people starved to death.\n" +
							"In the previous year " + this.immigrants + " people entered the kingdom.\n" +
							"The population is now " + this.population + ".\n" +
							"We harvested " + this.harvest + " bushels at " + this.grainPerAcre + " bushels per acre.\n" +
							"Rats destroyed " + this.ratsDestroy + " bushels, leaving " + this.bushelsOwned + " bushels in storage.\n" +
							"The city owns " + this.acres + " acres of land.\n" +
							"Land is currently worth " + this.price + " bushels per acre.");
	}

	public void finalSummary() {
		System.out.println(	"\nO great Hammurabi!\n" +
							"You have finished your 10 year rule.\n" +
							"In the previous year " + this.immigrants + " people entered the kingdom.\n" +
							"The population is now " + this.population + ".\n" +
							"The city owns " + this.acres + " acres of land.\n" +
							"During your rule, " + this.totalStarved + " people starved.\n" +
							"There are " + this.acresPerPerson + " acres per person in the city.\n" +
							"Your total score is: " + this.score);
		
	}

	public int askHowManyAcresToBuy(int price, int bushelsOwned) {
		while (true) {
			int buyAcres = getNumber("O great Hammurabi, how many acres shall you buy? > ");
			if (this.bushelsOwned >= price * buyAcres) {
				this.acres += buyAcres;
				this.bushelsOwned -= price * buyAcres;
				return buyAcres;
			} else {
				System.out.println("O Great Hammurabi, surely you jest! We have only " +
									this.bushelsOwned + " bushels left!\n");
			}
		}
	}

	public void askHowManyAcresToSell() {
		while (true) {
			int sellAcres = getNumber("O great Hammurabi, how many acres shall you sell? > ");
			if (this.acres >= sellAcres) {
				this.acres -= sellAcres;
				this.bushelsOwned += sellAcres * this.price;
				return;
			} else {
				System.out.println("O Great Hammurabi, surely you jest! We have only " +
									this.acres + " arces!\n");
			}
		}
	}

	public int askHowMuchGrainToFeedPeople(int bushels) {
		while (true) {
			int grainUsedToFeed = getNumber("O great Hammurabi, how many bushels shall you use to feed the people? > ");
			if (this.bushelsOwned >= grainUsedToFeed) {
				this.bushelsOwned -= grainUsedToFeed;
				return grainUsedToFeed;
			} else {
				System.out.println("O Great Hammurabi, surely you jest! We have only " +
									this.bushelsOwned + " bushels!\n");
			}
		}
	}

	public int askHowManyAcresToPlant() {
		while (true) {
			int acresToPlant = getNumber("O great Hammurabi, how many acres shall you plant? > ");
			if (this.bushelsOwned >= acresToPlant * 2 && acresToPlant <= this.population * 10) {
				return acresToPlant;
			} else {
				if (this.bushelsOwned < acresToPlant * 2) {
					System.out.println(	"O Great Hammurabi, surely you jest! We have only " +
										this.bushelsOwned + " bushels!\n");
				} else if (this.population * 10 < acresToPlant) {
					System.out.println(	"O Great Hammurabi, surely you jest! We have only " +
										this.population + " people to farm!\n");
				}
			}
		}
	}

	public int plagueDeaths(int population) {
		int randNum = this.rand.nextInt(100);
		Boolean plague = false;
		if (randNum < 15) {
			plague = true;
		}
		if (plague) {
			return population / 2;
		} else {
			return 0;
		}
	}

	public int plagueMessage(int population) {
		int plagueDeaths = 0;
		plagueDeaths = plagueDeaths(population);
		if (plagueDeaths > 0) {
			System.out.println("O Great Hammurabi, a plague has swept through our nation! " + String.valueOf(plagueDeaths) + " people have died.");
		}
		return plagueDeaths;
	}

	public int starvationDeaths(int population, int bushelsFedToPeople) {
		int peopleFed = bushelsFedToPeople / 20;
		int deaths = population - peopleFed;
		if (deaths <= 0) {
			return 0;
		} else {
			return population - peopleFed;
		}
	}

	public Boolean uprising(int population, int howManyPeopleStarved) {
		double maxDeaths = population * .45;
		if (howManyPeopleStarved > maxDeaths) {
			return true;
		} else {
			return false;
		}
	}

	public int immigrants(int population, int acresOwned, int grainInStorage) {
		return (20 * acresOwned + grainInStorage) / (100 * population) + 1;
	}

	public int harvest(int acres, int bushelsUsedAsSeed) {
		int randNum = this.rand.nextInt(6) + 1;
		this.bushelsOwned -= bushelsUsedAsSeed;
		this.grainPerAcre = randNum;
		int harvest = acres * randNum;
		return harvest;
	}

	public Boolean ratInfestation() {
		int ratInfestation = this.rand.nextInt(100);
		if (ratInfestation < 40) {
			return true;
		} else {
			return false;
		}
	}

	public int grainEatenByRats(int bushels) {
		if (ratInfestation()) {
			double grainEatenPercent = this.rand.nextInt(21) + 10;
			return (int) Math.round((bushels * (grainEatenPercent/100.0)));
		} else {
			return 0;
		}
	}

	public int newCostOfLand() {
		int newCost = this.rand.nextInt(7) + 17;
		return newCost;
	}

	/**
     * Prints the given message (which should ask the user for some integral
     * quantity), and returns the number entered by the user. If the user's
     * response isn't an integer, the question is repeated until the user
     * does give a integer response.
     * 
     * @param message The request to present to the user.
     * @return The user's numeric response.
     */
     public int getNumber(String message) {
        while (true) {
            System.out.print(message);
            try {
                return scanner.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("\"" + scanner.next() + "\" isn't a number!");
            }
        }
    }
}