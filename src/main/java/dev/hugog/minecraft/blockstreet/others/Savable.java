package dev.hugog.minecraft.blockstreet.others;

public interface Savable<T> {

    void save();

    T load();

}
