package org.example.appwarehouse.service;

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
public class OutputService {
    @Autowired
    private OutputRepository outputRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private OutputProductRepository outputProductRepository;


    public List<OutputResponseDto> getOutputs() {
        List<OutputResponseDto> responseDtoList = new ArrayList<>();
        List<Output> ouputList = outputRepository.findAll();
        for (Output output : ouputList) {
            OutputResponseDto outputResponseDto = new OutputResponseDto();
            outputResponseDto.setId(output.getId());
            outputResponseDto.setDate(output.getDate().toLocalDateTime().toString());
            outputResponseDto.setWarehouseId(output.getWarehouse().getId());
            outputResponseDto.setWarehouseName(output.getWarehouse().getName());
            outputResponseDto.setCurrencyCode(output.getCurrency().getName());
            outputResponseDto.setFactureNumber(output.getFactureNumber());
            outputResponseDto.setCode(output.getCode());
            outputResponseDto.setClientName(output.getClient().getName());
            outputResponseDto.setClientPhoneNumber(output.getClient().getPhoneNumber());
            List<OutputProductResponseDto> outputProductResponseDtoList = new ArrayList<>();
            List<OutputProduct> outputProducts = outputProductRepository.findAllByOutputId(output.getId());
            for (OutputProduct outputProduct : outputProducts) {
                OutputProductResponseDto outputProductResponseDto = new OutputProductResponseDto();
                outputProductResponseDto.setId(outputProduct.getId());
                outputProductResponseDto.setProductId(outputProduct.getProduct().getId());
                outputProductResponseDto.setProductName(outputProduct.getProduct().getName());
                outputProductResponseDto.setProductCode(outputProduct.getProduct().getCode());
                outputProductResponseDto.setPhotoUrl(generatePhotoUrl(outputProduct.getProduct().getAttachment().getId()));
                outputProductResponseDto.setAmount(outputProduct.getAmount());
                outputProductResponseDto.setPrice(outputProduct.getPrice());
                outputProductResponseDto.setExpireDate(outputProduct.getExpireDate().toLocalDate().toString());
                outputProductResponseDto.setCategoryName(outputProduct.getProduct().getCategory().getName());
                outputProductResponseDto.setMeasurementName(outputProduct.getProduct().getMeasurement().getName());
                outputProductResponseDtoList.add(outputProductResponseDto);
            }
            outputResponseDto.setProducts(outputProductResponseDtoList);
            responseDtoList.add(outputResponseDto);
        }
        return responseDtoList;
    };

    public OutputResponseDto getOutputById(Integer id) {
        OutputResponseDto outputResponseDto = new OutputResponseDto();
        Optional<Output> outputOptional = outputRepository.findById(id);
        if (!outputOptional.isPresent()) {
            return outputResponseDto;
        }
        Output output = outputOptional.get();
        outputResponseDto.setId(output.getId());
        outputResponseDto.setDate(output.getDate().toLocalDateTime().toString());
        outputResponseDto.setWarehouseId(output.getWarehouse().getId());
        outputResponseDto.setWarehouseName(output.getWarehouse().getName());
        outputResponseDto.setCurrencyCode(output.getCurrency().getName());
        outputResponseDto.setFactureNumber(output.getFactureNumber());
        outputResponseDto.setCode(output.getCode());
        outputResponseDto.setClientName(output.getClient().getName());
        outputResponseDto.setClientPhoneNumber(output.getClient().getPhoneNumber());
        List<OutputProductResponseDto> outputProductResponseDtoList = new ArrayList<>();
        List<OutputProduct> outputProducts = outputProductRepository.findAllByOutputId(output.getId());
        for (OutputProduct outputProduct : outputProducts) {
            OutputProductResponseDto outputProductResponseDto = new OutputProductResponseDto();
            outputProductResponseDto.setId(outputProduct.getId());
            outputProductResponseDto.setProductId(outputProduct.getProduct().getId());
            outputProductResponseDto.setProductName(outputProduct.getProduct().getName());
            outputProductResponseDto.setProductCode(outputProduct.getProduct().getCode());
            outputProductResponseDto.setPhotoUrl(generatePhotoUrl(outputProduct.getProduct().getAttachment().getId()));
            outputProductResponseDto.setAmount(outputProduct.getAmount());
            outputProductResponseDto.setPrice(outputProduct.getPrice());
            outputProductResponseDto.setExpireDate(outputProduct.getExpireDate().toLocalDate().toString());
            outputProductResponseDto.setCategoryName(outputProduct.getProduct().getCategory().getName());
            outputProductResponseDto.setMeasurementName(outputProduct.getProduct().getMeasurement().getName());
            outputProductResponseDtoList.add(outputProductResponseDto);
        }
        outputResponseDto.setProducts(outputProductResponseDtoList);

        return outputResponseDto;
    };

    public Result addOutput(OutputRequestDto outputRequestDto) {
        Optional<Client> optionalClient = clientRepository.findById(outputRequestDto.getClientId());
        if (!optionalClient.isPresent()) {
            return new Result("Client not found",false);
        }

        Optional<Currency> currencyOptional = currencyRepository.findById(outputRequestDto.getCurrencyId());
        if (!currencyOptional.isPresent()) {
            return new Result("Currency not found",false);
        }

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(outputRequestDto.getWarehouseId());
        if (!optionalWarehouse.isPresent()) {
            return new Result("Warehouse not found",false);
        }

        if(outputRequestDto.getOutputProducts() == null || outputRequestDto.getOutputProducts().isEmpty()) {
            return new Result("Output products bo'sh bo'lmasligi", false);
        }

        Output output = new Output();
        output.setClient(optionalClient.get());
        output.setCurrency(currencyOptional.get());
        output.setWarehouse(optionalWarehouse.get());
        output.setDate(outputRequestDto.getDate());
        output.setCode(generateCode());
        output.setFactureNumber(outputRequestDto.getFactureNumber());
        Output saved = outputRepository.save(output);
        var outputProducts = outputRequestDto.getOutputProducts();
        for (var outputProduct : outputProducts) {
            OutputProduct outputProductDto = new OutputProduct();
            outputProductDto.setAmount(outputProduct.getAmount());
            outputProductDto.setPrice(outputProduct.getPrice());
            Optional<Product> optionalProduct = productRepository.findById(outputProduct.getProductId());
            if (!optionalProduct.isPresent()) {
                return new Result("Product not found",false);
            }
            outputProductDto.setProduct(optionalProduct.get());
            outputProductDto.setExpireDate(outputProduct.getExpireDate());
            outputProductDto.setOutput(saved);
            outputProductRepository.save(outputProductDto);
        }
        return new Result("Output added",true);
    }


    private String generatePhotoUrl(Integer attachmentId) {
        return "/attachment/photos/" + attachmentId;
    }

    @Transactional
    public Result updateOutput(Integer id, OutputRequestDto outputRequestDto) {
        Optional<Output> optionalOutput = outputRepository.findById(id);
        if (!optionalOutput.isPresent()) {
            return new Result("Output not found",false);
        };

        Optional<Client> optionalClient = clientRepository.findById(outputRequestDto.getClientId());
        if (!optionalClient.isPresent()) {
            return new Result("Client not found",false);
        }

        Optional<Currency> currencyOptional = currencyRepository.findById(outputRequestDto.getCurrencyId());
        if (!currencyOptional.isPresent()) {
            return new Result("Currency not found",false);
        }

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(outputRequestDto.getWarehouseId());
        if (!optionalWarehouse.isPresent()) {
            return new Result("Warehouse not found",false);
        }

        if(outputRequestDto.getOutputProducts() == null || outputRequestDto.getOutputProducts().isEmpty()) {
            return new Result("Output products bo'sh bo'lmasligi", false);
        };
        Output output = optionalOutput.get();
        output.setId(id);
        output.setClient(optionalClient.get());
        output.setCurrency(currencyOptional.get());
        output.setWarehouse(optionalWarehouse.get());
        output.setDate(outputRequestDto.getDate());
        output.setCode(output.getCode());
        output.setFactureNumber(outputRequestDto.getFactureNumber());
        Output saved = outputRepository.save(output);
        int deletedOutputProductCount = outputProductRepository.deleteByProductId(id);
        if (deletedOutputProductCount > 0) {
            var outputProducts = outputRequestDto.getOutputProducts();
            for (var outputProduct : outputProducts) {
                OutputProduct outputProductDto = new OutputProduct();
                outputProductDto.setAmount(outputProduct.getAmount());
                outputProductDto.setPrice(outputProduct.getPrice());
                Optional<Product> optionalProduct = productRepository.findById(outputProduct.getProductId());
                if (!optionalProduct.isPresent()) {
                    return new Result("Product not found",false);
                }
                outputProductDto.setProduct(optionalProduct.get());
                outputProductDto.setExpireDate(outputProduct.getExpireDate());
                outputProductDto.setOutput(saved);
                outputProductRepository.save(outputProductDto);
            }
        }
     return new Result("Output updated -> id: " + id,true);
    }

    public Result deleteOutput(Integer id) {
        Optional<Output> outputOptional = outputRepository.findById(id);
        if (!outputOptional.isPresent()) {
            return new Result("Output not found",false);
        }
        int deleted = outputProductRepository.deleteByProductId(id);
        System.out.println("BINGO. "+deleted);
        if (deleted > 0) {
            outputRepository.deleteById(id);
            return new Result("Output deleted",true);
        }
        return new Result("Output deleted -> id: " + id,true);
    }

    private String generateCode() {
        Optional<Output> outputOptional = outputRepository.findTopByCodeAsNumberDesc();
        if (outputOptional.isPresent()) {
            String maxCodeStr = outputOptional.get().getCode();
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
