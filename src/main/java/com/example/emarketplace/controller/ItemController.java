package com.example.emarketplace.controller;

import com.example.emarketplace.dto.ItemResponseDto;
import com.example.emarketplace.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/items")
@CrossOrigin
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<Page<ItemResponseDto>> getAllItems(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(itemService.getItems(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PostMapping
    public ResponseEntity<?> addItem(@RequestParam("name") String name,
                                     @RequestParam("price") Double price,
                                     @RequestParam("description") String description,
                                     @RequestParam("photo") MultipartFile photo) {
        itemService.createItem(name, price, description, photo);
        return ResponseEntity.ok().build();
    }
}