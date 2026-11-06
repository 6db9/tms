package com.tms.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Request {
    private int id;
    private RequestType type;
    private RequestState state;
    private Client client;
    private LocalDateTime createdAt;

    private String loadDescription;
    private Integer quantity;
    private BigDecimal weightPerItem;

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

    public String getLoadDescription() { return loadDescription; }

    public void setLoadDescription(String loadDescription) { this.loadDescription = loadDescription; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getWeightPerItem() { return weightPerItem; }
    public void setWeightPerItem(BigDecimal weightPerItem) { this.weightPerItem = weightPerItem; }

    // Calculation: 100 * 20 = 2000. If unknown, return null
    public BigDecimal getTotalWeight() {
        if (quantity == null || weightPerItem == null) {
            return null;
        }
        return weightPerItem.multiply(new BigDecimal(quantity));
    }
}