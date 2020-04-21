package com.example.beadworkhelper.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Work {
    private int _id;
    private String name;
    private String linkPicture;
    private String linkScheme;
    private String notes;
    private String state;
    private List<Material> materials;

    public Work() {
    }

    public Work(int _id, String name, String linkPicture, String linkScheme, String notes, String state) {
        this._id = _id;
        this.name = name;
        this.linkPicture = linkPicture;
        this.linkScheme = linkScheme;
        this.notes = notes;
        this.state = state;
        this.materials = new ArrayList<>();
    }

    public Work(int _id, String name, String linkPicture, String linkScheme, String notes,
                String state, List<Material> materials) {
        this(_id, name, linkPicture, linkScheme, notes, state);
        this.materials = materials;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkPicture() {
        return linkPicture;
    }

    public void setLinkPicture(String linkPicture) {
        this.linkPicture = linkPicture;
    }

    public String getLinkScheme() {
        return linkScheme;
    }

    public void setLinkScheme(String linkScheme) {
        this.linkScheme = linkScheme;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Work work = (Work) o;
        return _id == work._id &&
                Objects.equals(name, work.name) &&
                Objects.equals(linkPicture, work.linkPicture) &&
                Objects.equals(linkScheme, work.linkScheme) &&
                Objects.equals(notes, work.notes) &&
                Objects.equals(state, work.state) &&
                Objects.equals(materials, work.materials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, name, linkPicture, linkScheme, notes, state, materials);
    }

    @Override
    public String toString() {
        return "Work{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", linkPicture='" + linkPicture + '\'' +
                ", linkScheme='" + linkScheme + '\'' +
                ", notes='" + notes + '\'' +
                ", state='" + state + '\'' +
                ", materials=" + materials +
                '}';
    }
}
