package org.example.appwarehouse.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.appwarehouse.entity.*;
import org.example.appwarehouse.payload.*;
import org.example.appwarehouse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InputService {

    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InputProductRepository inputProductRepository;

    @Transactional
    public Result addInput(InputDto inputDto) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(inputDto.getWarehouseId());
        if (!warehouseOptional.isPresent()) {
            return new Result("Warehouse topilmadi", false);
        }

        Optional<Supplier> supplierOptional = supplierRepository.findById(inputDto.getSupplierId());
        if (!supplierOptional.isPresent()) {
            return new Result("Supplier topilmadi", false);
        }

        Optional<Currency> currencyOptional = currencyRepository.findById(inputDto.getCurrencyId());
        if (!currencyOptional.isPresent()) {
            return new Result("Currency topilmadi", false);
        }

        if (inputDto.getInputProducts() == null || inputDto.getInputProducts().isEmpty()) {
            return new Result("Input products bo'sh bo'lmasligi kerak", false);
        }

        // Collect product IDs
        List<Integer> productIds = inputDto.getInputProducts().stream()
                .map(InputProductDto::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(productIds);
        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // Check for any missing product
        for (Integer productId : productIds) {
            if (!productMap.containsKey(productId)) {
                return new Result("Product topilmadi: " + productId, false);
            }
        }

        Input input = new Input();
        input.setDate(inputDto.getDate());
        input.setWarehouse(warehouseOptional.get());
        input.setSupplier(supplierOptional.get());
        input.setCurrency(currencyOptional.get());
        input.setFactureNumber(inputDto.getFactureNumber());
        input.setCode(generateCode());
        Input savedInput = inputRepository.save(input);

        for (InputProductDto ip : inputDto.getInputProducts()) {
            InputProduct inputProduct = new InputProduct();
            inputProduct.setInput(savedInput);
            inputProduct.setProduct(productMap.get(ip.getProductId()));
            inputProduct.setAmount(ip.getAmount());
            inputProduct.setPrice(ip.getPrice());
            inputProduct.setExpireDate(ip.getExpireDate());
            inputProductRepository.save(inputProduct);
        }

        return new Result("Input muvaffaqiyatli saqlandi", true, savedInput.getId());
    }

    @Transactional
    public Result updateInput(Integer id, InputDto inputDto) {
        Optional<Input> inputOptional = inputRepository.findById(id);
        if (!inputOptional.isPresent()) {
            return new Result("Input topilmadi", false);
        }

        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(inputDto.getWarehouseId());
        if (!warehouseOptional.isPresent()) {
            return new Result("Warehouse topilmadi", false);
        }

        Optional<Supplier> supplierOptional = supplierRepository.findById(inputDto.getSupplierId());
        if (!supplierOptional.isPresent()) {
            return new Result("Supplier topilmadi", false);
        }

        Optional<Currency> currencyOptional = currencyRepository.findById(inputDto.getCurrencyId());
        if (!currencyOptional.isPresent()) {
            return new Result("Currency topilmadi", false);
        }

        if (inputDto.getInputProducts() == null || inputDto.getInputProducts().isEmpty()) {
            return new Result("Input products bo'sh bo'lmasligi kerak", false);
        }

        // Validate all product IDs
        List<Integer> productIds = inputDto.getInputProducts().stream()
                .map(InputProductDto::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(productIds);
        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        for (Integer productId : productIds) {
            if (!productMap.containsKey(productId)) {
                return new Result("Product topilmadi: " + productId, false);
            }
        }

        Input input = inputOptional.get();
        input.setDate(inputDto.getDate());
        input.setWarehouse(warehouseOptional.get());
        input.setSupplier(supplierOptional.get());
        input.setCurrency(currencyOptional.get());
        input.setFactureNumber(inputDto.getFactureNumber());

        inputProductRepository.deleteByInputId(input.getId());
        Input savedInput = inputRepository.save(input);

        for (InputProductDto ip : inputDto.getInputProducts()) {
            InputProduct inputProduct = new InputProduct();
            inputProduct.setInput(savedInput);
            inputProduct.setProduct(productMap.get(ip.getProductId()));
            inputProduct.setAmount(ip.getAmount());
            inputProduct.setPrice(ip.getPrice());
            inputProduct.setExpireDate(ip.getExpireDate());
            inputProductRepository.save(inputProduct);
        }

        return new Result("Input muvaffaqiyatli yangilandi", true, savedInput.getId());
    }

    public Result deleteInput(Integer id) {
        Optional<Input> optionalInput = inputRepository.findById(id);
        if (!optionalInput.isPresent()) {
            return new Result("Input topilmadi", false);
        }
        inputProductRepository.deleteByInputId(id);
        //har extimolga qarshi manual ham o'chiryapman
        inputRepository.deleteById(id);
        return new Result("Input o'chirildi", true, id);
    };

    public InputResponseDto getInput(Integer id) {
        Input input = inputRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Input not found with id: " + id));

        InputResponseDto dto = new InputResponseDto();
        dto.setId(input.getId());
        dto.setDate(input.getDate().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
        dto.setWarehouseId(input.getWarehouse().getId());
        dto.setWarehouseName(input.getWarehouse().getName());
        dto.setSupplierId(input.getSupplier().getId());
        dto.setSupplierName(input.getSupplier().getName());
        dto.setCurrencyCode(input.getCurrency().getName());
        dto.setInvoiceNumber(input.getFactureNumber());
        dto.setCode(input.getCode());

        List<InputProductResponseDto> productDtos = input.getInputProducts().stream()
                .map(inputProduct -> {
                    Product product = inputProduct.getProduct();
                    Category category = product.getCategory();
                    Measurement measurement = product.getMeasurement();
                    Attachment attachment = product.getAttachment();

                    return new InputProductResponseDto(
                            inputProduct.getId(),
                            product.getId(),
                            product.getName(),
                            product.getCode(),
                            attachment != null ? generatePhotoUrl(attachment.getId()) : null,
                            inputProduct.getAmount(),
                            inputProduct.getPrice(),
                            inputProduct.getExpireDate() != null ?
                                    new java.sql.Date(inputProduct.getExpireDate().getTime())
                                            .toLocalDate()
                                            .format(DateTimeFormatter.ISO_LOCAL_DATE) : null,
                            category != null ? category.getName() : null,
                            measurement != null ? measurement.getName() : null
                    );
                })
                .collect(Collectors.toList());

        dto.setInputProducts(productDtos);
        return dto;
    }

    public List<InputResponseDto> getInputProducts() {
            List<Input> inputs = inputRepository.findAllWithDetails();
            return inputs.stream()
                    .map(input -> {
                        InputResponseDto dto = new InputResponseDto();
                        dto.setId(input.getId());
                        dto.setDate(input.getDate().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                        dto.setWarehouseId(input.getWarehouse().getId());
                        dto.setWarehouseName(input.getWarehouse().getName());
                        dto.setSupplierId(input.getSupplier().getId());
                        dto.setSupplierName(input.getSupplier().getName());
                        dto.setCurrencyCode(input.getCurrency().getName());
                        dto.setInvoiceNumber(input.getFactureNumber());
                        dto.setCode(input.getCode());

                        List<InputProductResponseDto> productDtos = input.getInputProducts().stream()
                                .map(inputProduct -> {
                                    Product product = inputProduct.getProduct();
                                    Category category = product.getCategory();
                                    Measurement measurement = product.getMeasurement();
                                    Attachment attachment = product.getAttachment();

                                    return new InputProductResponseDto(
                                            inputProduct.getId(),
                                            product.getId(),
                                            product.getName(),
                                            product.getCode(),
                                            attachment != null ? generatePhotoUrl(attachment.getId()) : null,
                                            inputProduct.getAmount(),
                                            inputProduct.getPrice(),
                                            inputProduct.getExpireDate() != null ?
                                                    new java.sql.Date(inputProduct.getExpireDate().getTime())
                                                            .toLocalDate()
                                                            .format(DateTimeFormatter.ISO_LOCAL_DATE) : null,
                                            category != null ? category.getName() : null,
                                            measurement != null ? measurement.getName() : null
                                    );
                                })
                                .collect(Collectors.toList());

                        dto.setInputProducts(productDtos);
                        return dto;
                    })
                    .collect(Collectors.toList());
        }

    private String generatePhotoUrl(Integer attachmentId) {
        return "/attachment/photos/" + attachmentId;
    }

    private String generateCode() {
        Optional<Input> optionalInput = inputRepository.findTopByCodeAsNumberDesc();
        if (optionalInput.isPresent()) {
            String maxCodeStr = optionalInput.get().getCode();
            try {
                int maxCode = Integer.parseInt(maxCodeStr);
                return String.valueOf(maxCode + 1);
            } catch (NumberFormatException e) {
                return "1";
            }
        }
        return "1";
    };

}
