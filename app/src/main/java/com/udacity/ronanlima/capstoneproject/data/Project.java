package com.udacity.ronanlima.capstoneproject.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import lombok.Data;

/**
 * Created by rlima on 27/11/18.
 */

@Entity(tableName = "project")
@Data
public class Project implements Parcelable {
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

    @Ignore
    protected Project(Parcel in) {
        id = in.readString();
        descricao = in.readString();
        idProposito = in.readString();
        nomeProjeto = in.readString();
        imagemCapa = in.readString();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(descricao);
        parcel.writeString(idProposito);
        parcel.writeString(nomeProjeto);
        parcel.writeString(imagemCapa);
    }

    //    @Ignore
//    private List<Image> images;
}
