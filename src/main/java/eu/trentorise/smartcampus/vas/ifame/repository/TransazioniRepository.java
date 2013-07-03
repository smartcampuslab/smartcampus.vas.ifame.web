package eu.trentorise.smartcampus.vas.ifame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.smartcampus.vas.ifame.model.Transazione;

@Repository
public interface TransazioniRepository extends JpaRepository<Transazione, Long> {

	List<Transazione> getUserTransactions(Long user_id);

}
