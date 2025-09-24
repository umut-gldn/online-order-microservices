package com.onlineorder.courierservice.controller;

import com.onlineorder.courierservice.dto.CreateDeliveryRequest;
import com.onlineorder.courierservice.dto.DeliveryResponse;
import com.onlineorder.courierservice.dto.UpdateDeliveryRequest;
import com.onlineorder.courierservice.model.DeliveryStatus;
import com.onlineorder.courierservice.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService service;

    @PostMapping
    public ResponseEntity<DeliveryResponse> assign(@Valid @RequestBody CreateDeliveryRequest request){
        return ResponseEntity.ok(service.assign(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> get(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<DeliveryResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ){

        Sort s=sort.toLowerCase().endsWith(",desc") ?
                Sort.by(sort.split(",")[0]).descending() :
                Sort.by(sort.split(",")[0]).ascending();

        return  ResponseEntity.ok(service.list(PageRequest.of(page,size,s)));
    }

    @GetMapping("/by-courier/{courierId}")
    public ResponseEntity<Page<DeliveryResponse>> listByCourier(
            @PathVariable Long courierId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        return  ResponseEntity.ok(service.listByCourier(courierId,PageRequest.of(page,size)));
    }

    @GetMapping("/by-status")
    public ResponseEntity<Page<DeliveryResponse>> listByStatus(
            @RequestParam DeliveryStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        return  ResponseEntity.ok(service.listByStatus(status,PageRequest.of(page,size)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryResponse> update(@PathVariable Long id,@Valid @RequestBody UpdateDeliveryRequest request ){
        return ResponseEntity.ok(service.update(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
