import java.lang.String;
import java.lang.Integer;
import java.lang.Math;
import java.lang.Boolean;
import java.util.ArrayList;

import java.util.Scanner;  
import java.util.Random;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class enemyGenerator {

	public static int str, dex, con, intel, wis, cha;	

	public static void main(String args[]) throws FileNotFoundException{

		//////////////////////////////////////////////////////
		//set up
		//////////////////////////////////////////////////////
		Scanner userInput = new Scanner(System.in);
		Scanner spacedUserInput = new Scanner(System.in).useDelimiter(" ");

		//////////////////////////////////////////////////////
		//user input
		//////////////////////////////////////////////////////
		System.out.println("Enter amount of enemies");
		int amtOfEnemies = userInput.nextInt();
		
		System.out.println("Enter enemy level (with spaces, if multiple enemies)");
		String s[] = spacedUserInput.nextLine().split(" ");
		int enemyLevels[] = new int[s.length];
		boolean enoughNumsInputted = true;		

		for(int u=0; u<s.length; u++){
			enemyLevels[u] = Integer.parseInt(s[u]);
		}
		for (int r=0; r<amtOfEnemies; r++){
			CreateEnemy(enemyLevels[r], r+1);
		}
		
	}

	static void CreateEnemy(int enemyLevel, int enemyNumber) throws FileNotFoundException{
		Random random = new Random();  
		
		//////////////////////////////////////////////////////
		//enemy name
		//////////////////////////////////////////////////////
		ArrayList<String> listOfFirstNames = fileToArrayList("FirstNames.txt");
		ArrayList<String> listOfLastNames = fileToArrayList("LastNames.txt");
		int ranFNameNum = random.nextInt(listOfFirstNames.size());
		int ranLNameNum = random.nextInt(listOfLastNames.size());
		String enemyName = listOfFirstNames.get(ranFNameNum) + " " + listOfLastNames.get(ranLNameNum);
	
		//////////////////////////////////////////////////////
		// player stats generator
		//////////////////////////////////////////////////////
		int randStats = random.nextInt(2);
		ArrayList<Integer> statsArr = new ArrayList<Integer>();
		switch (randStats) {
			case 0:
				statsArr.add(15);
				statsArr.add(14);
				statsArr.add(13);
				statsArr.add(12);
				statsArr.add(10);
				statsArr.add(8);
				break;
			case 1:
				int diceTotal = 0;
				int diceRoll = 0;
				boolean firstRoll = true;
				int minRoll = 0;
				for (int j=0; j<6; j++){
					for(int i=0; i < 4; i++){
						while (diceRoll <= 1){
							diceRoll= random.nextInt(6) + 1;	
						}
						if(firstRoll){
							minRoll = diceRoll;
							firstRoll = false;
						}
						if(minRoll > diceRoll){
							minRoll = diceRoll;		
						}						
						diceTotal = diceTotal + diceRoll;
						diceRoll = 0;
					}
					diceTotal = diceTotal - minRoll;
					if(diceTotal <8) { diceTotal = 8; }				
					statsArr.add(diceTotal);
					diceTotal = 0;
					firstRoll = true;
				}
				break;
		}	
		ArrayList<Integer> numsUsed = new ArrayList<Integer>();
		int val = random.nextInt(6);
		str = statsArr.get(val);
		numsUsed.add(val);
		while(numsUsed.contains(val)){
			val = random.nextInt(6);
		}
		dex = statsArr.get(val);
		numsUsed.add(val);
		while(numsUsed.contains(val)){
			val = random.nextInt(6);
		}
		con = statsArr.get(val);
		numsUsed.add(val);		
		while(numsUsed.contains(val)){
			val = random.nextInt(6);
		}
		intel = statsArr.get(val);
		numsUsed.add(val);		
		while(numsUsed.contains(val)){
			val = random.nextInt(6);
		}
		wis = statsArr.get(val);
		numsUsed.add(val);		
		while(numsUsed.contains(val)){
			val = random.nextInt(6);
		}
		cha = statsArr.get(val);
		numsUsed.add(val);

		int strMod = StatMod(str);
		int dexMod = StatMod(dex);
		int conMod = StatMod(con);
		int intelMod = StatMod(intel);
		int wisMod = StatMod(wis);
		int chaMod = StatMod(cha);

		//////////////////////////////////////////////////////
		//ENEMY HEALTH
		//////////////////////////////////////////////////////
		int enemyHealth = 0;
		int m =0;
		int healthMod = 0;
		int healthRoll = 0;
		healthMod = conMod;
		do{
			while (healthRoll <= 1){ healthRoll= random.nextInt(8) + 1; }
			enemyHealth += healthRoll + healthMod;
			healthRoll = 0;
			m++;
		} while(m < enemyLevel);

		//////////////////////////////////////////////////////
		// add in armor		  
		//////////////////////////////////////////////////////
		int armRan = random.nextInt(8);
		String armorName = "";
		int armor = 0;
		int armorMod = 0;
		switch(armRan){
			case 0:
				armor = 11; 
				armorName = "catsuit";
				break;
			case 1:
				armor = 12; 
				armorName = "scout armor";
				break;
			case 2:
				armor = 12; 
				armorName = "scavenger";
				break;
			case 3:
				armor = 13; 
				armorName = "trooper";
				break;
			case 4:
				armor = 14; 
				armorName = "bounty hunter armor";
				break;
			case 5:
				armor = 14; armorName = "ion armor";
				break;
			case 6:
				armor = 16; armorName = "armored spacesuit";
				break;
			case 7:
				armor = 17; armorName = "battle armor";
				break;
		}
		
		if(armRan >= 5){
			armorMod = 0;
		}
		else if (armRan >= 2 || armRan <=4){
			if (dexMod > 2){ armorMod = 2; } else { armorMod = dexMod; }
		}
		else { armorMod = dexMod; }
		if((random.nextInt(10) % 2) == 0){
			armorName += ", with a shield";
			armorMod += 2;
		}
		armor += armorMod;		

		//////////////////////////////////////////////////////
		// damage resistances		  
		//////////////////////////////////////////////////////
		
		ArrayList<String> listOfResistances = fileToArrayList("Resistances.txt");
		ArrayList<String> printRes = new ArrayList<String>();
		int ranResNum = 0;
		if(enemyLevel<= 5){
			printRes.add("none");
		}
		if(enemyLevel>5){
			ranResNum = random.nextInt(listOfResistances.size());
			while(printRes.contains(listOfResistances.get(ranResNum))){
				ranResNum = random.nextInt(listOfResistances.size());
			}
			printRes.add(listOfResistances.get(ranResNum));
		}
		if(enemyLevel>8){
			ranResNum = random.nextInt(listOfResistances.size());
			while(printRes.contains(listOfResistances.get(ranResNum))){
				ranResNum = random.nextInt(listOfResistances.size());
			}
			printRes.add(listOfResistances.get(ranResNum));
		}
		if(enemyLevel>12){
			ranResNum = random.nextInt(listOfResistances.size());
			while(printRes.contains(listOfResistances.get(ranResNum))){
				ranResNum = random.nextInt(listOfResistances.size());
			}
			printRes.add(listOfResistances.get(ranResNum));
		}
		if(enemyLevel>16){
			ranResNum = random.nextInt(listOfResistances.size());
			while(printRes.contains(listOfResistances.get(ranResNum))){
				ranResNum = random.nextInt(listOfResistances.size());
			}
			printRes.add(listOfResistances.get(ranResNum));
		}
		if(enemyLevel>18){
			ranResNum = random.nextInt(listOfResistances.size());
			while(printRes.contains(listOfResistances.get(ranResNum))){
				ranResNum = random.nextInt(listOfResistances.size());
			}
			printRes.add(listOfResistances.get(ranResNum));
		}
		if(enemyLevel>20){
			ranResNum = random.nextInt(listOfResistances.size());
			while(printRes.contains(listOfResistances.get(ranResNum))){
				ranResNum = random.nextInt(listOfResistances.size());
			}
			printRes.add(listOfResistances.get(ranResNum));
		}

		//////////////////////////////////////////////////////
		// enemy abilities		  
		//////////////////////////////////////////////////////
		ArrayList<String> listOfAbilities= fileToArrayList("Abilities.txt");
		ArrayList<String> printAbilities = new ArrayList<String>();
		int ranAbilityNum = 0;
		if(enemyLevel<= 3){
			printAbilities.add("none");
		}
		if(enemyLevel>3){
			ranAbilityNum = random.nextInt(listOfAbilities.size());
			while(printAbilities.contains(listOfAbilities.get(ranAbilityNum))){
				ranAbilityNum = random.nextInt(listOfAbilities.size());
			}
			printAbilities.add(listOfAbilities.get(ranAbilityNum));
		}
		if(enemyLevel>6){
			ranAbilityNum = random.nextInt(listOfAbilities.size());
			while(printAbilities.contains(listOfAbilities.get(ranAbilityNum))){
				ranAbilityNum = random.nextInt(listOfAbilities.size());
			}
			printAbilities.add(listOfAbilities.get(ranAbilityNum));
		}
		if(enemyLevel>10){
			ranAbilityNum = random.nextInt(listOfAbilities.size());
			while(printAbilities.contains(listOfAbilities.get(ranAbilityNum))){
				ranAbilityNum = random.nextInt(listOfAbilities.size());
			}
			printAbilities.add(listOfAbilities.get(ranAbilityNum));
		} 
				
		//////////////////////////////////////////////////////
		// enemy actions		  
		//////////////////////////////////////////////////////	
		ArrayList<String> loMA= fileToArrayList("Actions_Melee.txt");
		ArrayList<String> loRA= fileToArrayList("Actions_Ranged.txt");
		ArrayList<String> printA = new ArrayList<String>();
		int ranMA = random.nextInt(loMA.size());
		int ranRA = random.nextInt(loRA.size());
		while ((ranMA % 3) != 0){
			ranMA = random.nextInt(loMA.size());			
		}
		while ((ranRA % 3) != 0){
			ranRA = random.nextInt(loRA.size());
		}
		String strAddend = "";
		String dexAddend = "";
		if(strMod>=0) { strAddend = "+"; } 
		if(dexMod>=0) { dexAddend = "+"; }		 
		printA.add(loMA.get(ranMA) + strAddend + strMod + " " + loMA.get(ranMA+1) + strAddend + " " + strMod + loMA.get(ranMA+2));
		printA.add(loRA.get(ranRA) + dexAddend + dexMod + " " + loRA.get(ranRA+1) + dexAddend + " " + dexMod + loRA.get(ranRA+2));

		//////////////////////////////////////////////////////
		//output
		//////////////////////////////////////////////////////
		System.out.println("----------------ENEMY #" + enemyNumber + "----------------");
		System.out.println("~~NAME:~~");
		System.out.println(enemyName);
		System.out.println("~~STATS:~~"); 
		System.out.println("str:" + str + "(" + strMod + "), dex:" + dex + "(" + dexMod + "), con:" + con + "(" + conMod + "), int:" + intel + "(" + intelMod + "), wis:" + wis + "(" + wisMod + "), cha:" + cha + "(" + chaMod + ")");
		System.out.println("~~HEALTH:~~");
		System.out.println(enemyHealth + " (" + enemyLevel + "d8+" + (conMod*enemyLevel) + ")");
		System.out.println("~~ARMOR:~~");
		System.out.println(armorName + " <--> AC:" + armor);
		System.out.println("~~RESISTANCES:~~");
		for(String ab : printRes){
			System.out.println(ab);
		}
		System.out.println("~~ABILITIES:~~");
		for(String bc : printAbilities){
			System.out.println(bc);
		}
		System.out.println("~~ACTIONS:~~");
		for(String cd : printA){
			System.out.println(cd);
		} 
	}

	static int StatMod(int con){
		int healthMod = 0;
		if(con < 2) { healthMod = -5; }
		else if(con == 2 || con == 3) { healthMod = -4; }
		else if(con == 4 || con == 5) { healthMod = -3; }		
		else if(con == 6 || con == 7) { healthMod = -2; }
		else if(con == 8 || con == 9) { healthMod = -1; }
		else if(con == 10 || con == 11) { healthMod = 0; }
		else if(con == 12 || con == 13) { healthMod = 1; }
		else if(con == 14 || con == 15) { healthMod = 2; }
		else if(con == 16 || con == 17) { healthMod = 3; }
		else if(con == 18 || con == 19) { healthMod = 4; }
		else if(con == 20 || con == 21) { healthMod = 5; }
		else if(con == 22 || con == 23) { healthMod = 6; }
		else if(con == 24 || con == 25) { healthMod = 7; }
		else if(con == 26 || con == 27) { healthMod = 8; }
		else if(con == 28 || con == 29) { healthMod = 9; }
		else if(con >= 30) { healthMod = 10; }
		return healthMod;
	}

	static ArrayList<String> fileToArrayList(String filename) throws FileNotFoundException{
		ArrayList<String> result = new ArrayList<>();
		try(Scanner s = new Scanner(new FileReader(filename))){
			while (s.hasNext()) {
				result.add(s.nextLine());
			}
			return result;
		}	
		catch(FileNotFoundException ex) { 
			return null; 
		}
	}
}