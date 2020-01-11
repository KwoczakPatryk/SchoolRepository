//Patryk Kwoczak, 111706152, pkwoczak, R01

/**
 * Happens when a parent node is tasked with having another child when it already has 3.
 */
public class PositionNotAvailableException extends Exception {
    public PositionNotAvailableException(String message){
        super(message);
    }
}
