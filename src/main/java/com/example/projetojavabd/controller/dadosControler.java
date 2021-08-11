package com.example.projetojavabd.controller;

import com.example.projetojavabd.model.Estados;
import com.example.projetojavabd.model.ValorPorMes;
import com.example.projetojavabd.model.dto.EstadoDTO;
import com.example.projetojavabd.services.dadosServices;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/dados")
public class dadosControler {
    @Autowired
    public dadosServices dadosServices;
    ArrayList<String> meses_ano = new ArrayList<String>() {
        {
            add("Janeiro");
            add("Fevereiro");
            add("Março");
            add("Abril");
            add("Maio");
            add("Junho");
            add("Julho");
            add("Agosto");
            add("Setembro");
            add("Outubro");
            add("Novembro");
            add("Dezembro");
        }
    };

    @GetMapping
    public List<Estados> listAll() {
        return this.dadosServices.listAll();
    }

    @GetMapping("/{id}")
    public EstadoDTO find(@PathVariable String id) {
        return this.dadosServices.findUF(id);
    }

    @PutMapping
    public ResponseEntity<Estados> replace(@RequestBody Estados estado) {
        dadosServices.replace(estado);
        return new ResponseEntity<>(estado, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        dadosServices.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity<List<Estados>> importExcelFile(@RequestParam("file") MultipartFile files) throws IOException {
        HttpStatus status = HttpStatus.OK;
        List<Estados> estadosList = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        XSSFRow FirstRow = worksheet.getRow(0);

        for (int index = 0; index < FirstRow.getLastCellNum(); index++) {
            if(index > 0) {
                Estados estado = new Estados();
                List<ValorPorMes> meses = new ArrayList<>();
                estado.setNome(FirstRow.getCell(index).getStringCellValue());
                for(int i = 1; i < 13 ; i++) {
                    ValorPorMes mes = new ValorPorMes();
                    XSSFRow rowByColuna = worksheet.getRow(i);
                    mes.setMes(meses_ano.get(i-1));

                    Integer number = (int) rowByColuna.getCell(index).getNumericCellValue();
                    mes.setValue(number);

                    meses.add(mes);
                }
                estado.setMeses(meses);
                estadosList.add(estado);
            }
        }
        dadosServices.lerFile(estadosList);
        return new ResponseEntity<>(estadosList, status);
    }
}
