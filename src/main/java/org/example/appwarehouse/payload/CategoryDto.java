package org.example.appwarehouse.payload;

import lombok.Data;

@Data
public class CategoryDto {
    private String name;
    private Integer parentCategoryId;
    private boolean active;
}
