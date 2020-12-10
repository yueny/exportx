package com.whosly.infra.exportx.metadata.config;

public interface ServerConfigurable extends Configurable {
    /**
     * Close IO
     */
    void finish();

}
