package com.jonas.UnlockIT.entity.locks;


import jakarta.persistence.*;

@Entity
@Table(name = "locks")
public class Lock{

    @Id
    @GeneratedValue
    private Long id;

    private String MCUIdentifier;
    private String ECBKey;

    public Lock(String MCUIdentifier, String ECBKey) {
        this.MCUIdentifier = MCUIdentifier;
        this.ECBKey = ECBKey;
    }

    public Lock() {
    }

    public String getMCUIdentifier() {
        return MCUIdentifier;
    }

    public void setMCUIdentifier(String MCUIdentifier) {
        this.MCUIdentifier = MCUIdentifier;
    }

    public String getECBKey() {
        return ECBKey;
    }

    public void setECBKey(String ECBKey) {
        this.ECBKey = ECBKey;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
