package com.ensah.core.services;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.Module;
import com.ensah.core.bo.Niveau;
import com.ensah.core.utils.ExcelExporter;

import java.util.List;
import java.util.Optional;

public interface ModuleService {

    public List<Module> moduleByNiveau(Niveau niveau);
    public Module trouverModule(long id);
    public Module moduleByTitre(String titre);
    public Module moduleByCode(String code);
}
