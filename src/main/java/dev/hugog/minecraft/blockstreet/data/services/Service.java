package dev.hugog.minecraft.blockstreet.data.services;

import dev.hugog.minecraft.blockstreet.data.repositories.Repository;

public interface Service {

    Repository<?, ?> getRepository();

}
