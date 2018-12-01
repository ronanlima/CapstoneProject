package com.udacity.ronanlima.capstoneproject.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by rlima on 27/11/18.
 */

@Entity(tableName = "project")
public class Project {
    @PrimaryKey
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String descricao;
    @Getter
    @Setter
    private String idProposito;
    @Getter
    @Setter
    private String nomeProjeto;
    @Getter
    @Setter
    private String imagemCapa;

    public Project(String id, String descricao, String idProposito, String nomeProjeto, String imagemCapa) {
        setId(id);
        setDescricao(descricao);
        setIdProposito(idProposito);
        setNomeProjeto(nomeProjeto);
        setImagemCapa(imagemCapa);
    }

    @Ignore
    public Project(String descricao, String idProposito, String nomeProjeto, String imagemCapa) {
        setDescricao(descricao);
        setIdProposito(idProposito);
        setNomeProjeto(nomeProjeto);
        setImagemCapa(imagemCapa);
    }

    //    @Ignore
//    private List<Image> images;
}
