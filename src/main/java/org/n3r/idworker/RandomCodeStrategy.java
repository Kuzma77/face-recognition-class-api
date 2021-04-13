package org.n3r.idworker;

/**
 * @author mqxu
 */
public interface RandomCodeStrategy {
    void init();

    int prefix();

    int next();

    void release();
}
