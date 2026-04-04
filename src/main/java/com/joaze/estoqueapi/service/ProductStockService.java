package com.joaze.estoqueapi.service;

import com.joaze.estoqueapi.dto.MovementDto;
import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementType;
import com.joaze.estoqueapi.model.Product;
import com.joaze.estoqueapi.repository.MovementRepository;
import com.joaze.estoqueapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ProductStockService {

    private MovementRepository movementRepository;

    private ProductRepository productRepository;

    @Transactional
    public void stockIn(MovementDto movementDto){
        Product productData = productRepository.findById(movementDto.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        productData.setQuantity(productData.getQuantity() + movementDto.quantity());
        productData.setUpdatedAt(LocalDateTime.now());

        Movement movement = this.createMovement(MovementType.ENTRADA, movementDto.quantity(), productData);
        movementRepository.save(movement);
    }

    @Transactional
    public void stockOut(MovementDto movementDto) {
        Product productData = productRepository.findById(movementDto.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (movementDto.quantity() > productData.getQuantity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock!");
        }

        productData.setQuantity(productData.getQuantity() - movementDto.quantity());
        productData.setUpdatedAt(LocalDateTime.now());

        Movement movement = this.createMovement(MovementType.SAIDA, movementDto.quantity(), productData);
        movementRepository.save(movement);
    }

    private Movement createMovement(MovementType type, Integer quantity, Product product){
        Movement movement = new Movement();

        movement.setType(type);
        movement.setQuantity(quantity);
        movement.setDate(LocalDateTime.now());
        movement.setProduct(product);
        return movement;
    }
}