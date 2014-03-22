public class a2test {
	
	public static void main(String[] args) {
	
		System.out.println("In Main:");
		
		Assignment2 a2 = new Assignment2();
		String url = args[0];
		String username = args[1];
		String password = args[2];
		
		//connect to DB
		System.out.println("Connect returns : " + a2.connectDB(url,username,password));
		
		/*
		 *		 test insertCountry
		 */
		//System.out.println("Inserting France (cid = 7) to country returns: " + a2.insertCountry(7,"France", "Coach FRA"));
	
		//System.out.println("Inserting *EXISTING* country Spain (cid = 6) to country returns: " + a2.insertCountry(6,"Spain", "Coach ESP"));
		
		/*
		 *		 test getPlayersCount
		 */
		//System.out.println("Getting player count of country 3 - England returns: " + a2.getPlayersCount(3)); 
		
		//System.out.println("Getting player count of *NON-EXISTING* country 8 - China returns: " + a2.getPlayersCount(8)); 
		 
		 
		 /*
		  *		 test getPlayerInfo
		  */
		 
		//System.out.println("Getting player info of pid=1 returns: " + a2.getPlayerInfo(1)); 
		 
		//System.out.println("Getting player info of pid=16 returns: " + a2.getPlayerInfo(16)); 
		
		//System.out.println("Getting non-existing player info of pid=99 returns: " + a2.getPlayerInfo(99)); 
		 

		 /*
		  *		 test chgStadiumLocation
		  */
		
		//System.out.println("Changing stadium sid = 1 location returns: " + a2.chgStadiumLocation(1, "new city 1")); 

		//System.out.println("Changing stadium sid = 5 location returns: " + a2.chgStadiumLocation(5, "new city 5")); 

		//System.out.println("Changing non-existing stadium sid = 20 location returns: " + a2.chgStadiumLocation(20, "new city 20")); 
		 
		
		/*
		 *		 test deleteCountry
		 */
		
		//System.out.println("Deleting country cid = 5 - Germany returns: " + a2.deleteCountry(5)); 

		/*
		 *		 test listPlayers
		 */

		//System.out.println("Listing players from fcname = Chelsea returns: " + a2.listPlayers("Chelsea")); 

		//System.out.println("Listing players from fcname = Man City returns: " + a2.listPlayers("Man City")); 


		/*
		 *		 test updateValues
		 */

		//System.out.println("Updating players from country = Brazil returns: " + a2.updateValues("Brazil", 20)); 

		//System.out.println("Updating players from country = China returns: " + a2.updateValues("China", 500)); 


		/*
		 *		 test query7
		 */

		//System.out.println("Executing query 7 returns: " + a2.query7()); 

		
		/*
		 *		 test updateDB
		 */
		
		System.out.println("Executing updateDB returns: " + a2.updateDB()); 
		
		// disconnect from DB
		System.out.println("Disconnect returns : " + a2.disconnectDB());
	}
}

