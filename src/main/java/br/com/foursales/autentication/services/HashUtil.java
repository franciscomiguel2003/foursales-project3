package br.com.foursales.autentication.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    /**
     * Converte uma string em um hash SHA-256 e retorna como array de bytes.
     * @param input String a ser convertida.
     * @return Array de bytes com 32 bytes (256 bits).
     * @throws RuntimeException Se o algoritmo SHA-256 não estiver disponível.
     */
    public static byte[] toSha256Bytes(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash SHA-256: " + e.getMessage());
        }
    }

    /**
     * Converte uma string em um hash SHA-256 e retorna como string hexadecimal.
     * @param input String a ser convertida.
     * @return String hexadecimal com 64 caracteres (32 bytes em hex).
     * @throws RuntimeException Se o algoritmo SHA-256 não estiver disponível.
     */
    public static String toSha256Hex(String input) {
        byte[] hash = toSha256Bytes(input);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}