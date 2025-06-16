package dev.hugog.minecraft.blockstreet.commands.validators;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CompanyRiskArgumentParserTest {

    @Test
    void isValidReturnsTrueForValidRiskLevel() {
        CompanyRiskArgumentParser parser = new CompanyRiskArgumentParser("3");
        assertTrue(parser.isValid());
    }

    @Test
    void isValidReturnsFalseForInvalidRiskLevel() {
        CompanyRiskArgumentParser parser = new CompanyRiskArgumentParser("0");
        assertFalse(parser.isValid());
    }

    @Test
    void isValidReturnsFalseForNonNumericInput() {
        CompanyRiskArgumentParser parser = new CompanyRiskArgumentParser("abc");
        assertFalse(parser.isValid());
    }

    @Test
    void isValidReturnsFalseForEmptyString() {
        CompanyRiskArgumentParser parser = new CompanyRiskArgumentParser("");
        assertFalse(parser.isValid());
    }

    @Test
    void parseReturnsOptionalWithParsedValueForValidRiskLevel() {
        CompanyRiskArgumentParser parser = new CompanyRiskArgumentParser("2");
        assertEquals(Optional.of(2), parser.parse());
    }

    @Test
    void parseReturnsEmptyOptionalForInvalidRiskLevel() {
        CompanyRiskArgumentParser parser = new CompanyRiskArgumentParser("7");
        assertEquals(Optional.empty(), parser.parse());
    }

    @Test
    void parseReturnsEmptyOptionalForNonNumericInput() {
        CompanyRiskArgumentParser parser = new CompanyRiskArgumentParser("xyz");
        assertEquals(Optional.empty(), parser.parse());
    }

    @Test
    void parseReturnsEmptyOptionalForEmptyString() {
        CompanyRiskArgumentParser parser = new CompanyRiskArgumentParser("");
        assertEquals(Optional.empty(), parser.parse());
    }
    
}