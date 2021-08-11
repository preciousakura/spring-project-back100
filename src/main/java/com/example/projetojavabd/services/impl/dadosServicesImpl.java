package com.example.projetojavabd.services.impl;

import com.example.projetojavabd.model.Estados;
import com.example.projetojavabd.repository.dadosRepository;
import com.example.projetojavabd.services.dadosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class dadosServicesImpl implements dadosServices {
    @Autowired
    private dadosRepository dadosRepository;

    public double calcularMedia(Estados data) {
        double media = 0;
        for(int i = 0; i < data.getMeses().size(); i++) {
            media += data.getMeses().get(i).getValue();
        } return media/data.getMeses().size();
    }
    public double calcularDP(Estados data) {
        double desvio_p = 0;
        double media = data.getMedia();
        for(int i = 0; i < data.getMeses().size(); i++) {
            desvio_p += Math.pow(data.getMeses().get(i).getValue() - media, 2);
        }
        return Math.sqrt(desvio_p)/data.getMeses().size();
    }

    public double calcularVariancia(Estados data) {
        return Math.pow(data.getDesvio_padrao(), 2);
    }
    public int calcularModa(Estados data) {
        int maxValue = 0, maxCount = 0;

        for (int i = 0; i < data.getMeses().size(); ++i) {
            int count = 0;
            for (int j = 0; j < data.getMeses().size(); ++j) {
                if(data.getMeses().get(j).getValue() == data.getMeses().get(i).getValue() && i != j) ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = data.getMeses().get(i).getValue();
            }
        }
        if(maxCount < 1) return -1;
        return maxValue;
    }

    public int calcularMin(Estados data) {
        int min = data.getMeses().get(0).getValue();
        for(int i = 0; i < data.getMeses().size(); i++) {
            if(data.getMeses().get(i).getValue() < min)
                min = data.getMeses().get(i).getValue();
        } return min;
    }

    public int calcularMax(Estados data) {
        int max = data.getMeses().get(0).getValue();
        for(int i = 0; i < data.getMeses().size(); i++) {
            if(data.getMeses().get(i).getValue() > max)
                max = data.getMeses().get(i).getValue();
        } return max;
    }

    public List<Estados> listAll() {
        return this.dadosRepository.findAll();
    }
    public Estados createData(Estados data) {
        data.setMedia(this.calcularMedia(data));
        data.setDesvio_padrao(this.calcularDP(data));
        data.setVariancia(this.calcularVariancia(data));
        data.setModa(this.calcularModa(data));
        data.setMax(this.calcularMax(data));
        data.setMin(this.calcularMin(data));
        return this.dadosRepository.save(data);
    }
    public Optional<Estados> findUF(String nameEstado) {
        return dadosRepository.findById(nameEstado);
    }

    public void lerFile(List<Estados> estados) {
        if(this.dadosRepository.count() > 0) {
            this.dadosRepository.deleteAll();
        }

        for(int i = 0; i < estados.size(); i++) {
            createData(estados.get(i));
        }
    }

    public void delete(String id) {
        this.dadosRepository.deleteById(id);
    }

    public void replace(Estados estado) {
        delete(estado.getNome());
        createData(estado);
    }
}
