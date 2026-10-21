package com.example.lab1.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CosmicWordValidator Unit Tests")
class CosmicWordValidatorTest {

    private final CosmicWordValidator validator = new CosmicWordValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "Super Star Collar",
            "Milky Way Galaxy Toy",
            "Green Comet Treat",
            "Deep Nebula Cushion",
            "Full Moon Bell",
            "Cosmo Dust Mouse",
            "Solar Powered Laser",
            "Low Orbit Feeder"
    })
    @DisplayName("Returns true when string contains a cosmic keyword")
    void isValid_returnsTrue_whenKeywordPresent(String value) {
        assertTrue(validator.isValid(value, null));
    }

    @Test
    @DisplayName("Returns false when string does not contain any cosmic keyword")
    void isValid_returnsFalse_whenNoCosmicKeyword() {
        assertFalse(validator.isValid("Standard Cat Bowl", null));
        assertFalse(validator.isValid("Wooden stick", null));
    }

    @Test
    @DisplayName("Returns true for null or empty string so that @NotBlank can handle them")
    void isValid_returnsTrue_whenNullOrEmpty() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }
}
