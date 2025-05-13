package org.example.appwarehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.appwarehouse.entity.template.AbsEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Supplier extends AbsEntity {
    @Column(unique = true, nullable = false)
    private  String phoneNumber;
}
