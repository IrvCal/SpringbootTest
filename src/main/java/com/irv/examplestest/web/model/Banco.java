package com.irv.examplestest.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Banco {
    private Long id;
    private String nombre;
    private Integer totalTransferir;
    private int totalTransferencias;
}
