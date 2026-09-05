//package revauc;
package mstc;

public class REVMailException extends Exception
{

    private String message;

    public REVMailException(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
