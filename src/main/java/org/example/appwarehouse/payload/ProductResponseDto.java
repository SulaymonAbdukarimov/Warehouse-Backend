package org.example.appwarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.appwarehouse.entity.Category;
import org.example.appwarehouse.entity.Measurement;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private Integer id;
    private String name;
    private  boolean active;
    private Category category;
    private Measurement measurement;
    private  String code;
    private Integer  attachmentId;

    public ProductResponseDto(Integer id, String name, boolean active, Category category, Measurement measurement, String code) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.category = category;
        this.measurement = measurement;
        this.code = code;
    }
}
