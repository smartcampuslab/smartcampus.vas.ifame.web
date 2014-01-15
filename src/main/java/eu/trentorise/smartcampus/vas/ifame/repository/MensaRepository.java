package eu.trentorise.smartcampus.vas.ifame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.vas.ifame.model.Mensa;

@Repository
public interface MensaRepository extends JpaRepository<Mensa, String> {

}
