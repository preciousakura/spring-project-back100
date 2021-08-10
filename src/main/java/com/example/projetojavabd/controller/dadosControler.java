package com.example.projetojavabd.controller;

import com.example.projetojavabd.model.Estados;
import com.example.projetojavabd.services.dadosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/dados")
public class dadosControler {
    @Autowired
    public dadosServices dadosServices;

    @GetMapping
    public List<Estados> listAll() {
        return this.dadosServices.listAll();
    }

    @GetMapping("/{id}")
    public Optional<Estados> find(@PathVariable String id) {
        return this.dadosServices.findUF(id);
    }

    @PostMapping
    public Estados createData(@RequestBody Estados data) {
        return dadosServices.createData(data);
    }

}
