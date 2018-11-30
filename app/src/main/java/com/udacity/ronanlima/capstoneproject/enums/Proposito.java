package com.udacity.ronanlima.capstoneproject.enums;

import lombok.Getter;
import lombok.Setter;

public enum Proposito {
    ARQUITETURA("01", "Arquitetura"), DECORACAO("02", "Decoração");

    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String nome;

    Proposito(String id, String nome) {
        setId(id);
        setNome(nome);
    }

    /**
     * Return the proposito accordling id
     *
     * @param id
     * @return
     */
    public static Proposito getById(String id) {
        for (Proposito p : values()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

}
