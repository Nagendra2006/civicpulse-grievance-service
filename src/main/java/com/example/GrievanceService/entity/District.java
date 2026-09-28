package com.example.GrievanceService.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "districts")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @OneToMany(mappedBy = "district")
    private List<Mandal> mandals;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Mandal> getMandals() {
        return mandals;
    }

    public void setMandals(List<Mandal> mandals) {
        this.mandals = mandals;
    }

    // getters & setters
}