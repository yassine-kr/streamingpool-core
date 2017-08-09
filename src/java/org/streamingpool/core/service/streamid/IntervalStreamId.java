/**
 * Copyright (c) 2017 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package org.streamingpool.core.service.streamid;

import java.util.concurrent.TimeUnit;

public class IntervalStreamId {
    private final TimeUnit timeUnit;
    private final long period;
    private final long initialDelay;

    public IntervalStreamId(TimeUnit timeUnit, long period) {
        this(timeUnit, period, 0);
    }

    public IntervalStreamId(TimeUnit timeUnit, long period, long initialDelay) {
        this.timeUnit = timeUnit;
        this.period = period;
        this.initialDelay = initialDelay;
    }

    public TimeUnit getUnit() {
        return timeUnit;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public long getPeriod() {
        return period;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (initialDelay ^ (initialDelay >>> 32));
        result = prime * result + (int) (period ^ (period >>> 32));
        result = prime * result + ((timeUnit == null) ? 0 : timeUnit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IntervalStreamId other = (IntervalStreamId) obj;
        if (initialDelay != other.initialDelay)
            return false;
        if (period != other.period)
            return false;
        if (timeUnit != other.timeUnit)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "IntervalStreamId [timeUnit=" + timeUnit + ", period=" + period + ", initialDelay=" + initialDelay + "]";
    }

}
