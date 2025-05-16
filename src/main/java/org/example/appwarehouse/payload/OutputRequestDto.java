package org.example.appwarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OutputRequestDto {
    private Timestamp date;
    private Integer warehouseId;
    private Integer currencyId;
    private String factureNumber;
    private Integer clientId;
    private List<OutputProductRequestDto> outputProducts;


    public OutputRequestDto(Timestamp date, Integer warehouseId, Integer currencyId, String factureNumber, Integer clientId) {
        this.date = date;
        this.warehouseId = warehouseId;
        this.currencyId = currencyId;
        this.factureNumber = factureNumber;
        this.clientId = clientId;
        this.outputProducts = new ArrayList<>();
    }
}
