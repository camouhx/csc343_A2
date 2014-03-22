import java.sql.*;

public class Assignment2 {
    
  // A connection to the database  
  Connection connection;
  
  // Statement to run queries
  Statement sql;
  
  // Prepared Statement
  PreparedStatement ps;
  
  // Resultset for the query
  ResultSet rs;
  
  //CONSTRUCTOR
  Assignment2(){

	//System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");
	try {
		//Load JDBC driver
		Class.forName("org.postgresql.Driver");

	} catch (ClassNotFoundException e) {

		//System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
		e.printStackTrace();
		return;
	}
	
	//System.out.println("PostgreSQL JDBC Driver Registered!");
  }
  //Using the input parameters, establish a connection to be used for this session. Returns true if connection is sucessful
  public boolean connectDB(String URL, String username, String password){

	try {

		//Make the connection to the database, ****** but replace "username" with your username ******
		connection = DriverManager.getConnection(URL, username, password);

	} catch (SQLException e) {
		
		//System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return false;
	}

	if (connection != null) {
			//System.out.println("You made it, take control of your database now!");
			return true;
	} else {
			//System.out.println("Failed to make connection!");
			return false;
	}
  }
  
  //Closes the connection. Returns true if closure was sucessful
  public boolean disconnectDB(){

	try {
		connection.close();

	} catch (SQLException e) {
		e.printStackTrace();
		return false;
	} 	
	
      return true;    
  }
    
  //Inserts row into the country table
  public boolean insertCountry (int cid, String name, String coach) {
	
	try {
		sql = connection.createStatement();
		
		// before insert, check if the country cid exist
		String sqlText;
		sqlText = String.format("SELECT * " +
			  		"FROM country " +
			  		"WHERE cid = %d", cid);
		//System.out.println("Now executing the command: " + sqlText.replaceAll("\\s+", " ") + "\n");
		rs = sql.executeQuery(sqlText);
		
		// if ResultSet non-empty, country cid already exists
		if (rs.next() != false)
		{
			//System.out.println("Country cid = " + cid + " exists!");
			return false;
		} 

		rs.close();
		
		// if country cid not found, go ahead and insert
		String sqlText2;
		sqlText2 = String.format("INSERT INTO country " +
			   		 "VALUES (%d, '%s', '%s')", cid, name, coach);
		//System.out.println("Now executing the command: " + sqlText2.replaceAll("\\s+", " ") + "\n");
		sql.executeUpdate(sqlText2);
		
	} catch (SQLException e) {

		e.printStackTrace();
		return false;

	}
	
	return true;
  }
  
  //get the number of players in country "cid"
  public int getPlayersCount(int cid){
	
	try {
		sql = connection.createStatement();

		String sqlText = String.format("SELECT count(*) " +
										"FROM player P, country C " +
										"WHERE C.cid = P.cid AND " +
										"      C.cid = %d  ", cid);
		//System.out.println("Now executing the command: " + sqlText.replaceAll("\\s+", " ") + "\n");
		rs = sql.executeQuery(sqlText);
		
		int num = -1;

		if (rs.next() != false) {
		
			if (rs.getInt(1) != 0) {
			
				num = rs.getInt(1);
			
			}
			
		}
		rs.close();
		return num;

	} catch (SQLException e) {

		e.printStackTrace();
		return -1;
	}
	
  }
	// get the info about player pid
  public String getPlayerInfo(int pid){

	try {
		sql = connection.createStatement();

		String sqlText = String.format("SELECT fname, lname, position, goals " +
										"FROM player " +
										"WHERE pid = %d", pid);
		//System.out.println("Now executing the command: " + sqlText.replaceAll("\\s+", " ") + "\n");
		rs = sql.executeQuery(sqlText);	
			
		if (rs.next() == false) {
		
			rs.close();
			return "";

		}
		
		String resultString = rs.getString("fname") + ":" + rs.getString("lname") + ":" + rs.getString("position") + ":" + rs.getInt("goals");

		rs.close();
		return resultString;

	} catch (SQLException e) {
		
		e.printStackTrace();
		return "";
	}
	
  } 
	
	// change the city name of sid to "newCity"
  public boolean chgStadiumLocation(int sid, String newCity){
	
	try {
			sql = connection.createStatement();
			
			String sqlText = String.format("SELECT * " +
						       "FROM stadium " +
						       "WHERE sid = %d", sid);
										   
			//System.out.println("Now executing the command: " + sqlText.replaceAll("\\s+", " ") + "\n");
			
			rs = sql.executeQuery(sqlText);

			if (rs.next() == false) {
			
				return false;
			
			}
			
			rs.close();
			
			String sqlText2 = String.format(("UPDATE stadium " +
							"SET city = '%s'" +
							"WHERE sid = %d"), newCity, sid);
											
			//System.out.println("Now executing the command: " + sqlText2.replaceAll("\\s+", " ") + "\n");
			
			sql.executeUpdate(sqlText2);

			return true; 
			
	} catch (SQLException e) {

		e.printStackTrace();
		return false;

	}
  }
	// delete the national team specified by cid
  public boolean deleteCountry(int cid){
	
	try {
		
			sql = connection.createStatement();
			
			// create a view of the players in the national team 
			String sqlText1 = String.format("CREATE VIEW playerInTeam AS ("  +
										   "	SELECT P.pid " +
										   " 	FROM player P " +
										   "	WHERE P.cid = %d )", cid);
										   
			sql.executeUpdate(sqlText1);							   
			
			// create a view of the match played by the national team
			String sqlText2 = String.format("CREATE VIEW playedMatches AS ("  +
										   "	 SELECT CP.mid" +
										   " 	 FROM competes CP " +
										   "	 WHERE CP.country1 = %d OR" +
										   "	 	   CP.country2 = %d )", cid, cid);
										   
			sql.executeUpdate(sqlText2);	
			
			// delete from appearance table first
			String sqlText3 = String.format("DELETE " +
											"FROM appearance AP " +
											"WHERE AP.pid IN (SELECT pid FROM playerInTeam)");
											
			sql.executeUpdate(sqlText3);
			
			// delete the players in player table
			String sqlText4 = String.format("DELETE " +
											"FROM player P " +
											"WHERE P.pid IN (SELECT pid FROM playerInTeam)");
											
			sql.executeUpdate(sqlText4);
											
			// delete the competition in competes table
			String sqlText5 = String.format("DELETE " +
											"FROM competes CP " +
											"WHERE CP.mid IN (SELECT mid FROM playedMatches)");
											

			sql.executeUpdate(sqlText5);
			
			// finally delete the country
			String sqlText6 =  String.format("DELETE " +
											 "FROM country " +
											 "WHERE cid = %d ", cid );
			sql.executeUpdate(sqlText6); 
	
			// drop views
			String sqlText7 =  String.format("DROP VIEW playerInTeam; " +
											 "DROP VIEW playedMatches; " );
	
			sql.executeUpdate(sqlText7); 
			return true;
			
	} catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	
  }
  
	// listing the players belongs to club specified by fcname
  public String listPlayers(String fcname){
	
	try {
		sql = connection.createStatement();
		
		String sqlText =  String.format("SELECT P.fname, P.lname, P.position, P.goals, C.name as cname " +
										"FROM player P, country C , club CL " +
										"WHERE P.cid = C.cid AND " +	
										"      P.fcid = CL.fcid AND " +
										"      CL.name = '%s' ", fcname);
		//System.out.println("Now executing the command: " + sqlText.replaceAll("\\s+", " ") + "\n");
	
		rs = sql.executeQuery(sqlText);
		
		String resultString = "";		
		
		while(rs.next()) {

			String temp = rs.getString("fname") + ":" + 
						  rs.getString("lname") + ":" + 
			              rs.getString("position") + ":" + 
			              rs.getString("goals") + ":" + 
			              rs.getString("cname") + "#";
		
		    resultString = resultString + temp;
		
		}
		
		rs.close();
		return resultString;

	} catch (SQLException e) {

		e.printStackTrace();
		return "";
	}
	
  }
  
	// update player values by incrV in country cname
  public boolean updateValues(String cname, int incrV){
    
	try {
		sql = connection.createStatement();
		
		String sqlText =  String.format("SELECT * " +
										"FROM country " +
										"WHERE name = '%s'" , cname);

		rs = sql.executeQuery(sqlText);

		if (rs.next() == false){
			
			rs.close();
			return false;

		}

		String sqlText2 = String.format(("UPDATE player " +
    						             "SET value = value + %d " +
										 "WHERE pid IN (SELECT pid  " +
										 "		FROM player P, country C " +
										 "		WHERE P.cid = C.cid AND " +
										 "      	C.name = '%s')"), incrV, cname);
		sql.executeUpdate(sqlText2);
		return true;
	
	} catch (SQLException e) {

		e.printStackTrace();
		return false;
	}

  }
  
  public String query7(){
	
	try {
	
		sql = connection.createStatement();
		
		String sqlText =  String.format("CREATE VIEW teamsBudget2 AS ( "  +
									    "SELECT P.cid, sum(P.value) AS budget " +
										"FROM player P " +
										"GROUP BY P.cid )"); 
		sql.executeUpdate(sqlText);
		
		String sqlText2 =  String.format("CREATE VIEW topScorer2 AS ( "  +
									     "SELECT P.pid, P.fname, P.lname, P.cid " +
										 "FROM player P " +
										 "WHERE P.goals = (SELECT MAX(goals) FROM player))"); 
		sql.executeUpdate(sqlText2);
		
		String sqlText3 =  String.format("SELECT C.name, C.coach, TB.budget "  +
									     "FROM teamsBudget2 TB, topScorer2 TS, Country C " +
										 "WHERE TS.cid = TB.cid AND " +
										 "			     TB.cid = C.cid AND " +
										 "				 TB.budget = (SELECT MIN(budget) FROM teamsBudget2)"); 
		rs = sql.executeQuery(sqlText3);
		
		String resultString = "";		
		
		while(rs.next()) {

			String temp = rs.getString("name") + ":" + 
						  rs.getString("coach") + ":" + 
			              rs.getString("budget") + "#"; 
		
		    resultString = resultString + temp;
		
		}
		
		String sqlText4 = String.format("DROP VIEW teamsBudget2;" +
										"DROP VIEW topScorer2;");
																		
		sql.executeUpdate(sqlText4);
		
		rs.close();
		return resultString;

	} catch (SQLException e) {

		e.printStackTrace();
		return "";
		
	}
   
  }
  
  
  public boolean updateDB(){
	try {
	
		sql = connection.createStatement();
		
		String sqlText =  String.format("CREATE TABLE valuablePlayers ( " +
										 "pid INTEGER, " +
										 "lname VARCHAR(20) NOT NULL )" );
		sql.executeUpdate(sqlText);
		
		String sqlText2 =  String.format("INSERT INTO valuablePlayers ( " +
											"SELECT P.pid, P.lname " +
											"FROM player P, Appearance AP " +
											"WHERE P.pid = AP.pid AND " +
											"      AP.minutes = 90 " +
											"GROUP BY P.pid " +
											"ORDER BY P.pid ASC )" );
		sql.executeUpdate(sqlText2);
		
		return true;
		
	} catch	(SQLException e) {

		e.printStackTrace();
		return false;

	}
	
  }
  
}
