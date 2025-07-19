package ru.otus.homework.crm.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.homework.crm.model.Phone;

public interface PhoneRepository extends ListCrudRepository<Phone, Long> {

}
