package com.udacity.ronanlima.capstoneproject.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.Data;

/**
 * Created by rlima on 27/11/18.
 */

@Entity(tableName = "project")
@Data
public class Project {
    @PrimaryKey
    @NonNull
    private String id;
    private String descricao;
    private String idProposito;
    private String nomeProjeto;
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

    public Project() {
    }

    //    @Ignore
//    private List<Image> images;
}
