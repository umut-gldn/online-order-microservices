package com.onlineorder.restaurantservice.controller;

import com.onlineorder.restaurantservice.dto.CreateMenuItemRequest;
import com.onlineorder.restaurantservice.dto.MenuItemResponse;
import com.onlineorder.restaurantservice.dto.UpdateMenuItemRequest;
import com.onlineorder.restaurantservice.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemResponse> create(@PathVariable Long restaurantId,
                                                   @RequestBody @Valid CreateMenuItemRequest request) {
        return ResponseEntity.ok(menuItemService.create(restaurantId, request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> get(@PathVariable Long restaurantId, @PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getById(restaurantId, id));
    }

    @GetMapping
    public ResponseEntity<Page<MenuItemResponse>> list(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        Sort s = Sort.by(sort.split(",")[0]).ascending();
        if (sort.toLowerCase().endsWith(",desc")) {
            s = Sort.by(sort.split(",")[0]).descending();
        }
        Pageable pageable = PageRequest.of(page, size, s);
        return ResponseEntity.ok(menuItemService.list(restaurantId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> update(@PathVariable Long restaurantId,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody UpdateMenuItemRequest request) {

        return ResponseEntity.ok(menuItemService.update(restaurantId, id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long restaurantId, @PathVariable Long id) {
        menuItemService.delete(restaurantId, id);
        return ResponseEntity.noContent().build();
    }
}
