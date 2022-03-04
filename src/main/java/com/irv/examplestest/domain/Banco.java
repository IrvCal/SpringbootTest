package com.irv.examplestest.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Banco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//este jala para h2
//    @GeneratedValue(generator = "UUID")//SIN ESTO NO JALA, hay que especificar el tipo de valor generado este jala pra MYSQL
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private Long id;
    private String nombre;
    private int totalTransferencias=0;
}
