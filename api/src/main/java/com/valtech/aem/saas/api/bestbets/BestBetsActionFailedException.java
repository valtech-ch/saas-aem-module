package com.valtech.aem.saas.api.bestbets;

/**
 * A custom exception which is thrown when a best bet action fails.
 */
public class BestBetsActionFailedException extends RuntimeException {

    public BestBetsActionFailedException(String message) {
        super(message);
    }

    public BestBetsActionFailedException(
            String message,
            Throwable cause) {
        super(message, cause);
    }
}
