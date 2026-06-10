package com.david.academysystem.dto.valiacoesfisicas;

import java.math.BigDecimal;
import java.util.UUID;

public interface AvaliacoesFisicasProjection {

    UUID getIdAluno();
    String getNomeAluno();
    UUID getIdAvaliacao();
    BigDecimal getPeso();
    BigDecimal getAltura();
    BigDecimal getPorcentagemGorduraCorporal();
}
