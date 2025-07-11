package ru.otus.homework.crm.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address clonedAddress) {
        this.id = id;
        this.name = name;
        this.address = clonedAddress;
    }

    public Client(Long id, String name, Address clonedAddress, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = clonedAddress;
        this.phones = phones != null ? phones.stream()
                .map(phone -> new Phone(phone.getId(), phone.getNumber(), this))
                .collect(Collectors.toList())
                : new ArrayList<>();
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        Address origAddress = this.address;
        Address clonedAddress = origAddress != null ? new Address(origAddress.getId(), origAddress.getStreet()) : null;

        Client newClient = new Client(this.id, this.name, clonedAddress);
        List<Phone> phoneList = this.phones != null ? this.phones.stream()
                .map(phone -> new Phone(phone.getId(), phone.getNumber(), newClient))
                .collect(Collectors.toList())
                : new ArrayList<>();

        newClient.setPhones(phoneList);

        return newClient;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
