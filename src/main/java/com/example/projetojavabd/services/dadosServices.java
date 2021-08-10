package com.example.projetojavabd.services;


import com.example.projetojavabd.model.Estados;

import java.util.List;
import java.util.Optional;

public interface dadosServices {
    public List<Estados> listAll();
    public Estados createData(Estados data);
    public Optional<Estados> findUF(String nameEstado);
    public double calcularMedia(Estados data);
    public double calcularVariancia(Estados data);
    public double calcularDP(Estados data);
    public int calcularModa(Estados data);
    public int calcularMin(Estados data);
    public int calcularMax(Estados data);
}
