package com.example.projetojavabd.services;


import com.example.projetojavabd.model.Estados;
import com.example.projetojavabd.model.dto.EstadoDTO;

import java.util.List;

public interface dadosServices {
    public List<Estados> listAll();
    public Estados createData(Estados data);
    public EstadoDTO findUF(String nameEstado);
    public double calcularMedia(Estados data);
    public double calcularVariancia(EstadoDTO data);
    public double calcularDP(EstadoDTO data);
    public int calcularModa(Estados data);
    public int calcularMin(Estados data);
    public int calcularMax(Estados data);
    public void lerFile(List<Estados> estados);
    public void delete(String nome);
    public void replace(Estados estado);

}
