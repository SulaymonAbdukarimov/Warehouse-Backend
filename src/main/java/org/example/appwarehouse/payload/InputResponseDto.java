package org.example.appwarehouse.payload;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.appwarehouse.entity.Currency;
import org.example.appwarehouse.entity.InputProduct;
import org.example.appwarehouse.entity.Supplier;
import org.example.appwarehouse.entity.Warehouse;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputResponseDto {
    private Integer id;

    private Timestamp date;

    private Warehouse warehouse;

    private Supplier supplier;


    private Currency currency;

    private String factureNumber;

    private String code;

    private List<InputProductResponseDto> inputProducts;

    public InputResponseDto(Integer id, Timestamp date, Warehouse warehouse, Supplier supplier, Currency currency, String factureNumber, String code) {
        this.id = id;
        this.date = date;
        this.warehouse = warehouse;
        this.supplier = supplier;
        this.currency = currency;
        this.factureNumber = factureNumber;
        this.code = code;
    }
}
