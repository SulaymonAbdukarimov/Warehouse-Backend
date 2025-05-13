package org.example.appwarehouse.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.appwarehouse.entity.template.AbsEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product  extends AbsEntity {

    @ManyToOne
    private Category category;

    @OneToOne
    private Attachment attachment;

    private  String code;

    @ManyToOne
    private Measurement measurement;

}
