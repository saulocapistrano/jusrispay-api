package br.com.jurispay.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de integração para validar que o contexto Spring sobe corretamente
 * com PostgreSQL e Liquibase aplica as migrações.
 */
@SpringBootTest
@Testcontainers
class LiquibaseSmokeIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("jurispay_test")
            .withUsername("jurispay")
            .withPassword("jurispay");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.liquibase.change-log", () -> "classpath:db/changelog/db.changelog-master.yaml");
    }

    private final DataSource dataSource;

    public LiquibaseSmokeIT(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Test
    void shouldLoadSpringContext() {
        // Se chegou aqui, o contexto Spring carregou com sucesso
        assertNotNull(dataSource, "DataSource deve estar disponível");
    }

    @Test
    void shouldHaveTablesCreatedByLiquibase() throws Exception {
        // Tabelas esperadas criadas pelo Liquibase
        Set<String> expectedTables = Set.of(
                "customer",
                "loan",
                "payment",
                "document"
        );

        Set<String> actualTables = new HashSet<>();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Buscar tabelas no schema público
            try (ResultSet tables = metaData.getTables(
                    connection.getCatalog(),
                    "public",
                    null,
                    new String[]{"TABLE"})) {
                
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    actualTables.add(tableName.toLowerCase());
                }
            }
        }

        // Verificar que todas as tabelas esperadas existem
        for (String expectedTable : expectedTables) {
            assertTrue(actualTables.contains(expectedTable),
                    "Tabela '" + expectedTable + "' deve existir após migração do Liquibase");
        }
    }
}

