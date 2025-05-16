package org.example.appwarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputProductResponseDto {
    private Integer id;
    private Integer productId;
    private String productName;
    private String productCode;
    private String photoUrl;
    private Double amount;
    private Double price;
    private String expireDate;
    private String categoryName;
    private String measurementName;
    };
