//Patryk Kwoczak, 111706152, pkwoczak, R01

import java.util.Scanner;

public class FoodPyramid {
    public static OrganismTree myTree;


    public static void main(String args[]) {

        System.out.println("Hello Biologist!");
        System.out.println("What is the name of your Apex Predator?");
        Scanner input = new Scanner(System.in);

        String apexName = input.nextLine();

        Boolean flag = true;
        Boolean apexIsHerbivore = false;
        Boolean apexIsCarnivore = false;

        //returns [isCarnivore, isHerbivore]
        Boolean[] dietInfo = getDietInfo();
        apexIsCarnivore = dietInfo[0];
        apexIsHerbivore = dietInfo[1];

        System.out.println("Constructing food pyramid. . .");
        OrganismNode apexRoot = new OrganismNode(apexName, false, apexIsCarnivore, apexIsHerbivore);

        try {
            myTree = new OrganismTree(apexRoot);
        } catch (IsPlantException prevented) {
        }//will not happen here.

        try {
            while (true) { //offer menu to user
                System.out.println("\nMenu:" +
                        "\n" +
                        "(PC) - Create New Plant Child\n" +
                        "(AC) - Create New Animal Child\n" +
                        "(RC) - Remove Child\n" +
                        "(P) - Print Out Cursor’s Prey\n" +
                        "(C) - Print Out Food Chain\n" +
                        "(F) - Print Out Food Pyramid at Cursor\n" +
                        "(LP) - List All Plants Supporting Cursor\n" +
                        "(R) - Reset Cursor to Root\n" +
                        "(M) - Move Cursor to Child\n" +
                        "(Q) - Quit");

                String option = input.nextLine();
                switch (option) {
                    case "PC"://create a new Plant Child
                        if (myTree.getCursor().isFull()) {
                            System.out.println("ERROR: There is no room for any more prey for this animal.");
                            break;
                        }
                        if (myTree.getCursor().getIsPlant()) {
                            System.out.println("Error: A plant cannot have any plant prey.");
                            break;
                        }
                        if (!myTree.getCursor().getIsHerbivore()) {
                            System.out.println("Error: Animal cannot eat plants");
                            break;
                        }
                        System.out.println("What is the name of your plant?");
                        String plantName = input.nextLine();
                        try {
                            myTree.addPlantChild(plantName);
                        } catch (PositionNotAvailableException exc) {
                            System.out.println("ERROR: There is no room for any more prey for this predator");

                        } catch (IllegalArgumentException wrongArg) {
                            System.out.println("Error: That name is already taken.");
                        }

                        break;

                    case "AC"://create a new Animal Child
                        if (myTree.getCursor().getIsPlant()) {
                            System.out.println("Error: Plant cannot eat prey");
                            break;
                        }
                        if (!myTree.getCursor().getIsCarnivore()) {
                            System.out.println("Error: Animal cannot eat other animals");
                            break;
                        }

                        if (myTree.getCursor().isFull()) {
                            System.out.println("ERROR: There is no room for anymore prey for this predator.");
                            break;
                        }

                        System.out.println("What is the name of your organism?");
                        String animalName = input.nextLine();
                        Boolean[] animalDiet = getDietInfo();
                        OrganismNode newChild = new OrganismNode(animalName, false, animalDiet[0], animalDiet[1]);

                        try {
                            myTree.addAnimalChild(animalName, animalDiet[0], animalDiet[1]);
                            System.out.println(animalName + " has been succesfully added as prey for " + myTree.getCursor().getName());
                        } catch (PositionNotAvailableException posUn) {
                            System.out.println("ERROR: There is no more room for more prey for this predator.\n");

                        } catch (IllegalArgumentException illexc) {
                            System.out.println("ERROR: Cannot have another prey with the same name.\n");
                        }


                        break;

                    case "RC"://remove a child
                        if (myTree.getCursor().getIsPlant()) {
                            System.out.println("Error: Plant cannot have Children");
                            break;
                        }

                        if (myTree.getCursor().isEmpty()) {
                            System.out.println("Error: No prey to remove.");
                            break;
                        }
                        System.out.println("What prey would you like to remove?");
                        String removeName = input.nextLine();
                        try {
                            myTree.removeChild(removeName);
                        } catch (IllegalArgumentException illexc) {
                            System.out.println("Error: The prey was not found.");
                        }
                        break;

                    case "P"://print out cursor's prey
                        if (myTree.getCursor().getIsPlant()) {
                            System.out.println("Error: a plant canny have any prey.");
                            break;
                        }
                        try {
                            System.out.println(myTree.listPrey());
                        } catch (IsPlantException plantErr) {
                            System.out.println("Error: A plant cannot have prey.");
                        } catch (IllegalArgumentException illExc) {
                        } catch (NullPointerException nullexc) {
                        }

                        break;

                    case "C"://Print out the food chain.
                        System.out.println(myTree.listFoodChain(myTree.getCursor()));
                        break;

                    case "F"://Print out the food pyramid at the cursor"
                        try {
                            myTree.printOrganismTree(myTree.getCursor());
                        } catch (Exception exc) {
                            System.out.println("Something went wrong");
                        } //has never happened in testing.
                        break;

                    case "LP"://List all the plants supporting cursor
                        try {
                            if (myTree.listAllPlants(myTree.getCursor()).equals(""))
                                System.out.println("No plants found under " + myTree.getCursor().getName());

                            else System.out.println(myTree.listAllPlants(myTree.getCursor()) + ".");
                        } catch (Exception exc) {
                            System.out.println("Something went awfully wrong"); //Should NEVER happen
                        }

                        break;

                    case "R"://reset cursor to the root

                        myTree.cursorReset();
                        System.out.println("Cursor successfully reset to root!");
                        break;

                    case "M"://move the cursor to a child node
                        if (myTree.getCursor().getIsPlant()) {
                            System.out.println("Error: Plant cannot have Children");
                            break;
                        }
                        if (myTree.getCursor().isEmpty()) {
                            System.out.println("Error: Animal has no prey yet.");
                            break;
                        }
                        System.out.println("Move to? ");
                        String destinationName = input.nextLine();

                        try {
                            myTree.moveCursor(destinationName);
                            System.out.println("Cursor successfully moved to " + myTree.getCursor().getName() + "!");
                        } catch (IllegalArgumentException illexc) {
                            System.out.println("Error: No prey found with the name " + destinationName);
                        } catch (NullPointerException nullexc) {
                            System.out.println("Error: No prey found with the name " + destinationName);
                        }
                        break;

                    case "Q"://quit the program
                        System.out.println("Thank you for using my program");
                        System.exit(0);

                    default:
                        System.out.println("Please put a valid choice");


                }


            }


        }catch(Exception exc){//final line of defense.
            System.out.println("Something went terribly wrong.");
    }

    }
    /**
     *get info for comparison to be used in adding children
     * Loops until user puts in valid info
     * @return [isCarnivore, isHerbivore];
     */
    private static Boolean[] getDietInfo() {
        Scanner userInput = new Scanner(System.in);
        Boolean flag = true;
        Boolean isHerbivore = false;
        Boolean isCarnivore = false;
        while (flag) {
            System.out.print("\nIs the organism an herbivore / a carnivore / an omnivore? (H / C / O): ");
            String choice = userInput.nextLine();

            if (choice.equals("H")) {
                isHerbivore = true;
                flag = false;
            } else if (choice.equals("C")) {
                isCarnivore = true;
                flag = false;
            } else if (choice.equals("O")) {
                isCarnivore = true;
                isHerbivore = true;
                flag = false;
            }
            if(flag) System.out.println("Please give a valid response.");
        }

        Boolean[] myReply = new Boolean[2];
        myReply[0] = isCarnivore;
        myReply[1] = isHerbivore;

        return myReply;


    }
}
