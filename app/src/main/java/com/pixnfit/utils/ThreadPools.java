package com.pixnfit.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by fabier on 28/02/16.
 */
public class ThreadPools {
    public static final Executor IMAGELOAD_THREADPOOL = Executors.newFixedThreadPool(4);
    public static final Executor METADATA_THREADPOOL = Executors.newFixedThreadPool(4);
    public static final Executor POST_THREADPOOL = Executors.newFixedThreadPool(4);
}
