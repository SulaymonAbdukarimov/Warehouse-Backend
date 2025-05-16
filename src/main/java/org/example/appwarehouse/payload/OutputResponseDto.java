package org.example.appwarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class OutputResponseDto {
    private Integer id;
    private String date;
    private Integer warehouseId;
    private String warehouseName;
    private String currencyCode;
    private String factureNumber;
    private String code;
    private String clientName;
    private String clientPhoneNumber;
    private List<OutputProductResponseDto> products;

}
