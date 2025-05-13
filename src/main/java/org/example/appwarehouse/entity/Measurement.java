package org.example.appwarehouse.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.appwarehouse.entity.template.AbsEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity

public class Measurement  extends AbsEntity {

}
