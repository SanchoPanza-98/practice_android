package com.example.beadworkhelper.database;

import java.util.Objects;

public class Material {
    private int id;
    private String name;
    private String description;
    private int quantity;
    private int price;
    private int workId;

    public Material(int id, String name, String description, int quantity, int price, int workId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.workId = workId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return id == material.id &&
                quantity == material.quantity &&
                price == material.price &&
                workId == material.workId &&
                Objects.equals(name, material.name) &&
                Objects.equals(description, material.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, quantity, price, workId);
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", workId=" + workId +
                '}';
    }
}
