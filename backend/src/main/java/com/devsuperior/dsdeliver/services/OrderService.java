package com.devsuperior.dsdeliver.services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsdeliver.dto.OrderDTO;
import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.entities.Order;
import com.devsuperior.dsdeliver.entities.Product;
import com.devsuperior.dsdeliver.enus.OrderStatus;
import com.devsuperior.dsdeliver.repositories.OrderRepository;
import com.devsuperior.dsdeliver.repositories.ProductRepository;


@Service
public class OrderService {
	
	@Autowired	
	private OrderRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Transactional(readOnly = true)
	public List<OrderDTO>findAll(){
		List<Order> list = this.repository.findOrdersWithProduct();
		return list.stream().map(OrderDTO::new).collect(Collectors.toList());
		
	}

	@Transactional()
	public OrderDTO insert(OrderDTO dto) {
		Order order = new Order(dto.getAddress(), dto.getLatitude(),dto.getLongitude(), Instant.now(), OrderStatus.PENDING);
		for(ProductDTO p : dto.getProducts()) {
			Product prod = this.productRepository.getOne(p.getId());
			order.getProducts().add(prod);
			
		}
		order = repository.save(order);
		return new OrderDTO(order);
		
	}
	
	@Transactional()
	public OrderDTO alterStatus(Long id) {
		Order order = this.repository.getOne(id);
		order.setStatus(OrderStatus.DELIVERED);
		order = repository.save(order);
		return new OrderDTO(order);
	}
	
	
}
