package com.tayek.util.concurrent;

import java.io.IOException;

public interface Stopable extends Runnable {
    void stop() throws IOException,InterruptedException;
    default boolean isStopping() { return false; }
    boolean setIsStopping();
}
