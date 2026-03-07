package com.juancarlos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalificacionesTest {
    public CalificacionesTest() {
    }

    /**
     * Test of calificaciones method, of class Utils.
     */
    @Test
    public void testCalificaciones() {
        System.out.println("* JUnit4Test: testCalificaciones()");
        assertEquals(5, Calificaciones.calificaciones(3.5f, 2, true), 0.1);
        assertEquals(5.5, Calificaciones.calificaciones(3.5f, 2, false), 0.1);
        assertEquals(2.5, Calificaciones.calificaciones(2.5f, 0.5f, true), 0.1);
        assertEquals(-1, Calificaciones.calificaciones(-2, 1, false), 0.1);
        assertEquals(-1, Calificaciones.calificaciones(8, 1, true), 0.1);
        assertEquals(-1, Calificaciones.calificaciones(1, -2, false), 0.1);
        assertEquals(-1, Calificaciones.calificaciones(1, 4, true), 0.1);

    }

}