package com.app.onlineshop.service;

import com.app.onlineshop.DTO.request.OrderRequest;
import com.app.onlineshop.DTO.response.OrderResponse;
import com.app.onlineshop.model.Customer;
import com.app.onlineshop.model.Item;
import com.app.onlineshop.model.Order;
import com.app.onlineshop.repository.CustomerRepository;
import com.app.onlineshop.repository.ItemRepository;
import com.app.onlineshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Validate customer
        Optional<Customer> optionalCustomer = customerRepository.findById(orderRequest.getCustomerId());
        if (!optionalCustomer.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        // Validate item
        Optional<Item> optionalItem = itemRepository.findById(orderRequest.getItemId());
        if (!optionalItem.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }

        // Check if item is available
        Item item = optionalItem.get();
        if (!item.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item is not available");
        }

        // Check if there is enough stock
        if (item.getStock() < orderRequest.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock for item");
        }

        // Calculate total price based on item price and quantity
        Double totalPrice = item.getPrice() * orderRequest.getQuantity();

        // Create new order
        Order order = new Order();
        order.setOrderCode(orderRequest.getOrderCode());
        order.setOrderDate(orderRequest.getOrderDate() != null ? orderRequest.getOrderDate() : new Date());
        order.setTotalPrice(totalPrice); // Set total price calculated
        order.setQuantity(orderRequest.getQuantity());
        order.setCustomer(optionalCustomer.get());
        order.setItem(item);

        // Update item stock
        int newStock = item.getStock() - orderRequest.getQuantity();
        item.setStock(newStock);

        // Check if item is out of stock
        if (newStock == 0) {
            item.setAvailable(false);
        }

        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getOrderCode(),
                savedOrder.getOrderDate(),
                savedOrder.getTotalPrice(),
                savedOrder.getQuantity(),
                savedOrder.getCustomer().getCustomerId(),
                savedOrder.getItem().getItemId());
    }

    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderRequest orderRequest) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order existingOrder = optionalOrder.get();
            int previousQuantity = existingOrder.getQuantity();
            Item item = existingOrder.getItem();
            if (!item.isAvailable()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item is not available");
            }

            int quantityDifference = orderRequest.getQuantity() - previousQuantity;

            if (quantityDifference != 0) {
                int newStock = item.getStock() - quantityDifference;
                if (newStock < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock for item");
                }

                // Update item stock
                item.setStock(newStock);

                // Check if item is out of stock
                if (newStock == 0) {
                    item.setAvailable(false);
                }
            }

            // Update the order details
            existingOrder.setOrderCode(orderRequest.getOrderCode());
            existingOrder.setOrderDate(orderRequest.getOrderDate() != null ? orderRequest.getOrderDate() : new Date());
            existingOrder.setTotalPrice(item.getPrice() * orderRequest.getQuantity()); // Update total price based on
                                                                                       // new quantity
            existingOrder.setQuantity(orderRequest.getQuantity());

            Order updatedOrder = orderRepository.save(existingOrder);

            return new OrderResponse(
                    updatedOrder.getOrderId(),
                    updatedOrder.getOrderCode(),
                    updatedOrder.getOrderDate(),
                    updatedOrder.getTotalPrice(),
                    updatedOrder.getQuantity(),
                    updatedOrder.getCustomer().getCustomerId(),
                    updatedOrder.getItem().getItemId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with ID " + orderId + " not found");
        }
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with ID " + orderId + " not found");
        }
    }
}
