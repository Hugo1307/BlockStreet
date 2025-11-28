package dev.hugog.minecraft.blockstreet.commands.validators;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MaterialArgumentParserTest {

    @Test
    void isValid_returnsTrue_forUppercaseMaterial() {
        MaterialArgumentParser parser = new MaterialArgumentParser("STONE");

        assertTrue(parser.isValid(), "Expected STONE to be a valid material");
        Optional<Material> parsed = parser.parse();
        assertTrue(parsed.isPresent());
        assertEquals(Material.STONE, parsed.get());
    }

    @Test
    void isValid_returnsTrue_forLowercaseMaterial() {
        MaterialArgumentParser parser = new MaterialArgumentParser("stone");

        assertTrue(parser.isValid(), "Expected 'stone' to be a valid material (case-insensitive)");
        Optional<Material> parsed = parser.parse();
        assertTrue(parsed.isPresent());
        assertEquals(Material.STONE, parsed.get());
    }

    @Test
    void parse_returnsEmpty_forInvalidMaterial() {
        MaterialArgumentParser parser = new MaterialArgumentParser("NOT_A_MATERIAL");

        assertFalse(parser.isValid(), "Expected NOT_A_MATERIAL to be invalid");
        Optional<Material> parsed = parser.parse();
        assertFalse(parsed.isPresent(), "Expected parse() to return an empty Optional for invalid input");
    }

}