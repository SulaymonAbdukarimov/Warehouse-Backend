package org.example.appwarehouse.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputResponseDto {
    private Integer id;
    private String date;
    private Integer warehouseId;
    private String warehouseName;
    private Integer supplierId;
    private String supplierName;
    private String currencyCode;
    private String invoiceNumber;
    private String code;
    private List<InputProductResponseDto> inputProducts;
}
