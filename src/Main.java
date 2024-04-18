/* Create a Java program to manage a sports league's team rosters using nested
HashMaps.Each team's roster is represented by a HashMap where the keys are the player surnames,
and the values are another HashMap containing the player's given name and jersey number.
The teams themselves are stored in the outer HashMap where the keys are the team names.
Features to Implement:
Add Team Feature:
>>> Allow the user to add a new team to the league. >>> Ensure that the team name is unique.
Add Player Feature:
>>> Allow the user to add a player to the roster of a specific team.
>>> Prompt the user to enter the player's surname, given name, jersey number, and team name.
>>> Ensure that the jersey number is unique within the team's roster.
Search Player Feature:
>>> Prompt the user to enter the player's name and surname.
>>> Display the player's name, jersey number, and team name if found.
>>> If the player is not found, notify the user accordingly. */

import com.jogamp.common.os.Platform;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HashMap<String, HashMap<String, HashMap<String, String>>> masterTable = new HashMap<>();
        HashMap<String, HashMap<String, String>> teamRoster = new HashMap<>();
        HashMap<String, String> playerDetail = new HashMap<>();

        populateTeams(masterTable, teamRoster, playerDetail);

        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        System.out.println(" > > > > > N H L  T E A M  &  P L A Y E R  M A N A G E R < < < < <");
        System.out.println();

        while (!userInput.equalsIgnoreCase("q")) {
            startSelection();
            System.out.print("Please select function = ");
            userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("q")) {
                System.out.println("The program will quit.");

            } else if (userInput.length() != 1) {
                System.out.println("User shall enter only one character number. Size of entered characters does not match program requirement.");

            } else if (!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3") && !userInput.equals("4")) {
                System.out.println("Invalid input. User shall enter only number values = 1, 2, 3, 4 for function selection.");

            } else {
                switch (userInput) {
                    case "1" :
                        addPlayer(scanner, masterTable, teamRoster, playerDetail);
                        break;
                    case "2" :
                        addTeam(scanner, masterTable);
                        break;
                    case "3" :
                        searchPlayer(scanner, masterTable);
                        break;
                    case "4" :
                        listTeams(masterTable);
                        break;
                    default:
                        System.out.println("System error. Contact your admin.");
                        userInput = "q";
                        break;
                }
            }
        }
        System.out.println("Program ends.");
    }








    public static void addPlayer (Scanner scanner, HashMap<String, HashMap<String, HashMap<String, String>>> masterTable,
                                  HashMap<String, HashMap<String, String>> teamRoster,
                                  HashMap<String, String> playerDetail) {
        String playerName = "";
        String playerSurname = "";
        String playerTeam = "";
        String jerseyNumber = "";
        boolean playerValidation = true;
        boolean teamValidation = true;
        boolean jerseyNumberValidation = false;

        while (playerValidation) {
            System.out.print("Player first name: ");
            playerName = scanner.nextLine();

            System.out.print("Player last name: ");
            playerSurname = scanner.nextLine();

            if (playerName.isBlank() || playerName.isEmpty() || playerSurname.isEmpty() || playerSurname.isBlank()) {
                System.out.println("Invalid input. Value of new player name might not include only white spaces, or must not be empty.");
            } else {
                playerValidation = false;
                break;
            }
        }

        while (teamValidation) {
            System.out.println("Note: Player team must be from the existing teams.");
            System.out.print("Player team: ");
            playerTeam = scanner.nextLine();

            for (Map.Entry<String, HashMap<String, HashMap<String, String>>> entry : masterTable.entrySet()) {
                String teamName = entry.getKey();
                if (teamName.equalsIgnoreCase(playerTeam)) {
                    playerTeam = teamName;
                    teamValidation = false;
                    break;
                }
            }
            if (teamValidation) {
                System.out.println("No match in the database for the team name entered by user.");
            }
        }

        while (!jerseyNumberValidation) {
            System.out.println("Jersey number must be 01 - 99 in two digit format, and must be unique per team.");
            System.out.print("Player jersey number: ");
            jerseyNumber = scanner.nextLine().trim();
            HashMap<String, HashMap<String, String>> teamMap = masterTable.get(playerTeam);

            if (jerseyNumber.length() != 2) {
                System.out.println("Invalid input. Program expects jersey number format as two digits, e.g. 01, 08, 23, etc.");
                break;
            }  else if (!Character.isDigit(jerseyNumber.charAt(0)) || !Character.isDigit(jerseyNumber.charAt(1))) {
                System.out.println("Invalid input. Program expects jersey number as integer, you entered alphabetic character or special character.");
                break;

            } else {
                jerseyNumberValidation = true;
                for (Map.Entry<String, HashMap<String, String>> playerEntry : teamMap.entrySet()) {
                    if (playerEntry.getValue().equals(playerTeam)) {
                        for (Map.Entry<String, String> playerNumber : playerEntry.getValue().entrySet()) {
                            if (playerNumber.getValue().equals(jerseyNumber)) {
                                String existingPlayerSurname = playerEntry.getKey();
                                String existingPlayerFirstName = playerNumber.getKey();
                                jerseyNumberValidation = false;
                                System.out.println("The entered number " + jerseyNumber + " is already taken by player: " + existingPlayerFirstName + " " + existingPlayerSurname + ".");
                                break;
                            }
                        }
                        if (!jerseyNumberValidation) {
                            break;
                    }
                    }
                }
            }
        }

        teamRoster = masterTable.get(playerTeam);
        if (teamRoster == null) {
            teamRoster = new HashMap<>();
        }

        HashMap<String, String> newPlayerDetail = new HashMap<>();
        newPlayerDetail.put(playerName, jerseyNumber);
        teamRoster.put(playerSurname, newPlayerDetail);
        masterTable.put(playerTeam, teamRoster);

        System.out.println("* * * * * Player data updated based on user input * * * * * ");
        System.out.println(" * " + jerseyNumber + "|" + playerSurname + "|" + playerName + "|" + playerTeam);
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    }

    public static void addTeam (Scanner scanner, HashMap<String, HashMap<String, HashMap<String, String>>> masterTable) {
        String team = "";
        boolean teamValidation = true;

        while (teamValidation) {
            System.out.println("Player team must not be from the existing teams.");
            System.out.print("Name of the new NHL team: ");
            team = scanner.nextLine();

            if (team.isEmpty() || team.isBlank()) {
                System.out.println("Invalid input. Value of new team name might not include only white spaces, or must not be empty. User input = '" + team +"'.");

            }  else if (masterTable.containsKey(team)) {
                System.out.println("New team name must not match any existing team name. Please enter different team name.");
            } else {
                teamValidation = false;
                masterTable.put(team, new HashMap<>());
                System.out.println("New team added into database with name = '" + team + "'.");
            }
        }
    }






    public static void searchPlayer (Scanner scanner, HashMap<String, HashMap<String, HashMap<String, String>>> masterTable) {
        String player = "";
        boolean playerValidation = true;

        while (playerValidation) {
            System.out.print("Player last name to look up: ");
            player = scanner.nextLine().trim();
            System.out.println();

            if (player.isBlank() || player.isEmpty()) {
                System.out.println("Invalid input. Value of player name to look up might not include only white spaces, or must not be empty.");
                break;
            } else {
                System.out.println("List of NHL player-team by player name: " + player);

                boolean playerFound = false;

                for (Map.Entry<String, HashMap<String, HashMap<String, String>>> teams : masterTable.entrySet()) {
                    for (Map.Entry<String, HashMap<String, String>> players : teams.getValue().entrySet()) {
                        if (players.getKey().equalsIgnoreCase(player)) {
                            playerFound = true;
                            String teamName = teams.getKey();
                            for (Map.Entry<String, String> details : players.getValue().entrySet()) {
                                String playerName1 = players.getKey();
                                String playerName2 = details.getKey();
                                String playerNumber = details.getValue();
                                if (playerName1.equalsIgnoreCase(player)) {
                                    System.out.println("* | " + teamName + " | #" + playerNumber + " | " + playerName1 + "," + playerName2);
                                }
                            }
                        }
                    }
                }
                    if (!playerFound) {
                        System.out.println("Player '" + player + "' not found in the master table.");
                    }

                playerValidation = false;
            }
        }
        System.out.println("End of player search function.");
    }






    public static void listTeams (HashMap<String, HashMap<String, HashMap<String, String>>> masterTable) {
        System.out.println("Active teams in the database are:");

        if (masterTable.isEmpty()) {
            System.out.println("There are no active teams in the database.");
        } else{
            for (Map.Entry<String, HashMap<String, HashMap<String, String>>> entry : masterTable.entrySet()) {
                String teamName = entry.getKey();
                System.out.println("Team name = " + teamName);
                System.out.println();
            }
            System.out.println(" >  >  >  E N D  O F  T E A M S  L I S T  <  <  < ");
            System.out.println();
        }
    }







    public static void startSelection() {
        System.out.println("---------------------------------------------------------");
        System.out.println("   1 - Add player");
        System.out.println("   2 - Add team");
        System.out.println("   3 - Search player by last name in teams' rosters");
        System.out.println("   4 - List all teams");
        System.out.println("   Q - Exit");
        System.out.println();
    }











    // just populate HashMaps with some data to start and work with in the program
    public static void populateTeams(HashMap<String, HashMap<String, HashMap<String, String>>> masterTable,
                                     HashMap<String, HashMap<String, String>> teamRoster,
                                     HashMap<String, String> playerDetail) {

        // Boston Bruins
        playerDetail.put("Patrice", "37");
        teamRoster.put("Bergeron", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Brad", "63");
        teamRoster.put("Marchand", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("David", "88");
        teamRoster.put("Pastrnak", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Chad", "01");
        teamRoster.put("Smith", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Mike", "35");
        teamRoster.put("Richter", new HashMap<>(playerDetail));

        masterTable.put("Boston Bruins", new HashMap<>(teamRoster));

        teamRoster.clear();

        // Detroit Red Wings
        playerDetail.clear();
        playerDetail.put("Patrick", "88");
        teamRoster.put("Kane", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Alex", "12");
        teamRoster.put("DeBrincat", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Dylan", "71");
        teamRoster.put("Larkin", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Steve", "90");
        teamRoster.put("Richter", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Sidney", "87");
        teamRoster.put("Crosby", new HashMap<>(playerDetail));

        masterTable.put("Detroit Red Wings", new HashMap<>(teamRoster));

        teamRoster.clear();

        // Chicago Blackhawks
        playerDetail.clear();
        playerDetail.put("Taylor", "24");
        teamRoster.put("Hall", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Connor", "59");
        teamRoster.put("Bedard", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Nick", "17");
        teamRoster.put("Foligno", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Brandon", "13");
        teamRoster.put("Smith", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Sidney", "87");
        teamRoster.put("Crosby", new HashMap<>(playerDetail));

        masterTable.put("Chicago Blackhawks", new HashMap<>(teamRoster));

        teamRoster.clear();

        // Montreal Canadiens
        playerDetail.clear();
        playerDetail.put("Carey", "31");
        teamRoster.put("Price", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Brendan", "11");
        teamRoster.put("Gallagher", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Shea", "6");
        teamRoster.put("Weber", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Mark", "33");
        teamRoster.put("Smith", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Sidney", "87");
        teamRoster.put("Crosby", new HashMap<>(playerDetail));

        masterTable.put("Montreal Canadiens", new HashMap<>(teamRoster));

        teamRoster.clear();

        // Toronto Maple Leafs
        playerDetail.clear();
        playerDetail.put("Auston", "34");
        teamRoster.put("Matthews", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("John", "91");
        teamRoster.put("Tavares", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Mitch", "16");
        teamRoster.put("Marner", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("John", "10");
        teamRoster.put("Richter", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Sidney", "87");
        teamRoster.put("Crosby", new HashMap<>(playerDetail));

        masterTable.put("Toronto Maple Leafs", new HashMap<>(teamRoster));

        teamRoster.clear();

        // New York Rangers
        playerDetail.clear();
        playerDetail.put("Artemi", "10");
        teamRoster.put("Panarin", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Mika", "93");
        teamRoster.put("Zibanejad", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Adam", "23");
        teamRoster.put("Fox", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Mike", "35");
        teamRoster.put("Richter", new HashMap<>(playerDetail));

        playerDetail.clear();
        playerDetail.put("Sidney", "87");
        teamRoster.put("Crosby", new HashMap<>(playerDetail));

        masterTable.put("New York Rangers", new HashMap<>(teamRoster));

        teamRoster.clear();
    }
}