package com.example.projetojavabd.services.impl;

import com.example.projetojavabd.model.Estados;
import com.example.projetojavabd.model.dto.EstadoDTO;
import com.example.projetojavabd.repository.dadosRepository;
import com.example.projetojavabd.services.dadosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

import javax.persistence.EntityNotFoundException;

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
    public double calcularDP(EstadoDTO data) {
        double desvio_p = 0;
        double media = data.getMedia();
        for(int i = 0; i < data.getMeses().size(); i++) {
            desvio_p += Math.pow(data.getMeses().get(i).getValue() - media, 2);
        }
        return Math.sqrt(desvio_p/data.getMeses().size());
    }

    public double calcularVariancia(EstadoDTO data) {
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
        return this.dadosRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    public EstadoDTO convertToDto(Estados estado) {
        EstadoDTO estadoDto = new EstadoDTO();
        estadoDto.setNome(estado.getNome());
        estadoDto.setMeses(estado.getMeses());
        estadoDto.setMedia(this.calcularMedia(estado));
        estadoDto.setDesvio_padrao(this.calcularDP(estadoDto));
        estadoDto.setVariancia(this.calcularVariancia(estadoDto));
        estadoDto.setModa(this.calcularModa(estado));
        estadoDto.setMax(this.calcularMax(estado));
        estadoDto.setMin(this.calcularMin(estado));
        return estadoDto;
    }

    public Estados createData(Estados data) {
        return this.dadosRepository.save(data);
    }

    public EstadoDTO findUF(String nameEstado) {
        Estados estado = this.dadosRepository
                .findById(nameEstado)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Estado " + nameEstado + "was not found"));
        return convertToDto(estado);
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
