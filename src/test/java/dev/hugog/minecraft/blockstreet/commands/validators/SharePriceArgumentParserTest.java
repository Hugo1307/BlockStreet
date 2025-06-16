package dev.hugog.minecraft.blockstreet.commands.validators;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SharePriceArgumentParserTest {

    @Test
    void isValidReturnsTrueForValidPositiveDecimal() {
        SharePriceArgumentParser parser = new SharePriceArgumentParser("123.45");
        assertTrue(parser.isValid());
    }

    @Test
    void isValidReturnsTrueForValidNegativeDecimal() {
        SharePriceArgumentParser parser = new SharePriceArgumentParser("-123.45");
        assertTrue(parser.isValid());
    }

    @Test
    void isValidReturnsTrueForValidInteger() {
        SharePriceArgumentParser parser = new SharePriceArgumentParser("123");
        assertTrue(parser.isValid());
    }

    @Test
    void isValidReturnsFalseForInvalidCharacters() {
        SharePriceArgumentParser parser = new SharePriceArgumentParser("abc123");
        assertFalse(parser.isValid());
    }

    @Test
    void isValidReturnsFalseForEmptyString() {
        SharePriceArgumentParser parser = new SharePriceArgumentParser("");
        assertFalse(parser.isValid());
    }

    @Test
    void parseReturnsOptionalWithParsedValueForValidInput() {
        SharePriceArgumentParser parser = new SharePriceArgumentParser("123.45");
        assertEquals(Optional.of(123.45), parser.parse());
    }

    @Test
    void parseReturnsEmptyOptionalForInvalidInput() {
        SharePriceArgumentParser parser = new SharePriceArgumentParser("abc");
        assertEquals(Optional.empty(), parser.parse());
    }

    @Test
    void parseReturnsOptionalWithParsedValueForNegativeInput() {
        SharePriceArgumentParser parser = new SharePriceArgumentParser("-123.45");
        assertEquals(Optional.of(-123.45), parser.parse());
    }

    @Test
    void parseReturnsOptionalWithParsedValueForIntegerInput() {
        SharePriceArgumentParser parser = new SharePriceArgumentParser("123");
        assertEquals(Optional.of(123.0), parser.parse());
    }
    
}