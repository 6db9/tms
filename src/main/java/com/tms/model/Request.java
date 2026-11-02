package com.tms.model;

import java.time.LocalDateTime;

public class Request {
    private int id;
    private RequestType type;
    private RequestState state;
    private Client client;
    private LocalDateTime createdAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public RequestType getType() { return type; }
    public void setType(RequestType type) { this.type = type; }
    public RequestState getState() { return state; }
    public void setState(RequestState state) { this.state = state; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}