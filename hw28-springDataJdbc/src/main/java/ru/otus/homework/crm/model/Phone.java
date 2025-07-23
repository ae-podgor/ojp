package ru.otus.homework.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.annotation.Nonnull;

@Table(name = "phone")
public record Phone(@Id @Column("id") Long id,
                    @Nonnull String number,
                    Long clientId) {}
