package org.n3r.idworker;

/**
 * @author mqxu
 */
public class InvalidSystemClock extends RuntimeException {
    public InvalidSystemClock(String message) {
        super(message);
    }
}
