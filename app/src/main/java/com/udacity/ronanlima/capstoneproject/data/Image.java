package com.udacity.ronanlima.capstoneproject.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.Data;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "image", foreignKeys = @ForeignKey(entity = Project.class,
        parentColumns = "id",
        childColumns = "idProjeto",
        onDelete = CASCADE))
@Data
public class Image {
    @PrimaryKey
    @NonNull
    private String id;
    private String descricao;
    private String urlImagem;
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

    public Image() {
    }
}
