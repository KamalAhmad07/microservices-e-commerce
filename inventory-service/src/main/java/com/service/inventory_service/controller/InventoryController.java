package com.service.inventory_service.controller;

import com.service.inventory_service.dto.InventoryResponse;
import com.service.inventory_service.service.InventoryService;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode, HttpServletRequest request){
        log.info("Inside the inventory controller!!");
        log.error("URI = {} \n AND path ={} "+ request.getRequestURL(), request.getQueryString());

         return inventoryService.isInStock(skuCode);
    }


}
