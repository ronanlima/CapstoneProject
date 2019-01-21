package com.udacity.ronanlima.capstoneproject.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import lombok.Data;
import lombok.ToString;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "image", foreignKeys = @ForeignKey(entity = Project.class,
        parentColumns = "id",
        childColumns = "idProjeto",
        onDelete = CASCADE,
        onUpdate = CASCADE),
        primaryKeys = {"id", "idProjeto"}, indices = {@Index("idProjeto")})
@Data
@ToString
public class Image {
    @NonNull
    private String id;
    private String descricao;
    private String urlImagem;
    @NonNull
    private String idProjeto;
    private String uriImagem;
    private String nome;

    public Image(String id, String descricao, String urlImagem, String idProjeto, String uriImagem, String nome) {
        setId(id);
        setDescricao(descricao);
        setUrlImagem(urlImagem);
        setIdProjeto(idProjeto);
        setUriImagem(uriImagem);
        setNome(nome);
    }

    @Ignore
    public Image(String descricao, String urlImagem, String idProjeto) {
        setDescricao(descricao);
        setUrlImagem(urlImagem);
        setIdProjeto(idProjeto);
    }

    @Ignore
    public Image() {
    }

}
