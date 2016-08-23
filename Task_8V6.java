import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Task_8V6 {
	
	/********************************************
	********* Creating class of each ship *******
	********************************************/	
	//unused at this time, maybe for further implementation
	public static class Ship {
		public char shipType;
		public int shipLength;
		public int shipHealth;
		public int posX;
		public int posY;
		public int shipHeading;
	}
	
	public class PatrolBoat extends Ship {{ //silly error when only 1 curly, so 2
		shipType = 'p';
		shipLength = 2;
		shipHealth = shipLength;
	}}
	public class Battleship extends Ship {{
		shipType = 'b';
		shipLength = 3;
		shipHealth = shipLength;
	}}
	public class Submarine extends Ship {{
		shipType = 's';
		shipLength = 3;
		shipHealth = shipLength;
	}}
	public class Destroyer extends Ship {{
		shipType = 'd';
		shipLength = 4;
		shipHealth = shipLength;
	}}
	public class Carrier extends Ship {{
		shipType = 'c';
		shipLength = 5;
		shipHealth = shipLength;
	}}
	
	/****************************************** 
	********* Main code goes here *************
	******************************************/	
	public static void main(String[] args) throws FileNotFoundException {
		
		//initilizes the boards for both players
		Scanner file = new Scanner(new FileReader("C:\\BATTLE.txt"));
		System.out.println("");
		while (file.hasNextLine()) {
			//String temp = file.nextLine();
			System.out.println("\t\t"+file.nextLine());
		}
		int boardWidth = 12;
		int boardHeight = boardWidth;
		Scanner read = new Scanner(System.in);
		int winner = 0;
		char boat = 'U';
		int i;
		boolean shootagain;
		char playerOneBoard[][] = new char[boardWidth][boardHeight];
		char playerTwoBoard[][] = new char[boardWidth][boardHeight];
		int aiAttackedBoard[][] = new int[boardWidth][boardHeight]; //0:Unknown,1:AttackedHit,2:AttackedMiss
		for (i=0;i<playerOneBoard.length;i++) {
			for (int j=0;j<playerOneBoard[0].length;j++) {
				playerOneBoard[i][j]='~';
				playerTwoBoard[i][j]='~';
				aiAttackedBoard[i][j]=0;
			}
		}
		
		// Places all ships
		System.out.println("Place ships yourself?(y/n) :");
		String s = read.nextLine();
		s.toLowerCase();
		System.out.println("\n"+s+"\n");
		for(int j=0;j<2;j++) {
			for (i=0;i<7;i++) {
				switch (i) {
					case 0: boat =  'p';
									break;
					case 1: boat =  'p';
									break;
					case 2: boat =  'b';
									break;
					case 3: boat =  'b';
									break;	
					case 4: boat =  's';
									break;
					case 5: boat =  'd';
									break;
					case 6: boat =  'c';
									break;
					default: break;
					}
					if (j==0) {
						if(s.equals("y")) {
							placeShip(playerOneBoard,1,boat);
						} else {
							RandPlaceShip(playerOneBoard,boat);
						}
					} else RandPlaceShip(playerTwoBoard,boat);
			}
			if(j==0) {
				playerboard(playerOneBoard,1,1);
				System.out.println("Good placement?(y/n):");
				s = read.nextLine();
				if(s.equals("y")) {
				} else {
					for (i=0;i<playerOneBoard.length;i++) {
						for (int k=0;k<playerOneBoard[0].length;k++) {
							playerOneBoard[i][k]='~';
						}
					}
					j--;
				}
			}
		}

		// actual game part here
		while(winner==0) {
			
			// player one attacks
			shootagain=true;
			while(shootagain) {
				playerboard(playerOneBoard,1,1);
				playerboard(playerTwoBoard,1,2);
				shootagain=attack(playerTwoBoard,1,aiAttackedBoard);
				if (isDead(playerTwoBoard)) {
					winner = 1;
				}
			}
			
			// player two attacks
			shootagain=true;
			while(shootagain) {
				shootagain=attack(playerOneBoard,2,aiAttackedBoard);
				if (isDead(playerOneBoard)) {
					winner = 2;
				}
			}
		}
		
		// prints out message to winner
		System.out.println("Player " +winner+ " is the winner!!!");
		
	}
		
	/****************************************** 
	** Prints out the player board to screen **
	******************************************/	
	public static void playerboard(char [][]position,int player, int playerwatching) {
		System.out.println("");
		if (player==playerwatching) System.out.println("             YOU:");
		else System.out.println("             OPPONENT:");
		System.out.print("              A    B    C    D    E    F    G    H    I    J    K    L\n");
		int i,j;
		for (i=0;i<position.length;i++) {
			System.out.print("          " +i);
			if (i<10) System.out.print(" ");
			for (j=0;j<position[i].length;j++) {
				if (player==playerwatching) {	// print board as it is
					System.out.print("[ " +position[i][j]+ " ]");		
				} else {
					if (position[i][j]=='x' ) {  // if not area known then just print sea icon
						System.out.print("[ x ]");
					} else if (position[i][j]=='o') {
						System.out.print("[ o ]");
					} else System.out.print("[ ~ ]");
				}
			}
			System.out.println();
		}
		return;
	}
		
	/*********************************** 
	**** Places a ship to the board ****
	***********************************/	
	public static char[][] placeShip(char position[][], int player, char c) {
		boolean success = false;
		int posX,posY,i,j;
		String boatName;
		int shipLength = 2;
		Scanner read = new Scanner(System.in);
		
		// Gets boat name and ship length from character c sent to method
		switch (c) {
			case 'p':   boatName="Patrol boat";
						shipLength = 2;
						break;
			case 'b': 	boatName="Battleship";
			 			shipLength = 3;			
						break;		
			case 's': 	boatName="Submarine";
 						shipLength = 3;	
						break;
			case 'd': 	boatName="Destroyer";
 						shipLength = 4;	
						break;	
			case 'c': 	boatName="Carrier";
 						shipLength = 5;	
						break;	
			default: 	boatName="Null";
 						shipLength = 1;	
						break;
		}
		
		while(!success) {
			playerboard(position,player,player);
			success = true;
			String s;
			try {
				System.out.print("\nPick a location for a " +boatName+ "(letter,number): ");
				s = read.nextLine();
				String[] split = s.split(",");
				posX = letterToNumber(split[0]);
				posY = Integer.parseInt(split[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Not understood,");
				success = false;
				continue;
			}			
			boolean[] temp = canPlace(position,posX,posY,shipLength);
			for (i=0;i<temp.length;i++) {
				if (temp[i]) {
					switch (i) {
						case 0:	System.out.println("You can place North");
								break;
						case 1:	System.out.println("You can place East");
								break;
						case 2:	System.out.println("You can place South");
								break;
						case 3:	System.out.println("You can place West");
								break;
					}
				}
			}
			if (!temp[0] && !temp[1] && !temp[2] && !temp[3]) {
				System.out.println("You can't place a ship there");
				success = false;
				continue;
			}
			System.out.print("Which direction would you like to put the ship(North,East,South,West)?: ");
			s = read.nextLine();
			s = s.toLowerCase();
			switch (s) {
				case "north": if (temp[0]) j = 1;
							  else {
								  System.out.println("Can't place a ship there! Pick another point.");
								  success = false;
								  continue;
							  }
							  break;
				case "east":  if (temp[1]) j = 2;
							  else {
								  System.out.println("Can't place a ship there! Pick another point.");
								  success = false;
								  continue;
							  }
							  break;
				case "south": if (temp[2]) j = 3;
							  else {
								  System.out.println("Can't place a ship there! Pick another point.");
								  success = false;
								  continue;
							  }
				  			  break;
				case "west":  if (temp[3]) j = 4;
							  else {
								  System.out.println("Can't place a ship there! Pick another point.");
								  success = false;
								  continue;
							  }
							  break;
				default: 	  System.out.println("Not understood,");
				  			  success = false;
				  			  continue;
			}
			position = updateBoardWithShip(position,posX,posY,c,j);
		}
		return position;
	}
	
	
	/***********************************************
	***** Place a ship randomly onto the board *****
	***********************************************/
	public static void RandPlaceShip(char[][] position, char c ) {
		boolean success = false;
		int posX=0,posY=0;
		int shipLength = 2;
		boolean[] temp={false,false,false,false};
		Random rand = new Random();
		switch (c) {
		case 'p':   shipLength = 2;
					break;
		case 'b': 	shipLength = 3;			
					break;		
		case 's': 	shipLength = 3;	
					break;
		case 'd': 	shipLength = 4;	
					break;	
		case 'c': 	shipLength = 5;	
					break;	
		default: 	shipLength = 1;	
					break;
		}
		while(!success) {
			posX = rand.nextInt(position.length);
			posY = rand.nextInt(position.length);
			temp = canPlace(position,posX,posY,shipLength);
			if (!temp[0] && !temp[1] && !temp[2] && !temp[3]);
			else success = true;
		}
		success = false;
		while(!success) {
			int j = rand.nextInt(4);
			if (temp[j]==true) {
				position = updateBoardWithShip(position,posX,posY,c,j+1);
				success=true;
			}
		}
	}
	
	/*********************************************** 
	**** Converts letter to a number(a/1,b/2... ****
	***********************************************/
	public static int letterToNumber(String s) {
		int i;
		s=s.toLowerCase();
		switch(s) {
			case "a":	i = 0;
						break;
			case "b":	i = 1;
						break;
			case "c":	i = 2;
						break;
			case "d":	i = 3;
						break;
			case "e":	i = 4;
						break;
			case "f":	i = 5;
						break;
			case "g":	i = 6;
						break;
			case "h":	i = 7;
						break;
			case "i":	i = 8;
						break;
			case "j":	i = 9;
						break;
			case "k":	i = 10;
						break;
			case "l":	i = 11;
						break;
			default :   i = 999;
						break;
		}
		return i;
	}
	
	/************************************************************* 
	**** Checks if a ship can be placed and in what direction ****
	*************************************************************/
	public static boolean[] canPlace(char[][] position, int posX, int posY, int shipLength) {
		boolean[] dir = new boolean[4]; //[North][East][South][West]
		for (int i = 0; i<dir.length;i++) {
			dir[i]=false;
		}
		boolean success;
		int i;
		
		// only tests if within board
		if (posX<position.length && posY<position.length ) {
			
			//Test okay for North
			success = true;
			for(i = 0; i<shipLength; i++) {
				try {
					if (position[posY-i][posX]=='~'); // do nothing
					else success = false;
				} catch (ArrayIndexOutOfBoundsException e){
					success = false;
				}
			} if (success) dir[0]=true;
			
			//Test okay for East
			success = true;
			for(i = 0; i<shipLength; i++) {	
				try {
					if (position[posY][posX+i]=='~'); // do nothing
					else success = false;
				} catch (ArrayIndexOutOfBoundsException e){
					success = false;
				}
			} if (success) dir[1]=true;
	
			//Test okay for South
			success = true;
			for(i = 0; i<shipLength; i++) {
				try {
					if (position[posY+i][posX]=='~'); // do nothing
					else success = false;
				} catch (ArrayIndexOutOfBoundsException e ){
					success = false;
				}
			} if (success) dir[2]=true;
			
			//Test okay for West
			success = true;
			for(i = 0; i<shipLength; i++) {	
				try {
					if (position[posY][posX-i]=='~'); // do nothing
					else success = false;
				} catch (ArrayIndexOutOfBoundsException e){
					success = false;
				}
			} if (success) dir[3]=true;		
		}
		return dir;
	}
	
	/**************************************************************** 
	**** Places a ship in specified direction, no error checking ****
	****************************************************************/
	public static char[][] updateBoardWithShip(char [][]position,int posX, int posY, char c, int direction) {
		int i;
		int shipLength;
		switch (c) {
			case 'p':   shipLength=2;
						break;
			case 'b':   shipLength=3;
						break;
			case 's':   shipLength=3;
						break;	
			case 'd':   shipLength=4;
						break;		
			case 'c':   shipLength=5;
						break;
			default:	shipLength=2;
						break;
		}
		if (direction==1) {
			for (i=0;i<shipLength;i++) position[posY-i][posX]=c;
		} else if (direction==2) {
			for (i=0;i<shipLength;i++) position[posY][posX+i]=c;
		} else if (direction==3) {
			for (i=0;i<shipLength;i++) position[posY+i][posX]=c;
		} else if (direction==4) {
			for (i=0;i<shipLength;i++) position[posY][posX-i]=c;
		}
		return position;
	}
	
	
	/*************************************************************************** 
	**** Attacks the player board in the location specified, no error check ****
	****************************************************************************/
	public static boolean isHit(char [][]position,int posX, int posY) {
		if (position[posY][posX]=='~') {
			System.out.println("Miss!");
			position[posY][posX]='o';
			return false;
		} else {
			System.out.println("Hit!");
			position[posY][posX]='x';
			return true;
		}
	}
	
	/********************************************************************** 
	**** Asks the player where they want to attack the opponents board ****
	**********************************************************************/
	public static boolean attack(char [][]position, int player, int [][]attacked) {
		Scanner read = new Scanner(System.in);
		if (player==1) System.out.print("\nPlayer"+player);
		else System.out.print("Ai attacks,");
		int posX=0,posY=0;
		String s;
		boolean success = false;
		if (player==1) {
			while(!success) {	
				success = true;
				try {
					System.out.print(", where would you like to attack? (letter,number): ");
					s = read.nextLine();
					String[] split = s.split(",");
					posX = letterToNumber(split[0]);
					if (posX==999) {
						System.out.println("Not understood,");
						success = false;
						continue;
					}
					try {
						posY = Integer.parseInt(split[1]);
					} catch (NumberFormatException e) {
						System.out.println("Not understood,");
						success = false;
						continue;
					}
					if(isNotValid(position,posX,posY)) {
						success = false;
						System.out.println("Not a valid target!");
						continue;					
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Not understood,");
					success = false;
					continue;
				}
			}
		} else {
			int[] getPos = aiTurn(position,attacked);
			posX = getPos[0];
			posY = getPos[1];		
			System.out.print(intToString(getPos[0])+","+getPos[1]+"..."); 
		}
		boolean temp = isHit(position,posX,posY);
		return temp;
	}
		
	/************************************************ 
	**** Checks whether an attack target is valid ***
	************************************************/
	public static boolean isNotValid(char[][] position, int posX, int posY) {
		if (position[posY][posX]=='x' || position[posY][posX]=='o') return true;
		else return false;
	}
	
	/**************************************** 
	**** Checks whether a player is dead ****
	****************************************/
	public static boolean isDead(char[][] position) {
		boolean dead = true;
		for (int i = 0; i<position.length; i++) {
			for (int j = 0; j<position.length; j++) {
				if (position[i][j]!='~' && position[i][j]!='x' && position[i][j]!='o') {
					dead = false;
				}
			}
		}
		return dead;
	}
	
	/******************************** 
	**** Simplistic AI behaviour ****
	********************************/
	public static int[] aiTurn(char[][] position, int[][] attacked) {
		Random rand = new Random();
		int []attackPos = new int[2];
		while (true) {
			int posX = rand.nextInt(position.length);
			int posY = rand.nextInt(position.length);
			if (attacked[posY][posX]==0) {
				attacked[posY][posX]=1;
				attackPos[0]=posY;
				attackPos[1]=posX;
				return attackPos;
			}
		}
	}
	
	/****************************************** 
	**** Converts int to char, a=1,b=2,etc ****
	******************************************/
	public static char intToString(int a) {
		switch(a) {
			case 0: return 'A';
			case 1: return 'B';
			case 2: return 'C';
			case 3: return 'D';
			case 4: return 'E';
			case 5: return 'F';
			case 6: return 'G';
			case 7: return 'H';
			case 8: return 'I';
			case 9: return 'J';
			case 10: return 'K';
			case 11: return 'L';	
			default: return 'Z';
		}
	}
}
