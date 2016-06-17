/**
 * Copyright (c) 2016 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.streaming.pool.core.testing.subscriber;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.reactivestreams.Subscriber;

/**
 * This subscriber extends {@link TestSubscriber} and provides the ability to await the termination of the stream. A
 * stream is considered terminated when either {@link Subscriber#onComplete()} or {@link Subscriber#onError(Throwable)}
 * is called.
 * 
 * @author acalia
 * @param <T> type of the data that the subscriber will receive from the stream
 */
public class BlockingTestSubscriber<T> extends TestSubscriber<T> {

    private final CountDownLatch sync = new CountDownLatch(1);

    private BlockingTestSubscriber(String name, long consumingDurationMs) {
        super(name, consumingDurationMs);
    }

    private BlockingTestSubscriber(String name, long consumingDurationMs, boolean verbose) {
        super(name, consumingDurationMs, verbose);
    }

    public static final <T1> BlockingTestSubscriber<T1> ofName(String name) {
        return new BlockingTestSubscriber<>(name, 0);
    }

    public <T1> BlockingTestSubscriber<T1> withConsumingdelayInMs(long consumingDelayInMs) {
        return new BlockingTestSubscriber<>(this.getName(), consumingDelayInMs, this.isVerbose());
    }

    public <T1> BlockingTestSubscriber<T1> verbose() {
        return new BlockingTestSubscriber<>(this.getName(), this.getConsumingDurationMs(), true);
    }

    public <T1> BlockingTestSubscriber<T1> nonverbose() {
        return new BlockingTestSubscriber<>(this.getName(), this.getConsumingDurationMs(), false);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        sync.countDown();
    }

    @Override
    public void onError(Throwable error) {
        super.onError(error);
        sync.countDown();
    }

    public void await() {
        try {
            sync.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void await(long timeout, TimeUnit timeUnit) {
        try {
            sync.await(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}