package exceptionPackage;

public class WrongArgumentException extends IllegalArgumentException{
    public WrongArgumentException(String message){
        super(message);
    }
}
