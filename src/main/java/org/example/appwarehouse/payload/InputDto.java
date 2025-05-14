package org.example.appwarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputDto {
    private Timestamp date;
    private Integer warehouseId;
    private Integer supplierId;
    private Integer currencyId;
    private String factureNumber;
    private String code;
    List<InputProductDto> inputProducts;
}
