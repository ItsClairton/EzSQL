package com.gitlab.pauloo27.core.sql;

/**
 * Class that will handle with exceptions.
 *
 * @author Willian Gois / zMathi
 * @version 1.0
 * @since 0.4.0
 */
@FunctionalInterface
public interface ExceptionHandler {

    /**
     * Handles an exception.
     *
     * @param e The exception.
     */
    void onException(Exception e);
}
