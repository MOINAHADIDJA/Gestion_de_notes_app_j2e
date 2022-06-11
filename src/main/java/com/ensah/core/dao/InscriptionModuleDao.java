package com.ensah.core.dao;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.InscriptionModule;
import com.ensah.core.bo.Module;
import com.ensah.core.bo.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscriptionModuleDao  extends JpaRepository<InscriptionModule,Long> {
    //public List<Etudiant> studentByModule();
}
