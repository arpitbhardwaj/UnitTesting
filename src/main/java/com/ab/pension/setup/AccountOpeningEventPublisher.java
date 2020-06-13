package com.ab.pension.setup;

/**
 * @author Arpit Bhardwaj
 */
public interface AccountOpeningEventPublisher {
    void notify(String accountId);
}
