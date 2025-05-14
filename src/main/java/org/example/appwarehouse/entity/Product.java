package org.example.appwarehouse.entity;


import jakarta.persistence.*;
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

    @ManyToOne(optional = false)
    private Category category;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;

    private  String code;

    @ManyToOne(optional = false)
    private Measurement measurement;

}
