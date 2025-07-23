package ru.otus.homework.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.annotation.Nonnull;

@Table(name = "address")
public record Address(@Id Long id,
                      @Nonnull String street,
                      Long clientId) {}
