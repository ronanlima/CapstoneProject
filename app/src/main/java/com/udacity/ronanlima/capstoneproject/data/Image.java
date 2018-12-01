package com.udacity.ronanlima.capstoneproject.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "image", foreignKeys = @ForeignKey(entity = Project.class,
        parentColumns = "id",
        childColumns = "idProjeto",
        onDelete = CASCADE))
public class Image {
    @PrimaryKey
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String descricao;
    @Getter
    @Setter
    private String urlImagem;
    @Getter
    @Setter
    private String idProjeto;

    public Image(String id, String descricao, String urlImagem, String idProjeto) {
        setId(id);
        setDescricao(descricao);
        setUrlImagem(urlImagem);
        setIdProjeto(idProjeto);
    }

    @Ignore
    public Image(String descricao, String urlImagem, String idProjeto) {
        setDescricao(descricao);
        setUrlImagem(urlImagem);
        setIdProjeto(idProjeto);
    }
}
