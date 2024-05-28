package com.app.onlineshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.app.onlineshop.DTO.request.ItemRequest;
import com.app.onlineshop.DTO.response.ItemResponse;
import com.app.onlineshop.model.Item;
import com.app.onlineshop.repository.ItemRepository;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Item> getItemById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    @Transactional
    public ItemResponse createItem(ItemRequest requestDTO) {
        Item item = new Item();
        item.setItemName(requestDTO.getItemName());
        item.setItemCode(requestDTO.getItemCode());
        item.setStock(requestDTO.getStock());
        item.setPrice(requestDTO.getPrice());
        item.setAvailable(true);
        item.setLastRestock(requestDTO.getLastRestock());
        Item savedItem = itemRepository.save(item);
        return new ItemResponse(
                savedItem.getItemId(),
                savedItem.getItemName(),
                savedItem.getItemCode(),
                savedItem.getStock(),
                savedItem.getPrice(),
                savedItem.isAvailable(),
                savedItem.getLastRestock());
    }

    @Transactional
    public ItemResponse updateItem(Long itemId, ItemRequest requestDTO) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            Item existingItem = optionalItem.get();
            existingItem.setItemName(requestDTO.getItemName());
            existingItem.setItemCode(requestDTO.getItemCode());
            existingItem.setStock(requestDTO.getStock());
            existingItem.setPrice(requestDTO.getPrice());
            existingItem.setAvailable(requestDTO.isAvailable());
            existingItem.setLastRestock(requestDTO.getLastRestock());
            Item updatedItem = itemRepository.save(existingItem);
            return new ItemResponse(
                    updatedItem.getItemId(),
                    updatedItem.getItemName(),
                    updatedItem.getItemCode(),
                    updatedItem.getStock(),
                    updatedItem.getPrice(),
                    updatedItem.isAvailable(),
                    updatedItem.getLastRestock());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item with ID " + itemId + " not found");
        }
    }

    @Transactional
    public void deleteItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            itemRepository.deleteById(itemId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item with ID " + itemId + " not found");
        }
    }
}
