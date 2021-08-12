package com.example.projetojavabd.model.dto;

import com.example.projetojavabd.model.ValorPorMes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDTO {
    @Id
    private String nome;
    private List<ValorPorMes> meses;
    private double media;
    private int moda;
    private double variancia;
    private double desvio_padrao;
    private int max;
    private int min;
}
