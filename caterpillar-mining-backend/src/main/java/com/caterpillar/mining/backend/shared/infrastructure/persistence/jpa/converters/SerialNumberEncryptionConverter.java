package com.caterpillar.mining.backend.shared.infrastructure.persistence.jpa.converters;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * JPA attribute converter that transparently encrypts a {@link String} attribute at rest using
 * AES-256-GCM, and decrypts it back to plain text when the entity is loaded.
 * <p>
 * A fresh random 12-byte initialization vector (IV) is generated for every encryption, which
 * makes the resulting ciphertext non-deterministic: encrypting the same plain text twice never
 * produces the same stored value. This is a deliberate security choice - it prevents leaking
 * equality information about encrypted values to anyone with read access to the database - but
 * it also means encrypted columns cannot be compared for equality in SQL. Any business rule that
 * needs to compare values protected by this converter (e.g. uniqueness checks) must load
 * candidate rows through JPA and compare the decrypted attribute in memory, relying on the fact
 * that this converter runs transparently during entity hydration.
 * </p>
 * <p>
 * The stored value is {@code Base64(IV || ciphertext || authenticationTag)}, so no additional
 * column is required to keep track of the IV.
 * </p>
 * <p>
 * This converter is registered as a Spring bean ({@code @Component}) rather than relying on plain
 * {@code @Converter(autoApply = true)} instantiation, because it needs its secret key injected via
 * {@code @Value}. Spring Boot's Hibernate auto-configuration registers a Spring-aware bean
 * container with Hibernate, so Spring-managed {@link AttributeConverter} beans referenced from
 * {@code @Convert(converter = ...)} are resolved from the application context instead of being
 * instantiated with a no-arg constructor.
 * </p>
 *
 * @author Diego Vilca
 */
@Component
@Converter
public class SerialNumberEncryptionConverter implements AttributeConverter<String, String> {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTES = 12;
    private static final int GCM_TAG_LENGTH_BITS = 128;

    @Value("${mining.encryption.serial-number.secret-key}")
    private String secretKeyBase64;

    private SecretKeySpec secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Decodes the configured Base64 secret key into a usable AES key as soon as the bean is
     * constructed, so that a missing or malformed key fails fast at application startup rather
     * than on the first attempted encryption.
     */
    @PostConstruct
    public void initializeSecretKey() {
        var decodedKey = Base64.getDecoder().decode(secretKeyBase64);
        this.secretKey = new SecretKeySpec(decodedKey, "AES");
    }

    /**
     * Encrypts the given plain text value before it is persisted.
     *
     * @param plainText the plain text attribute value, or {@code null}
     * @return the Base64-encoded, encrypted value, or {@code null} if the input was {@code null}
     */
    @Override
    public String convertToDatabaseColumn(String plainText) {
        if (plainText == null) return null;
        try {
            var iv = new byte[IV_LENGTH_BYTES];
            secureRandom.nextBytes(iv);
            var cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv));
            var cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            var ivAndCipherText = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, ivAndCipherText, 0, iv.length);
            System.arraycopy(cipherText, 0, ivAndCipherText, iv.length, cipherText.length);
            return Base64.getEncoder().encodeToString(ivAndCipherText);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not encrypt attribute value.", exception);
        }
    }

    /**
     * Decrypts the given stored value when the entity is hydrated from the database.
     *
     * @param storedValue the Base64-encoded, encrypted value, or {@code null}
     * @return the decrypted plain text value, or {@code null} if the input was {@code null}
     */
    @Override
    public String convertToEntityAttribute(String storedValue) {
        if (storedValue == null) return null;
        try {
            var ivAndCipherText = Base64.getDecoder().decode(storedValue);
            var iv = Arrays.copyOfRange(ivAndCipherText, 0, IV_LENGTH_BYTES);
            var cipherText = Arrays.copyOfRange(ivAndCipherText, IV_LENGTH_BYTES, ivAndCipherText.length);
            var cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not decrypt attribute value.", exception);
        }
    }
}
