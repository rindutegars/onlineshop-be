package com.app.onlineshop.controller;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.onlineshop.service.ReportService;

import net.sf.jasperreports.engine.JRException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/store/report/")
@RestController
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("{format}")
    public ResponseEntity<?> exportReport(@PathVariable String format) throws FileNotFoundException, JRException {
        return reportService.exportReport(format);
    }
}