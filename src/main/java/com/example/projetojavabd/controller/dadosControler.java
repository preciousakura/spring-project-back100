package com.example.projetojavabd.controller;

import com.example.projetojavabd.model.Estados;
import com.example.projetojavabd.model.ValorPorMes;
import com.example.projetojavabd.model.dto.EstadoDTO;
import com.example.projetojavabd.services.dadosServices;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    @GetMapping("/export")
    public void exportExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=dados.xlsx";
        response.setHeader(headerKey, headerValue);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("dados");

        List<Estados> dados = listAll();
        for (int i = 0; i < dados.size() + 1; i++) {
            if (i > 0) {
                for (int k = 0; k < 13; k++) {
                    Row row = sheet.getRow(k);
                    if (k > 0)
                        row.createCell(i).setCellValue((Integer) dados.get(i - 1).getMeses().get(k - 1).getValue());
                    else
                        row.createCell(i).setCellValue((String) dados.get(i - 1).getNome());

                }
            } else {
                for (int k = 0; k < 13; k++) {
                    Row row = sheet.createRow(k);
                    if (k > 0)
                        row.createCell(i).setCellValue((String) meses_ano.get(k - 1));
                }
            }
        }

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.close();
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
                String nameEstado = FirstRow.getCell(index).getStringCellValue();
                if(!(nameEstado.isBlank() && nameEstado.isEmpty())) {
                    Estados estado = new Estados();
                    List<ValorPorMes> meses = new ArrayList<>();
                    estado.setNome(nameEstado);
                    for (int i = 1; i < 13; i++) {
                        ValorPorMes mes = new ValorPorMes();
                        XSSFRow rowByColuna = worksheet.getRow(i);
                        mes.setMes(meses_ano.get(i - 1));

                        Integer number = (int) rowByColuna.getCell(index).getNumericCellValue();
                        mes.setValue(number);

                        meses.add(mes);
                    }
                    estado.setMeses(meses);
                    estadosList.add(estado);
                }
            }
        }
        dadosServices.lerFile(estadosList);
        return new ResponseEntity<>(dadosServices.listAll(), status);
    }
}
