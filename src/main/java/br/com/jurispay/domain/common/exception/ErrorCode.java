package br.com.jurispay.domain.common.exception;

/**
 * Enum de códigos de erro padronizados do sistema.
 * Facilita rastreamento e tratamento de erros de forma consistente.
 */
public enum ErrorCode {

    // Validação (400)
    VALIDATION_ERROR("VALIDATION_ERROR", "Erro de validação"),
    INVALID_VALUE("INVALID_VALUE", "Valor inválido"),
    REQUIRED_FIELD("REQUIRED_FIELD", "Campo obrigatório"),
    INVALID_DATE("INVALID_DATE", "Data inválida"),
    INVALID_AMOUNT("INVALID_AMOUNT", "Valor monetário inválido"),

    // Não encontrado (404)
    NOT_FOUND("NOT_FOUND", "Recurso não encontrado"),
    CUSTOMER_NOT_FOUND("CUSTOMER_NOT_FOUND", "Cliente não encontrado"),
    LOAN_NOT_FOUND("LOAN_NOT_FOUND", "Empréstimo não encontrado"),
    PAYMENT_NOT_FOUND("PAYMENT_NOT_FOUND", "Pagamento não encontrado"),
    DOCUMENT_NOT_FOUND("DOCUMENT_NOT_FOUND", "Documento não encontrado"),
    CREDIT_ANALYSIS_NOT_FOUND("CREDIT_ANALYSIS_NOT_FOUND", "Análise de crédito não encontrada"),

    // Regras de negócio (422)
    BUSINESS_RULE_VIOLATION("BUSINESS_RULE_VIOLATION", "Violação de regra de negócio"),
    CUSTOMER_NOT_APPROVED("CUSTOMER_NOT_APPROVED", "Cliente não aprovado na análise de crédito"),
    CUSTOMER_ALREADY_APPROVED("CUSTOMER_ALREADY_APPROVED", "Cliente já aprovado"),
    ANALYSIS_ALREADY_IN_REVIEW("ANALYSIS_ALREADY_IN_REVIEW", "Análise já está em andamento"),
    ANALYSIS_NOT_IN_REVIEW("ANALYSIS_NOT_IN_REVIEW", "Análise não está em andamento"),
    INCOMPLETE_DOCUMENT_CHECKLIST("INCOMPLETE_DOCUMENT_CHECKLIST", "Checklist de documentos incompleto"),
    DUPLICATE_CPF("DUPLICATE_CPF", "CPF já cadastrado"),
    INVALID_DOCUMENT_STATUS("INVALID_DOCUMENT_STATUS", "Status de documento inválido"),

    // Autenticação/Autorização (401/403)
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Credenciais inválidas"),
    UNAUTHORIZED("UNAUTHORIZED", "Não autorizado"),
    FORBIDDEN("FORBIDDEN", "Acesso negado");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}

