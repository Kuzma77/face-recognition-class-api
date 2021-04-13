package org.n3r.idworker;

/**
 * @author mqxu
 */
public interface WorkerIdStrategy {
    void initialize();

    long availableWorkerId();

    void release();
}
