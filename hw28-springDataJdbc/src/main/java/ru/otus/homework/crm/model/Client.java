package ru.otus.homework.crm.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@Table(name = "client")
public class Client implements Persistable<Long> {

    @Id @Column("id")
    private final Long id;

    private final String name;

    @MappedCollection(idColumn = "client_id", keyColumn = "id")
    private final Address address;

    @MappedCollection(idColumn = "client_id", keyColumn = "id")
    private final List<Phone> phones;

    @Transient
    private final boolean isNew;

    public Client(Long id, String name,  Address address, List<Phone> phones, boolean isNew) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.isNew = isNew;
    }

    @PersistenceCreator
    private Client(Long id, String name, Address address, List<Phone> phones) {
        this(id, name, address, phones, false);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }

}
