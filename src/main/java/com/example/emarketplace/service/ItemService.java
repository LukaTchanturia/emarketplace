package com.example.emarketplace.service;

import com.example.emarketplace.dto.ItemResponseDto;
import com.example.emarketplace.entity.Item;
import com.example.emarketplace.entity.User;
import com.example.emarketplace.repository.ItemRepository;
import com.example.emarketplace.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final Path root = Paths.get("uploads");

    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Page<ItemResponseDto> getItems(int page, String sortParam) {
        Sort sort;
        if ("dateAsc".equals(sortParam)) {
            sort = Sort.by("submissionTime").ascending();
        } else if ("priceAsc".equals(sortParam)) {
            sort = Sort.by("price").ascending();
        } else if ("priceDesc".equals(sortParam)) {
            sort = Sort.by("price").descending();
        } else {
            sort = Sort.by("submissionTime").descending();
        }

        PageRequest pageRequest = PageRequest.of(page, 6, sort);
        return itemRepository.findAll(pageRequest).map(this::convertToDto);
    }

    public ItemResponseDto getItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException());
        return convertToDto(item);
    }

    public void createItem(String name, Double price, String description, MultipartFile file, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());

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
        item.setUser(user);
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
        if (item.getUser() != null) {
            dto.setUsername(item.getUser().getUsername());
        }
        return dto;
    }
}