package fr.eni.javaee.encheres;

import fr.eni.javaee.encheres.messages.PropertiesReader;

public class EException extends Exception{
    private int code;

    // CONSTRUCTORS

    public EException() { super(); }

    public EException(String message) { super(message); }

    public EException(String message, Exception exception) { super(message, exception); }

    public EException(int code) {
        super(decode(code));
        setCode(code);
    }

    public EException(int code, Exception exception) {
        super(decode(code), exception);
        setCode(code);
    }


    // METHODS

    /**
     * Get an error message from its code.
     * @param code int | Error code.
     * @return String | Error message.
     */
    private static String decode(int code) { return PropertiesReader.getProperty(code); }

    @Override
    public String getMessage() {
        return "Enchères | " + super.getMessage();
    }

    @Override
    public void printStackTrace() {
        System.out.println(getMessage());
        super.printStackTrace();
    }

    // GETTERS & SETTERS

    public void setCode(int code) { this.code = code; }

    public int getCode() { return this.code; }
}
