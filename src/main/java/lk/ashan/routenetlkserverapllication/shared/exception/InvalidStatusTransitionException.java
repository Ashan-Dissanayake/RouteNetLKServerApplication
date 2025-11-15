package lk.ashan.routenetlkserverapllication.shared.exception;

public class InvalidStatusTransitionException extends RuntimeException{

    public InvalidStatusTransitionException(){}
    public InvalidStatusTransitionException(String message){super(message);}
}
