package br.com.jurispay.application.common.util;

/**
 * Utilitário para mascaramento de dados sensíveis conforme LGPD.
 * 
 * <p>Esta classe fornece métodos estáticos para mascarar informações sensíveis
 * como CPF e chave PIX, garantindo que esses dados não sejam expostos em logs
 * ou respostas da API.</p>
 * 
 * <p><strong>Uso:</strong> Utilize estes métodos ao registrar logs ou retornar
 * respostas onde dados sensíveis não devem ser expostos na íntegra.</p>
 */
public final class SensitiveDataMasker {

    private SensitiveDataMasker() {
        // Classe utilitária - não deve ser instanciada
    }

    /**
     * Mascara um CPF, mantendo apenas os últimos 3 dígitos visíveis.
     * 
     * <p>Formato de saída: "***.***.890-1" ou "xxx.xxx.890-1"</p>
     * 
     * @param cpf CPF a ser mascarado (pode conter formatação)
     * @return CPF mascarado ou null se o CPF for nulo/vazio
     */
    public static String maskCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return null;
        }

        // Remove caracteres não numéricos
        String digitsOnly = cpf.replaceAll("[^0-9]", "");

        // Valida tamanho
        if (digitsOnly.length() != 11) {
            return "***********";
        }

        // Mascara mantendo últimos 3 dígitos
        String lastThree = digitsOnly.substring(8);
        return "***.***." + lastThree.substring(0, 3) + "-" + lastThree.substring(3);
    }

    /**
     * Mascara uma chave PIX, mantendo apenas os últimos 3 caracteres visíveis.
     * 
     * <p>Formato de saída: "***********abc"</p>
     * 
     * @param pixKey Chave PIX a ser mascarada
     * @return Chave PIX mascarada ou null se a chave for nula/vazia
     */
    public static String maskPixKey(String pixKey) {
        if (pixKey == null || pixKey.isEmpty()) {
            return null;
        }

        if (pixKey.length() <= 4) {
            return "****";
        }

        // Mostra apenas os últimos 3 caracteres
        String lastThree = pixKey.substring(pixKey.length() - 3);
        int maskLength = pixKey.length() - 3;
        return "*".repeat(maskLength) + lastThree;
    }
}

