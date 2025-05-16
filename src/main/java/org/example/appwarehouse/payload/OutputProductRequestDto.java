package org.example.appwarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputProductRequestDto {
    private Integer productId;
    private Double amount;
    private Double price;
    private Date expireDate;
}
