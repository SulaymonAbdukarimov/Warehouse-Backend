package org.example.appwarehouse.service;

import org.example.appwarehouse.entity.Attachment;
import org.example.appwarehouse.entity.Category;
import org.example.appwarehouse.entity.Measurement;
import org.example.appwarehouse.entity.Product;
import org.example.appwarehouse.payload.ProductDto;
import org.example.appwarehouse.payload.ProductResponseDto;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    MeasurementRepository measurementRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private AttachmentContentRepository attachmentContentRepository;


    public Result addProduct(ProductDto productDto) {
        boolean existsedByNameAndCategoryId = productRepository.existsByNameAndCategoryId(productDto.getName(), productDto.getCategoryId());

        if (existsedByNameAndCategoryId) {
            return new Result("Bunday maxsulot ushbu kategoryada mavjud",false);
        }

        boolean existsByAttachmentId = productRepository.existsByAttachmentId(productDto.getPhotoId());
        if (existsByAttachmentId) {
            return new Result("Bu rasm boshqa productda bor iltimos boshqa rasm saqlen",false);
        }

        // check category
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new Result("Bunday kategoriya mavjud emas",false);
        };

        // check attachment
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(productDto.getPhotoId());
        if (!optionalAttachment.isPresent()) {
            return new Result("Bunday fayl mavjud emas",false);
        }


        //check measurement
        Optional<Measurement> optionalMeasurement = measurementRepository.findById(productDto.getMeasurementId());
        if (!optionalMeasurement.isPresent()) {
            return new Result("Bunday o'lchov birligi mavjud emas",false);
        }

        //save
        Product product = new Product();
        product.setName(productDto.getName());
        product.setCode(generateCode());
        product.setCategory(optionalCategory.get());
        product.setAttachment(optionalAttachment.get());
        product.setMeasurement(optionalMeasurement.get());
        productRepository.save(product);
        return new Result("Maxsulot saqlandi",true);
    };


    public Result updateProduct(Integer id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new Result("Bunday product mavjud emas", false);
        }

        Product existingProduct = optionalProduct.get();

        // Check if another product with the same name and category exists
        boolean existsByNameAndCategoryId = productRepository.existsByNameAndCategoryIdAndIdNot(
                productDto.getName(), productDto.getCategoryId(), id);
        if (existsByNameAndCategoryId) {
            return new Result("Bunday maxsulot ushbu kategoryada mavjud", false);
        }

        // Validate category
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new Result("Bunday kategoriya mavjud emas", false);
        }

        // Validate attachment
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(productDto.getPhotoId());
        if (!optionalAttachment.isPresent()) {
            return new Result("Bunday fayl mavjud emas", false);
        }

        if (!existingProduct.getAttachment().getId().equals(productDto.getPhotoId())) {
            attachmentContentRepository.deleteByAttachmentId(existingProduct.getAttachment().getId());
            attachmentRepository.delete(existingProduct.getAttachment());

            existingProduct.setAttachment(optionalAttachment.get());
        }


        // Validate measurement
        Optional<Measurement> optionalMeasurement = measurementRepository.findById(productDto.getMeasurementId());
        if (!optionalMeasurement.isPresent()) {
            return new Result("Bunday o'lchov birligi mavjud emas", false);
        }

        // Update and save product
        existingProduct.setName(productDto.getName());
        existingProduct.setCode(existingProduct.getCode());
        existingProduct.setCategory(optionalCategory.get());
        existingProduct.setAttachment(optionalAttachment.get());
        existingProduct.setMeasurement(optionalMeasurement.get());
        existingProduct.setActive(productDto.isActive());
        productRepository.save(existingProduct);

        return new Result("Maxsulot o'zgartirildi", true);
    }

    @Transactional
    public Result deleteProduct(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new Result("Bunday product mavjud emas", false);
        }

        Product product = optionalProduct.get();
        productRepository.delete(product);
        return new Result("Maxsulot o'chirildi", true);
    }

    public List<ProductResponseDto> getAllProducts() {

      List<Product> products = productRepository.findAll();
      List<ProductResponseDto> productResponseDtos = new ArrayList<>();
      for (Product product : products) {
          ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(),product.getName(),product.isActive(),product.getCategory(),product.getMeasurement(),product.getCode());

          Optional<Attachment> optionalAttachment = attachmentRepository.findById(product.getAttachment().getId());
          if (optionalAttachment.isPresent()) {
              productResponseDto.setAttachmentId(optionalAttachment.get().getId());
          }
          productResponseDtos.add(productResponseDto);
      }
      return productResponseDtos;
    }

    public ProductResponseDto getProductById(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new ProductResponseDto();
        }
        Product product = optionalProduct.get();
        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(),product.getName(),product.isActive(),product.getCategory(),product.getMeasurement(),product.getCode());
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(product.getAttachment().getId());
        if (optionalAttachment.isPresent()) {
            productResponseDto.setAttachmentId(optionalAttachment.get().getId());
        }else {
            return new ProductResponseDto();
        }
        return productResponseDto;
    }

    private String generateCode() {
        Optional<Product> optionalProduct = productRepository.findTopByCodeAsNumberDesc();
        if (optionalProduct.isPresent()) {
            String maxCodeStr = optionalProduct.get().getCode();
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
