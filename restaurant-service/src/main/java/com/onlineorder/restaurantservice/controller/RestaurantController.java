package com.onlineorder.restaurantservice.controller;

import com.onlineorder.restaurantservice.dto.CreateRestaurantRequest;
import com.onlineorder.restaurantservice.dto.RestaurantResponse;
import com.onlineorder.restaurantservice.dto.UpdateRestaurantRequest;
import com.onlineorder.restaurantservice.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@RequestBody @Valid CreateRestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<RestaurantResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort){

        Sort s =Sort.by(sort.split(",")[0]).ascending();
        if(sort.toLowerCase().endsWith(",desc")){
            s =Sort.by(sort.split(",")[0]).descending();
        }

        Pageable pageable = PageRequest.of(page, size, s);
        return ResponseEntity.ok(restaurantService.list(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateRestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
