package com.juancarlos.junit5app.models;

import com.juancarlos.junit5app.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assumptions.*;
import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {
    Cuenta cuenta;

    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeEach
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter) {
        this.cuenta = new Cuenta("JuanCarlos", new BigDecimal("1000.12345"));
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        System.out.println("Iniciando Metodo de prueba");
        testReporter.publishEntry("Ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName() + " con las etiquetas " + testInfo.getTags());
    }

    @AfterEach
    void finalizarMetodoTest() {
        System.out.println("Finalizando Metodo de prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("--- INICIANDO EL TEST ---");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("--- FINALIZANDO EL TEST ---");
    }

    @Tag("cuenta")
    @Nested
    @DisplayName("Probando atributos de la cuenta corriente")
    class CuentaTestNombreSaldo {

        @Test
        @DisplayName("Probando nombre")
        void testNombreCuenta() {
            testReporter.publishEntry(testInfo.getTags().toString());
            if (testInfo.getTags().contains("cuenta")) {
                System.out.println("contiene la etiqueta cuenta (prueba de testInfo)");
            }
//        cuenta.setPersona("JuanCarlos");
            String esperado = "JuanCarlos";
            String real = cuenta.getPersona();
            assertNotNull(real, () -> "La cuenta no puede ser nula");
            assertEquals(esperado, real, () -> "El nombre de la cuenta no es el que se esperaba: se esperaba " + esperado + " sin embargo fue " + real);
            assertTrue(real.equals("JuanCarlos"), () -> "nombre cuenta esperada debe ser igual a la real");

        }

        @Test
        @DisplayName("Probando saldo")
        void testSaldoCuenta() {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("Testeando referencias que sean iguales con metodo equals")
        void testReferenciaCuenta() {
            cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
            Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
            assertEquals(cuenta2, cuenta);

        }
    }

    @Nested
    class CuentaOperacionesTest {

        @Tag("cuenta")
        @Test
        void testDebidoCuenta() {
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

        @Tag("cuenta")
        @Test
        void testCreditoCuenta() {
            cuenta.credito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
        }

        @Tag("cuenta")
        @Tag("banco")
        @Test
        void testTransferirDineroCuenta() {
            cuenta = new Cuenta("John Doe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("JuanCarlos", new BigDecimal("1500.9898"));

            Banco banco = new Banco();
            banco.setNombre("Banco del Estado");
            banco.transferir(cuenta2, cuenta, new BigDecimal(500));
            assertEquals("1000.9898", cuenta2.getSaldo().toPlainString());
            assertEquals("3000", cuenta.getSaldo().toPlainString());
        }
    }

    @Nested
    class ExecptionTest {

        @Tag("cuenta")
        @Tag("error")
        @Test
        void dineroInsuficienteException() {
            Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
                cuenta.debito(new BigDecimal(1500));
            });
            String actual = exception.getMessage();
            String esperado = "Dinero Insuficiente";
            assertEquals(esperado, actual);
        }
    }

    @Nested
    class RelaccionCuentaBancoTest {

        @Tag("cuenta")
        @Tag("banco")
        @Test
        // @Disabled // Salta este test
        @DisplayName("Probando relaciones entre la cuenta y el banco con assertAll")
        void testRelacionBancoCuenta() {
            cuenta = new Cuenta("John Doe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("JuanCarlos", new BigDecimal("1500.9898"));

            Banco banco = new Banco();
            banco.addCuenta(cuenta);
            banco.addCuenta(cuenta2);


            banco.setNombre("Banco del Estado");
            banco.transferir(cuenta2, cuenta, new BigDecimal(500));
            assertAll(() -> assertEquals("1000.9898", cuenta2.getSaldo().toPlainString()),
                    () -> assertEquals("3000", cuenta.getSaldo().toPlainString()),
                    () -> assertEquals(2, banco.getCuentas().size()),
                    () -> assertEquals("Banco del Estado", cuenta.getBanco().getNombre()),
                    () -> assertEquals("JuanCarlos", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("JuanCarlos"))
                            .findFirst()
                            .get().getPersona()),
                    () -> assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getPersona().equals("JuanCarlos")))
            );
        }
    }


    @Nested
    class SistemaOperativoTest {

        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMax() {
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }
    }

    @Nested
    class JavaVersionTest {

        @Test
        @EnabledOnJre(JRE.JAVA_19)
        void SoloJdk19() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void SoloJdk17() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_17)
        void TestNoJdk17() {
        }
    }

    @Nested
    class SystemPropertiesTest {

        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((key, value) -> System.out.println(key + ": " + value));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "17.*")
        void testJavaVersion() {
            String version = System.getProperty("java.version");
            System.out.println(version);
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64() {
            String arch = System.getProperty("os.arch");
            System.out.println(arch);
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testNo64() {
            String arch = System.getProperty("os.arch");
            System.out.println(arch);
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "juanc")
        void testUsername() {
            String username = System.getProperty("user.name");
            System.out.println(username);
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }
    }

    @Nested
    class VariableAmbienteTest {

        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getEnv = System.getenv();
            getEnv.forEach((key, value) -> System.out.println(key + ": " + value));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*17.*")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "20")
        void testProcesadores() {
            System.out.println("Numero de processadores: " + Runtime.getRuntime().availableProcessors());
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisabled() {
        }

    }

    @Nested
    class AssumptionsTest {

        @Test
        @DisplayName("test Saldo Cuenta Dev")
        void testSaldoCuentaDev() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(esDev);

            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("test Saldo Cuenta Dev 2")
        void testSaldoCuentaDev2() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumingThat(esDev, () -> {
                assertNotNull(cuenta.getSaldo());
                assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
                assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
                assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            });
        }
    }

    @Nested
    class RepeticcionTest {

        @RepeatedTest(value = 5, name = "{displayName} - Repeticion numero {currentRepetition} de {totalRepetitions}")
        @DisplayName("Probando Repeticiones con RepeatedTest")
        void testDebidoCuentaRepetir(RepetitionInfo rep) {
            if (rep.getCurrentRepetition() == 3) {
                System.out.println("Estamos en la repeticion " + rep.getCurrentRepetition());
            }

            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

    }

    @Tag("param")
    @Nested
    class PruebasParametrizadasTest {

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "400", "500", "700", "1000"})
        @DisplayName("Probando Repeticiones con ValueSource")
        void testDebidoCuentaValueSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"1,100", "2,200", "3,300", "4,400", "5,500", "6,700", "7,1000"})
        @DisplayName("Probando Repeticiones con CsvSource")
        void testDebidoCuentaCsvSource(String index, String monto) {
            System.out.println(index + "-> " + monto);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0}")
        @CsvSource({"200,100,JuanCarlos,JuanCarlos", "250,200,Jose,Jose", "350,300,Maria,Maria", "450,400,Lucas,Lucas", "510,500,Francisco,Francisco", "750,700, Manuel,Manuel", "1000.12345,1000, Salvador,Salvador"})
        @DisplayName("Probando Repeticiones con CsvSource2")
        void testDebidoCuentaCsvSource2(String saldo, String monto, String esperado, String actual) {
            System.out.println(saldo + "-> " + monto);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);

            assertNotNull(cuenta.getSaldo());
            assertNotNull(cuenta.getPersona());
            assertEquals(esperado, actual);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        @DisplayName("Probando Repeticiones con CsvFileSource")
        void testDebidoCuentaCsvFileSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        @DisplayName("Probando Repeticiones con CsvFileSource2")
        void testDebidoCuentaCsvFileSource2(String saldo, String monto, String esperado, String actual) {
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);

            assertNotNull(cuenta.getSaldo());
            assertNotNull(cuenta.getPersona());
            assertEquals(esperado, actual);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @MethodSource("montoList")
        @DisplayName("Probando Repeticiones con MethodSource")
        void testDebidoCuentaMethodSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        static List<String> montoList() {
            return Arrays.asList("100", "200", "300", "400", "500", "700", "1000");
        }
    }

    @Nested
    @Tag("timeout")
    class EjemploTimeOutTest {
        @Test
        @Timeout(2)
        void pruebaTimeout() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

        @Test
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
        void pruebaTimeout2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(900);
        }

        @Test
        void testTimeOutAssertions() {
            assertTimeout(Duration.ofSeconds(5), () -> {
                TimeUnit.MILLISECONDS.sleep(4000);
            });

        }
    }

}

