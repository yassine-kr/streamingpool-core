/**
 * Copyright (c) 2016 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.streaming.pool.core.testing.subscriber;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reactive Streams {@link Subscriber} for testing purposes. It keeps track of every value that is received and exposes
 * them as {@link List}. If enabled, it will log every reactive streams interaction. It is possible to set a consuming
 * delay for creating slow and fast subscribers. A consuming delay is the time elapsed between the
 * {@link Subscriber#onNext(Object)} call and the {@link Subscription#request(long)} call.
 * 
 * @author acalia
 * @param <T> type of the data that the subscriber will receive from the stream 
 */
public class TestSubscriber<T> implements Subscriber<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestSubscriber.class);

    private final List<T> values;
    private final String name;
    private final long consumingDurationMs;
    private Subscription subscription;
    private boolean verbose;

    private int requestedItems;

    public TestSubscriber(String name, long consumingDurationMs) {
        this.name = name;
        this.consumingDurationMs = consumingDurationMs;
        this.values = new LinkedList<>();
        this.verbose = false;
        this.requestedItems = 1;
    }

    public TestSubscriber(String name, long consumingDurationMs, boolean verbose) {
        this.name = name;
        this.consumingDurationMs = consumingDurationMs;
        this.values = new LinkedList<>();
        this.verbose = verbose;
        this.requestedItems = 1;
    }

    @Override
    public void onSubscribe(Subscription newSubscription) {
        log("[{}] onSubscribe", name);
        this.subscription = newSubscription;
        newSubscription.request(requestedItems);
    }

    @Override
    public void onNext(T value) {
        log("[{}] onNext: {}", name, value);
        values.add(value);
        sleep();
        subscription.request(requestedItems);
    }

    @Override
    public void onError(Throwable error) {
        log("[{}] onError", name, error);
        subscription.cancel();
    }

    @Override
    public void onComplete() {
        log("[{}] onComplete", name);
    }

    public void setRequestedItems(int requestedItems) {
        this.requestedItems = requestedItems;
    }

    private void log(String format, Object... args) {
        if (verbose) {
            LOGGER.info(format, args);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(consumingDurationMs);
        } catch (InterruptedException e) {
            LOGGER.error("Error while simulating expensive operation using Thread.sleep", e);
        }
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public List<T> getValues() {
        return new ArrayList<>(values);
    }

    public String getName() {
        return name;
    }

    public long getConsumingDurationMs() {
        return consumingDurationMs;
    }

    public boolean isVerbose() {
        return this.verbose;
    }
}
