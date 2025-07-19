package ru.otus.homework.crm.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.homework.crm.model.Address;

public interface AddressRepository extends ListCrudRepository<Address, Long> {


}
