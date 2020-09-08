package com.java.raocongyuan.backend.worker;

import com.java.raocongyuan.backend.DataManager;

public class Worker extends Thread {
    final protected DataManager manager;

    public Worker(DataManager manager) {
        super();
        this.manager = manager;
    }
}
