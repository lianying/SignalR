// Copyright (c) .NET Foundation. All rights reserved.
// Licensed under the Apache License, Version 2.0. See License.txt in the project root for license information.

package com.microsoft.aspnet.signalr;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

class MockTransport implements Transport {
    private static final String RECORD_SEPARATOR = "\u001e";

    private OnReceiveCallBack onReceiveCallBack;
    private ArrayList<String> sentMessages = new ArrayList<>();
    private boolean ignorePings;

    public MockTransport() {
        this(false);
    }

    public MockTransport(boolean ignorePings) {
        this.ignorePings = ignorePings;
    }

    @Override
    public CompletableFuture start() {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture send(String message) {
        if (!(ignorePings && message.equals("{\"type\":6}" + RECORD_SEPARATOR))) {
            sentMessages.add(message);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void setOnReceive(OnReceiveCallBack callback) {
        this.onReceiveCallBack = callback;
    }

    @Override
    public void onReceive(String message) throws Exception {
        this.onReceiveCallBack.invoke(message);
    }

    @Override
    public CompletableFuture stop() {
        return CompletableFuture.completedFuture(null);
    }

    public void receiveMessage(String message) throws Exception {
        this.onReceive(message);
    }

    public String[] getSentMessages() {
        return sentMessages.toArray(new String[sentMessages.size()]);
    }
}
