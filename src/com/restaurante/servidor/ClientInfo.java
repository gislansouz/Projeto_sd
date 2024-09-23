package com.restaurante.servidor;

import java.net.InetAddress;
import java.util.Objects;

public class ClientInfo {
    private final String requestId;
    private final String address;
    private final int port;

    public ClientInfo(String requestId, String address, int port) {
        this.requestId = requestId;
        this.address = address;
        this.port = port;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientInfo that = (ClientInfo) o;
        return port == that.port &&
                Objects.equals(requestId, that.requestId) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, address, port);
    }
}
