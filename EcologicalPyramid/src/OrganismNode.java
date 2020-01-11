//Patryk Kwoczak, 111706152, pkwoczak, R01

public class OrganismNode {
    private String name;
    private Boolean isPlant;
    private Boolean isCarnivore;
    private Boolean isHerbivore;
    private OrganismNode left = null;
    private OrganismNode middle = null;
    private OrganismNode right = null;
    private int subNum; //size of this subtree

    //constructor
    public OrganismNode(){
        subNum = 0;
    }

    //constructor
    public OrganismNode(String initName, Boolean initIsPlant, Boolean initIsCarnivore, Boolean initIsHerbivore){
        this.name = initName;
        this.isPlant = initIsPlant;
        this.isCarnivore = initIsCarnivore;
        this.isHerbivore = initIsHerbivore;

        subNum = 0;
    }

    /**
     * SETTERS & GETTERS
     * @return
     */
    public String getName(){
        return this.name;
    }

    public Boolean getIsPlant(){
        return this.isPlant;
    }

    public Boolean getIsCarnivore(){
        return  this.isCarnivore;
    }

    public Boolean getIsHerbivore(){
        return this.isHerbivore;
    }

    public OrganismNode getLeft(){
        return this.left;
    }

    public OrganismNode getMiddle(){
        return this.middle;
    }

    public int getSubNum(){
        return this.subNum;
    }

    public OrganismNode getRight(){
        return this.right;
    }

    public void setName(String initName){
        this.name = initName;
    }

    public void setIsPlant(Boolean isIt){
        this.isPlant = isIt;
    }

    public void setIsCarnivore(Boolean isIt){
        this.isCarnivore = isIt;
    }

    public void setIsHerbivore(Boolean isIt){
        this.isHerbivore = isIt;
    }

    public void setSubNum(int newNum){
        this.subNum = newNum;
    }

    public void setLeft(OrganismNode newLeft){
        this.left = newLeft;
    }

    public void setMiddle(OrganismNode newMid){
        this.middle = newMid;
    }

    public void setRight(OrganismNode newRight){
        this.right = newRight;
    }


    /**
     * preconditions:    This node is not a plant.
     * §                At least one of the three child node positions of this node is available.
     * §                The type of prey correctly corresponds to the diet of this node.
     * postcondition: Either an exception is thrown, or preyNode is added as a child of this node.
     * @param preyNode new Node coming in
     * @throws PositionNotAvailableException if all child nodes are full.
     * @throws IsPlantException if parent is a plant, it cannot have any prey
     * @throws DietMismatchException if a parent cannot consume child.
     */
    public void addPrey(OrganismNode preyNode) throws
            PositionNotAvailableException, IsPlantException, DietMismatchException
    {
        //Check: if thisNode is a Plant, it cannot prey on anything.
        if(this.isPlant) throw new IsPlantException("A plant cannot have children");

        //If child is a plant, animal must be able to eat plants.
        if(!this.isHerbivore && preyNode.getIsPlant()) throw new DietMismatchException("Animal cannot eat plants!");

        //if child is an animal, this node must be able to eat animals.
        if(!this.isCarnivore && !preyNode.getIsPlant()) throw new DietMismatchException("Animal cannot eat other animals!");

        //see if there is space left in this node
        if(this.getLeft() == null) this.setLeft(preyNode);
        else if (this.getMiddle()==null) this.setMiddle(preyNode);
        else if (this.getRight() == null) this.setRight(preyNode);
        else throw new PositionNotAvailableException("No more space in this node");

    }

    /**
     * Looks at cursor to inform if the cursor is full of child nodes.
     * @return boolean true if entirely full.
     */
    public Boolean isFull(){
        if(this.getLeft()== null) return false;
        else if(this.getMiddle() == null) return false;
        else if(this.getRight() == null) return  false;
        return true;
    }

    /**
     *Looks at cursor to inform if the cursor is empty of child nodes.
     * @returnboolean true if entirely empty.
     */
    public Boolean isEmpty(){
        if(this.getLeft()!= null) return false;
        else if(this.getMiddle() != null) return false;
        else if(this.getRight() != null) return  false;
        return true;
    }


}
