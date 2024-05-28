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

        // Create new order
        Order order = new Order();
        order.setOrderCode(orderRequest.getOrderCode());
        order.setOrderDate(orderRequest.getOrderDate() != null ? orderRequest.getOrderDate() : new Date());
        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setQuantity(orderRequest.getQuantity());
        order.setCustomer(optionalCustomer.get());
        order.setItem(optionalItem.get());

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
            existingOrder.setOrderCode(orderRequest.getOrderCode());
            existingOrder.setOrderDate(orderRequest.getOrderDate() != null ? orderRequest.getOrderDate() : new Date());
            existingOrder.setTotalPrice(orderRequest.getTotalPrice());
            existingOrder.setQuantity(orderRequest.getQuantity());

            Optional<Customer> optionalCustomer = customerRepository.findById(orderRequest.getCustomerId());
            if (!optionalCustomer.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
            }
            existingOrder.setCustomer(optionalCustomer.get());

            Optional<Item> optionalItem = itemRepository.findById(orderRequest.getItemId());
            if (!optionalItem.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
            }
            existingOrder.setItem(optionalItem.get());

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
