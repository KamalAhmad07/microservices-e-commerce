package com.service.order_service.Service;

import com.service.order_service.dto.InventoryResponse;
import com.service.order_service.dto.OrderLineItemsDto;
import com.service.order_service.dto.OrderRequest;
import com.service.order_service.event.OrderPlacedEvent;
import com.service.order_service.model.Order;
import com.service.order_service.model.OrderLineItems;
import com.service.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.StructuredDataId;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {


   private  final OrderRepository orderRepository;
   private  final WebClient.Builder webClientBuilder;
   private  final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public  String placeOrder(OrderRequest orderRequest)  {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

       List<OrderLineItems> orderLineItemsList =  orderRequest.getOrderLineItemsDtosList()
                .stream()
                .map(this::mapToData)
                .toList();

       order.setOrderLineItemsList(orderLineItemsList);

       // getSkuCodes
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode).toList();

       //Call Inventory service, and place order if product is in stock
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                        .uri("http://inventory-service/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block(); // syncronus result



        boolean allProductsInStock = false;
        if(inventoryResponses != null && inventoryResponses.length>0) {
            allProductsInStock = Arrays.asList(inventoryResponses).stream()
                    .allMatch(InventoryResponse::isInStock);
        }

      log.info("allProductsInStock = "+allProductsInStock);

       if(allProductsInStock){
           orderRepository.save(order);
           kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
           return "Order placed successfully!";
       }else {
            throw new RuntimeException("Product is not in stock, please try again later!!");
       }
    }

     private OrderLineItems mapToData(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
     }
}
