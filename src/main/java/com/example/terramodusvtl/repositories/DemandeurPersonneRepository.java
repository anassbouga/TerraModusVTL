package com.example.terramodusvtl.repositories;
import com.example.terramodusvtl.entities.devisPersonne.DemandeurPersonne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeurPersonneRepository extends JpaRepository<DemandeurPersonne,Long> {
}
