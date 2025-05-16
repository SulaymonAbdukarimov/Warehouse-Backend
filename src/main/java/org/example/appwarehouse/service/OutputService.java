package org.example.appwarehouse.service;

import lombok.RequiredArgsConstructor;
import org.example.appwarehouse.entity.*;
import org.example.appwarehouse.payload.OutputProductResponseDto;
import org.example.appwarehouse.payload.OutputRequestDto;
import org.example.appwarehouse.payload.OutputResponseDto;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OutputService {

    private static final String PHOTO_URL_PREFIX = "/attachment/photos/";
    private static final String ERR_NOT_FOUND = "%s not found with id: %d";
    private static final String ERR_INVALID_PRODUCTS = "Output products cannot be empty";
    private static final String MSG_ADDED = "Output added successfully";
    private static final String MSG_UPDATED = "Output updated successfully, id: %d";
    private static final String MSG_DELETED = "Output deleted successfully, id: %d";
    public static final String ERR_INVALID_PRODUCTs = "Invalid product id: %d";

    private final OutputRepository outputRepository;
    private final ProductRepository productRepository;
    private final CurrencyRepository currencyRepository;
    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;
    private final OutputProductRepository outputProductRepository;

    public List<OutputResponseDto> getOutputs() {
        List<Output> outputs = outputRepository.findAll();
        List<OutputResponseDto> responseDtos = new ArrayList<>();
        for (Output output : outputs) {
            responseDtos.add(mapOutputToResponseDto(output));
        }
        return responseDtos;
    }

    public OutputResponseDto getOutputById(Integer id) {
        return outputRepository.findById(id)
                .map(this::mapOutputToResponseDto)
                .orElse(new OutputResponseDto());
    }

    public Result addOutput(OutputRequestDto dto) {
        var clientOpt = clientRepository.findById(dto.getClientId());

        if (clientOpt.isEmpty()) return new Result("Client not found", false);

        var currencyOpt = currencyRepository.findById(dto.getCurrencyId());
        if (currencyOpt.isEmpty()) return new Result("Currency not found", false);

        var warehouseOpt = warehouseRepository.findById(dto.getWarehouseId());
        if (warehouseOpt.isEmpty()) return new Result("Warehouse not found", false);

        if (dto.getOutputProducts() == null || dto.getOutputProducts().isEmpty()) {
            return new Result("Output products bo'sh bo'lmasligi", false);
        }

        Output output = new Output();
        output.setClient(clientOpt.get());
        output.setCurrency(currencyOpt.get());
        output.setWarehouse(warehouseOpt.get());
        output.setDate(dto.getDate());
        output.setCode(generateCode());
        output.setFactureNumber(dto.getFactureNumber());

        Output savedOutput = outputRepository.save(output);

        for (var productDto : dto.getOutputProducts()) {
            var productOpt = productRepository.findById(productDto.getProductId());
            if (productOpt.isEmpty()) return new Result("Product not found", false);
            OutputProduct outputProduct = new OutputProduct();
            outputProduct.setProduct(productOpt.get());
            outputProduct.setAmount(productDto.getAmount());
            outputProduct.setPrice(productDto.getPrice());
            outputProduct.setExpireDate(productDto.getExpireDate());
            outputProduct.setOutput(savedOutput);
            outputProductRepository.save(outputProduct);
        }

        return new Result("Output added", true);
    }

    @Transactional
    public Result updateOutput(Integer id, OutputRequestDto dto) {
        var outputOpt = outputRepository.findById(id);
        if (outputOpt.isEmpty()) return new Result("Output not found", false);

        var clientOpt = clientRepository.findById(dto.getClientId());
        if (clientOpt.isEmpty()) return new Result("Client not found", false);

        var currencyOpt = currencyRepository.findById(dto.getCurrencyId());
        if (currencyOpt.isEmpty()) return new Result("Currency not found", false);

        var warehouseOpt = warehouseRepository.findById(dto.getWarehouseId());
        if (warehouseOpt.isEmpty()) return new Result("Warehouse not found", false);

        if (dto.getOutputProducts() == null || dto.getOutputProducts().isEmpty()) {
            return new Result("Output products bo'sh bo'lmasligi", false);
        }

        Output output = outputOpt.get();
        output.setClient(clientOpt.get());
        output.setCurrency(currencyOpt.get());
        output.setWarehouse(warehouseOpt.get());
        output.setDate(dto.getDate());
        output.setFactureNumber(dto.getFactureNumber());
        outputRepository.save(output);

        outputProductRepository.deleteByProductId(id);

        for (var productDto : dto.getOutputProducts()) {
            var productOpt = productRepository.findById(productDto.getProductId());
            if (productOpt.isEmpty()) return new Result("Product not found", false);

            OutputProduct outputProduct = new OutputProduct();
            outputProduct.setProduct(productOpt.get());
            outputProduct.setAmount(productDto.getAmount());
            outputProduct.setPrice(productDto.getPrice());
            outputProduct.setExpireDate(productDto.getExpireDate());
            outputProduct.setOutput(output);

            outputProductRepository.save(outputProduct);
        }

        return new Result("Output updated -> id: " + id, true);
    }

    public Result deleteOutput(Integer id) {
        var outputOpt = outputRepository.findById(id);
        if (outputOpt.isEmpty()) return new Result("Output not found", false);

        outputProductRepository.deleteByProductId(id);
        outputRepository.deleteById(id);

        return new Result("Output deleted -> id: " + id, true);
    }

    private OutputResponseDto mapOutputToResponseDto(Output output) {
        OutputResponseDto dto = new OutputResponseDto();
        dto.setId(output.getId());
        dto.setDate(output.getDate().toLocalDateTime().toString());
        dto.setWarehouseId(output.getWarehouse().getId());
        dto.setWarehouseName(output.getWarehouse().getName());
        dto.setCurrencyCode(output.getCurrency().getName());
        dto.setFactureNumber(output.getFactureNumber());
        dto.setCode(output.getCode());
        dto.setClientName(output.getClient().getName());
        dto.setClientPhoneNumber(output.getClient().getPhoneNumber());

        List<OutputProduct> outputProducts = outputProductRepository.findAllByOutputId(output.getId());
        List<OutputProductResponseDto> productDtos = new ArrayList<>();
        for (OutputProduct outputProduct : outputProducts) {
            productDtos.add(mapOutputProductToDto(outputProduct));
        }
        dto.setProducts(productDtos);

        return dto;
    }

    private OutputProductResponseDto mapOutputProductToDto(OutputProduct outputProduct) {
        OutputProductResponseDto dto = new OutputProductResponseDto();
        Product product = outputProduct.getProduct();

        dto.setId(outputProduct.getId());
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductCode(product.getCode());
        dto.setPhotoUrl(generatePhotoUrl(product.getAttachment().getId()));
        dto.setAmount(outputProduct.getAmount());
        dto.setPrice(outputProduct.getPrice());
        dto.setExpireDate(outputProduct.getExpireDate().toLocalDate().toString());
        dto.setCategoryName(product.getCategory().getName());
        dto.setMeasurementName(product.getMeasurement().getName());

        return dto;
    }

    private String generatePhotoUrl(Integer attachmentId) {
        return "/attachment/photos/" + attachmentId;
    }

    private String generateCode() {
        return outputRepository.findTopByCodeAsNumberDesc()
                .map(output -> {
                    try {
                        int maxCode = Integer.parseInt(output.getCode());
                        return String.valueOf(maxCode + 1);
                    } catch (NumberFormatException e) {
                        return "1";
                    }
                })
                .orElse("1");
    };

    private ValidationResult validateOutputRequest(OutputRequestDto dto) {
        if (dto.getOutputProducts() == null || dto.getOutputProducts().isEmpty()) {
            return ValidationResult.invalid(ERR_INVALID_PRODUCTs);
        }

        Optional<Client> client = clientRepository.findById(dto.getClientId());
        if (client.isEmpty()) {
            return ValidationResult.invalid(ERR_NOT_FOUND.formatted("Client", dto.getClientId()));
        }

        Optional<Currency> currency = currencyRepository.findById(dto.getCurrencyId());
        if (currency.isEmpty()) {
            return ValidationResult.invalid(ERR_NOT_FOUND.formatted("Currency", dto.getCurrencyId()));
        }

        Optional<Warehouse> warehouse = warehouseRepository.findById(dto.getWarehouseId());
        if (warehouse.isEmpty()) {
            return ValidationResult.invalid(ERR_NOT_FOUND.formatted("Warehouse", dto.getWarehouseId()));
        }

        for (var productDto : dto.getOutputProducts()) {
            Optional<Product> product = productRepository.findById(productDto.getProductId());
            if (product.isEmpty()) {
                return ValidationResult.invalid(ERR_NOT_FOUND.formatted("Product", productDto.getProductId()));
            }
        }

        return ValidationResult.valid();
    }

    private static class ValidationResult {
        private final boolean isValid;
        private final String message;

        private ValidationResult(boolean isValid, String message) {
            this.isValid = isValid;
            this.message = message;
        }

        static ValidationResult valid() {
            return new ValidationResult(true, null);
        }

        static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }

        boolean isValid() {
            return isValid;
        }

        String getMessage() {
            return message;
        }
    }


}
