package com.ensah.core.services.impl;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.Niveau;
import com.ensah.core.bo.Utilisateur;
import com.ensah.core.dao.ModuleDao;
import  com.ensah.core.bo.Module;
import com.ensah.core.services.ModuleService;
import com.ensah.core.utils.ExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    ModuleDao moduleDao;

    public List<Module> moduleByNiveau(Niveau niveau){
        System.out.println(niveau.getIdNiveau()+" est le niveau");
           return moduleDao.findModulesByNiveau(niveau);
    }

    public Module moduleByTitre(String titre){
        return moduleDao.findModuleByTitre(titre);
    }
    public Module moduleByCode(String code){
        return moduleDao.findModuleByCode(code);
    }

    public Module trouverModule(long id) {
        return moduleDao.getById(id);
    }




}
