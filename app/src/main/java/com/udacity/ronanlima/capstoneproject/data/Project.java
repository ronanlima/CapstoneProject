package com.udacity.ronanlima.capstoneproject.data;

import java.util.List;

import lombok.Data;

/**
 * Created by rlima on 27/11/18.
 */

@Data
public class Project {
    private String id;
    private String descricao;
    private String idProposito;
    private String nomeProjeto;
    private String imagemCapa;
    private List<Image> images;
}
