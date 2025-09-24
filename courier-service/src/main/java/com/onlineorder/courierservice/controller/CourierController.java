package com.onlineorder.courierservice.controller;

import com.onlineorder.courierservice.dto.CourierResponse;
import com.onlineorder.courierservice.dto.CreateCourierRequest;
import com.onlineorder.courierservice.dto.UpdateCourierRequest;
import com.onlineorder.courierservice.service.CourierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService service;

    @PostMapping
    public ResponseEntity<CourierResponse> create(@Valid @RequestBody CreateCourierRequest request){
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourierResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CourierResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ){
        Sort s = sort.toLowerCase().endsWith(",asc")
                ? Sort.by(sort.split(",")[0]).ascending()
                : Sort.by(sort.split(",")[0]).descending();

        return ResponseEntity.ok(service.list(PageRequest.of(page,size,s)));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<CourierResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateCourierRequest request){
        return ResponseEntity.ok(service.update(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
