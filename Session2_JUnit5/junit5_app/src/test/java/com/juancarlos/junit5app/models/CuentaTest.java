package com.juancarlos.junit5app.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("JuanCarlos", new BigDecimal("1000.12345"));
//        cuenta.setPersona("JuanCarlos");
        String esperado = "JuanCarlos";
        String real = cuenta.getPersona();
        assertEquals(esperado, real);
        assertTrue(real.equals("JuanCarlos"));

    }

}