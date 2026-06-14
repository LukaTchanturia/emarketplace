package com.example.emarketplace.service;

import com.example.emarketplace.dto.ItemResponseDto;
import com.example.emarketplace.entity.Item;
import com.example.emarketplace.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final Path root = Paths.get("uploads");

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Page<ItemResponseDto> getItems(int page) {
        PageRequest pageRequest = PageRequest.of(page, 6, Sort.by("submissionTime").descending());
        return itemRepository.findAll(pageRequest).map(this::convertToDto);
    }

    public ItemResponseDto getItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException());
        return convertToDto(item);
    }

    public void createItem(String name, Double price, String description, MultipartFile file) {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            Files.copy(file.getInputStream(), this.root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);
        item.setSubmissionTime(LocalDateTime.now());
        item.setPhotoUrl("/uploads/" + filename);
        itemRepository.save(item);
    }

    private ItemResponseDto convertToDto(Item item) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setDescription(item.getDescription());
        dto.setSubmissionTime(item.getSubmissionTime());
        dto.setPhotoUrl(item.getPhotoUrl());
        return dto;
    }
}