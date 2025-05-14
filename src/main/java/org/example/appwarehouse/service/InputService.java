package org.example.appwarehouse.service;

import org.example.appwarehouse.entity.*;
import org.example.appwarehouse.payload.InputDto;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public Result addInput(InputDto inputDto) {

        Warehouse warehouse = warehouseRepository.findById(inputDto.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse topilmadi"));

        Supplier supplier = supplierRepository.findById(inputDto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier topilmadi"));

        Currency currency = currencyRepository.findById(inputDto.getCurrencyId())
                .orElseThrow(() -> new RuntimeException("Currency topilmadi"));

        if (inputDto.getInputProducts() == null || inputDto.getInputProducts().isEmpty()) {
            return new Result("Input products bo'sh bo'lmasligi kerak", false);
        }

        Input input = new Input();
        input.setDate(inputDto.getDate());
        input.setWarehouse(warehouse);
        input.setSupplier(supplier);
        input.setCurrency(currency);
        input.setFactureNumber(inputDto.getFactureNumber());
        input.setCode(generateCode());
        List<InputProduct> inputProducts = inputDto.getInputProducts().stream().map(ip -> {
            Product product =  productRepository.findById(ip.getProductId()).orElseThrow(()-> new RuntimeException("Product topilmadi "+ip.getProductId()));
            InputProduct inputProduct = new InputProduct();
            inputProduct.setProduct(product);
            inputProduct.setAmount(ip.getAmount());
            inputProduct.setPrice(ip.getPrice());
            inputProduct.setExpireDate(ip.getExpireDate());
            inputProduct.setInput(input);
            return inputProduct;
        }).collect(Collectors.toList());
        input.setInputProducts(inputProducts);
        Input saved = inputRepository.save(input);
        return new Result("Input saved",true,saved.getId());
    };

    public Result updateInput(Integer id, InputDto inputDto) {
        Input input = inputRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Input topilmadi"));

        Warehouse warehouse = warehouseRepository.findById(inputDto.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse topilmadi"));

        Supplier supplier = supplierRepository.findById(inputDto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier topilmadi"));

        Currency currency = currencyRepository.findById(inputDto.getCurrencyId())
                .orElseThrow(() -> new RuntimeException("Currency topilmadi"));

        if (inputDto.getInputProducts() == null || inputDto.getInputProducts().isEmpty()) {
            return new Result("Input products bo'sh bo'lmasligi kerak", false);
        }

        input.setDate(inputDto.getDate());
        input.setWarehouse(warehouse);
        input.setSupplier(supplier);
        input.setCurrency(currency);
        input.setFactureNumber(inputDto.getFactureNumber());

        inputProductRepository.deleteByInputId(input.getId());

        List<InputProduct> inputProducts = inputDto.getInputProducts().stream().map(ip -> {
            Product product = productRepository.findById(ip.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product topilmadi " + ip.getProductId()));
            InputProduct inputProduct = new InputProduct();
            inputProduct.setProduct(product);
            inputProduct.setAmount(ip.getAmount());
            inputProduct.setPrice(ip.getPrice());
            inputProduct.setExpireDate(ip.getExpireDate());
            inputProduct.setInput(input);
            return inputProduct;
        }).collect(Collectors.toList());

        input.setInputProducts(inputProducts);

        Input saved = inputRepository.save(input);
        return new Result("Input updated", true, saved.getId());
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

    public Input getInput(Integer id) {
        Optional<Input> optionalInput = inputRepository.findById(id);
        if (!optionalInput.isPresent()) {
            return new Input();
        }
        return optionalInput.get();
    }

    public List<Input> getInputProducts() {
        return inputRepository.findAll();
    }

    private String generateCode() {
        Optional<Input> optionalInput = inputRepository.findTopByOrderByCodeDesc();
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
