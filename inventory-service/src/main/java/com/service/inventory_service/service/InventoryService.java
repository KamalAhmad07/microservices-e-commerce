package com.service.inventory_service.service;

import com.service.inventory_service.dto.InventoryResponse;
import com.service.inventory_service.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private  final InventoryRepository inventoryRepository;

    @Transactional
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode){
//        System.err.println("wait start in inventory");
//        Thread.sleep(10000);
        System.err.println( "Inventory Service");

      List<InventoryResponse> responses =  inventoryRepository.findBySkuCodeIn(skuCode)
              .stream()
              .map(inventory ->
                  InventoryResponse.builder()
                          .skuCode(inventory.getSkuCode())
                          .isInStock(inventory.getQuantity() > 0)
                          .build()).toList();

      log.info("inventory service value = "+responses);
      return responses;
    }
}
