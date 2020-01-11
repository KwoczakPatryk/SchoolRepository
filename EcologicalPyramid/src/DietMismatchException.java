//Patryk Kwoczak, 111706152, pkwoczak, R01

/**
 * Exception for when an animal's diet does not match the child node being assigned.
 */
public class DietMismatchException extends Exception{
    public DietMismatchException(String message){
        super(message);
    }
}
