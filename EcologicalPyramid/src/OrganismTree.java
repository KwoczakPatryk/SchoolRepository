//Patryk Kwoczak, 111706152, pkwoczak, R01


public class OrganismTree {
    private OrganismNode cursor;
    private OrganismNode root;
    private static Boolean isFound;

    /**
     *  Constructor that creates a new OrganismTree with apexPredator as the root.
     *  Condition: apexPredator must be an animal
     *  Postcondition: An OrganismTree object is made, with apexPredator representing the apex predator.
     *      Both root and cursor reference this node.
     * @param apexPredator
     */
    public OrganismTree ( OrganismNode apexPredator) throws IsPlantException
    {
        if(apexPredator.getIsPlant()) throw new IsPlantException("The root cannot be a plant.");
        this.root = apexPredator;
        this.cursor = apexPredator;
    }

    //Boolean for if tree is completely empty
    public Boolean isEmpty(){
        return (this.root == null);
    }

    /**
     * Getters/setters for tree
     * @return
     */
    public OrganismNode getCursor(){
        return this.cursor;
    }

    public OrganismNode getRoot(){
        return this.root;
    }

    /**
     * Cursor reset to the root
     */
    public void cursorReset(){
        this.cursor = this.root;
    }

    /**
     * Move cursor to one of children
     * Precondition: child name must be valid for one of them
     * @param name is name of child
     * @throws IllegalArgumentException
     * @throws NullPointerException if no child exists
     */
    public void  moveCursor (String name)  throws IllegalArgumentException, NullPointerException
    {
        if(cursor.getLeft()== null && cursor.getMiddle()== null && cursor.getRight() == null)
            throw new NullPointerException("No valid entry");
        else if (cursor.getLeft().getName().equals(name)) this.cursor = cursor.getLeft();
        else if (cursor.getMiddle().getName().equals(name)) this.cursor = cursor.getMiddle();
        else if (cursor.getRight().getName().equals(name)) this.cursor = cursor.getRight();
        else throw new IllegalArgumentException("Name not valid");

    }

    /**
     * @return a String including the organism at cursor and all its possible prey
     * info records the string information
     * @throws IsPlantException when there is no corresponding prey, because it is a plant.
     * cursor does not move
     */
    public String listPrey() throws IsPlantException
    {
        if (cursor.getIsPlant()) throw new IsPlantException("No prey because it is a plant.");
        String info = "";
        info+= cursor.getName() + " -> ";
        if(this.cursor.getLeft()!= null) info+= cursor.getLeft().getName();
        if(this.cursor.getMiddle()!=null)info+=", "+cursor.getMiddle().getName();
        if(this.cursor.getRight()!=null)info+=", "+cursor.getRight().getName();

        return info;


    }

    /**
     * lists all plants supporting the cursor
     * @param myNode is cursor.
     * @return string of all data.
     */
     public String listAllPlants(OrganismNode myNode){
        String s = "";

        s = listAllPlants(myNode, s);

        return s;

     }

    /**
     * Helper method to search for plants recursively
     * @param myNode is current node in the recursion, equal to or under the cursor.
     * @param myPlants is string of all plants obtained thus far.
     * @return string of all plant data
     */
     private String listAllPlants(OrganismNode myNode, String myPlants){
         //base case:
         if (myNode.getIsPlant()){
             if(myPlants.equals("")) myPlants += myNode.getName();
             else{
                 myPlants+=", "+myNode.getName();
             }
             return myPlants;
         }
         if(myNode.isEmpty()){
             return myPlants;
         }

         if(myNode.getLeft()!=null){
             myPlants = listAllPlants(myNode.getLeft(),myPlants);
         }

         if(myNode.getMiddle()!=null){
             myPlants = listAllPlants(myNode.getMiddle(),myPlants);

         }

         if(myNode.getRight()!=null){
             myPlants = listAllPlants(myNode.getRight(),myPlants);
         }

        return myPlants;
     }


    /**
     * Go from head and search through all children until you hit the cursor
     * @param myNode is original cursor.
     * @return string of all the necessary children to get from head to cursor.
     */
    public String listFoodChain(OrganismNode myNode){
        String myChain = "";
        this.isFound = false;
        myChain = listFoodChain(this.getRoot(), myChain, myNode.getName());

        String[] myBrokenChain = myChain.split("!x!");
        String newChain = "";
        for(int i = myBrokenChain.length-1; i >= 0; i--){
            if (i==0) newChain += myBrokenChain[i];
            else      newChain += myBrokenChain[i] + " -> ";
        }
        return newChain;
    }

    /**
     * Go from head and search through all children until you hit the cursor
     * when cursor is found, Boolean isFound is updated to stop going through more nodes and only return to root.
     * @param myNode is current node in recursion. check if name matches nameWanted
     * @param myChain is string of all node names starting from the string wanted up until the apex. only gets added
     *              on when the cursor is found.
     * @param nameWanted is name of cursor which we are looking for.
     * @return string of mychain
     */
    private String listFoodChain(OrganismNode myNode, String myChain, String nameWanted){
       //base case
        if(myNode.getName().equals(nameWanted)){
            isFound = true; //assigns the Boolean so the recursion won't continue down, only go back up.
            if(myNode.isEmpty()) myChain+=myNode.getName()+"!x!"; //adds the first case
        }

        if(myNode.isEmpty()) return myChain;


        if(!isFound){
            if(myNode.getLeft()!=null && !isFound){
                myChain = listFoodChain(myNode.getLeft(),myChain,nameWanted);
            }

            if(myNode.getMiddle()!=null && !isFound){
                myChain = listFoodChain(myNode.getMiddle(),myChain,nameWanted);

            }

            if(myNode.getRight()!=null && !isFound){
                myChain = listFoodChain(myNode.getRight(),myChain,nameWanted);
            }

        }
        if(isFound){
            myChain+=myNode.getName()+"!x!";
            return myChain;
        }


        return myChain;
    }



    /**
     * print out all organisms in tree beginning at cursor.
     * @param myNode is original cursor.
     */
    public void printOrganismTree(OrganismNode myNode){
        int myTabCount = 0;
        printOrganismTree(myNode, myTabCount);
    }

    /**
     * Goes through all children recursively and prints every node, keeping count of tabs for indents
     * @param myNode current node, prints it as |- if animal, and -- if plant.
     * @param tabCount +1 per level of tree
     */
    private void printOrganismTree(OrganismNode myNode, int tabCount){
        String tabs = "";
        //what we want to print
        for(int i = 0; i < tabCount; i++) tabs+= "\t";
        if(myNode.getIsPlant()){
            System.out.println(tabs+"--"+myNode.getName());

        }
        else{
            System.out.println(tabs+"|-"+myNode.getName());
        }

        //examine for base case
        if(myNode.isEmpty()){
            return;
        }

        //go through more iterations
        if(myNode.getLeft()!=null){
            printOrganismTree(myNode.getLeft(),tabCount+1);
        }

        if(myNode.getMiddle()!=null){
            printOrganismTree(myNode.getMiddle(),tabCount+1);

        }

        if(myNode.getRight()!=null){
            printOrganismTree(myNode.getRight(),tabCount+1);
        }

    }

    /**
     *
     * @param name specific name of child
     * @param isHerbivore animal specifications of diet
     * @param isCarnivore
     * @throws IllegalArgumentException if the name is already in use in this parent node.
     * @throws PositionNotAvailableException if no space in parent for new child
     * Postcondition: Either an exception is thrown,
     *      or a new animal node named name is added as a child of the cursor with a specific diet.
     *      Cursor doesn't move.
     */
    public void addAnimalChild( String name, Boolean isCarnivore, Boolean isHerbivore) throws IllegalArgumentException,
            PositionNotAvailableException {

        //check if this is the first entry
        if (root == null) {
            this.root = new OrganismNode(name, false, isCarnivore, isHerbivore);

        } else {
            incrementNum(this.cursor);
            //make sure none of children share the same name.
            if (cursor.getLeft() != null && cursor.getLeft().getName().equals(name))
                throw new IllegalArgumentException();
            if (cursor.getMiddle() != null && cursor.getMiddle().getName().equals(name))
                throw new IllegalArgumentException();
            if (cursor.getRight() != null && cursor.getRight().getName().equals(name))
                throw new IllegalArgumentException();

            try {
                this.cursor.addPrey(new OrganismNode(name, false, isCarnivore, isHerbivore));
            } catch (IsPlantException plantExc) {
                //do not add
            } catch (DietMismatchException dietExc) {
                //invalid due to diet, so do Not add
                System.out.println("Diet does not match animal.");
            }


        }
    }

    /**
     *
     * @param name specific name of child
     * @throws IllegalArgumentException
     * @throws PositionNotAvailableException
     *  Postcondition: Either an exception is thrown,
     *      *      or a new plant node named name is added as a child of the cursor with a specific diet.
     *      *      Cursor doesn't move.
     */
        public void addPlantChild( String name) throws IllegalArgumentException,
            PositionNotAvailableException
        {

            //check if this is the first entry
            if(root == null){
                this.root = new OrganismNode(name, true, false, false);

            } else {
                incrementNum(this.cursor);
                //make sure none of children share the same name.
                if(cursor.getLeft() != null && cursor.getLeft().getName().equals(name)) throw new IllegalArgumentException();
                if(cursor.getMiddle() != null && cursor.getMiddle().getName().equals(name)) throw new IllegalArgumentException();
                if(cursor.getRight() != null && cursor.getRight().getName().equals(name)) throw new IllegalArgumentException();

                try {
                    this.cursor.addPrey(new OrganismNode(name, true, false,false));
                    System.out.println("A(n) "+ name +" has successfully been added as prey for the "+cursor.getName() + "!");

                }catch(IsPlantException plantExc){
                    //do not add
                }
                catch(DietMismatchException dietExc){
                    //invalid due to diet, so do Not add
                    System.out.println("Error: Diet does not match animal.");
                }

            }

        }

    /**
     * remove child from tree.
     * @param name is name user inputs to remove.
     * @throws IllegalArgumentException if the name is not found
     */
    public void removeChild(String name) throws IllegalArgumentException
        {

            if(this.cursor.getLeft()!=null && this.cursor.getLeft().getName().equals(name)){ //left is removed, and check other children and move if needed
                this.cursor.setLeft(null);

                if(this.cursor.getMiddle()!=null) {
                    this.cursor.setLeft(this.cursor.getMiddle());

                    this.cursor.setMiddle(null);
                }

                if(this.cursor.getRight()!=null) {
                    this.cursor.setMiddle(this.cursor.getRight());
                    this.cursor.setRight(null);
                }
                decrementNum(this.cursor);
                return;
            }
            if(this.cursor.getMiddle()!=null && this.cursor.getMiddle().getName().equals(name)){

                this.cursor.setMiddle(null); //middle is removed and right is replaced if it exists
                if(this.cursor.getRight()!=null) {
                    this.cursor.setMiddle(this.cursor.getRight());
                    this.cursor.setRight(null);
                }
                decrementNum(this.cursor);
                return;
            }
            if(this.cursor.getRight()!=null && this.cursor.getRight().getName().equals(name)){
                System.out.println("Test 6");
                this.cursor.setRight(null); //eliminate right
                decrementNum(this.cursor);
                return;
            }


        }


    /**
     * Increment if child is added
     * @param myNode is cursor which is adding children
     */
    private void incrementNum(OrganismNode myNode){
        myNode.setSubNum(myNode.getSubNum() + 1);
    }

    /**
     * decrement if child is removed
     * @param myNode is cursor which is removing children
     */
    private void decrementNum(OrganismNode myNode){
        myNode.setSubNum(myNode.getSubNum() - 1);

    }



}
