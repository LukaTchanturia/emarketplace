package com.example.emarketplace.service;

import com.example.emarketplace.dto.ItemResponseDto;
import com.example.emarketplace.entity.Item;
import com.example.emarketplace.repository.ItemRepository;
import com.example.emarketplace.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testGetItemByIdSuccess() {
        Item item = new Item();
        item.setId(5L);
        item.setName("Macbook");
        item.setPrice(2000.0);

        when(itemRepository.findById(5L)).thenReturn(Optional.of(item));

        ItemResponseDto result = itemService.getItemById(5L);

        assertEquals("Macbook", result.getName());
        assertEquals(2000.0, result.getPrice());
        verify(itemRepository).findById(5L);
    }

    @Test
    public void testGetItemByIdThrow() {
        when(itemRepository.findById(99L)).thenThrow(new RuntimeException("Item not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            itemService.getItemById(99L);
        });

        assertEquals("Item not found", exception.getMessage());
        verify(itemRepository).findById(99L);
    }
}