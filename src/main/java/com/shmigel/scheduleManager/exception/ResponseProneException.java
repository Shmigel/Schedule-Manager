package com.shmigel.scheduleManager.exception;

/**
 * Since we are working with kind of stateless machine,
 * which can't handle anything but established json with some data in it
 * we should be ready to and prepare response of any exception,
 * in order to our users wouldn't suddenly get kicked out of dialog, without any hint why.
 * Every project exception should be able to generate a response from,
 * for now it's only responses base on exception message.
 * In order to prevent me from using Java 8+ feature like method reference ex. SomeException::new.
 * I create that class, so i always should give user some message when creating exception,
 * and create handler which supports only this class and his children,
 * while others exceptions should be wrapped into one of with response support
 */
public class ResponseProneException extends RuntimeException {

    public ResponseProneException(String message) {
        super(message);
    }
}
