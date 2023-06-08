package com.example.terramodusvtl.repositories.devisSte;
import com.example.terramodusvtl.entities.devisSte.DevisSteAmiable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevisSteAmiableRepository extends JpaRepository<DevisSteAmiable,Long> {

}
