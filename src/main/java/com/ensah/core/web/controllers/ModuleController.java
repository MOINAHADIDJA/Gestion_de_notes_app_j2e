package com.ensah.core.web.controllers;


import com.ensah.core.bo.*;
import com.ensah.core.bo.Module;
import com.ensah.core.services.impl.ModuleServiceImpl;
import com.ensah.core.utils.ExcelExporter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Data
@Controller
@RequestMapping("/fichier")
@AllArgsConstructor
@NoArgsConstructor
public class ModuleController {
    @Autowired
    public ModuleServiceImpl moduleService;
    long idModule;
    public  static int  nbreElement;
    public static List<Double> coeffList;
    public static int nbrEtudiants;
    public static String session;

    @RequestMapping(value = "searchStudents", method = RequestMethod.GET)
    public String searchStudentByModule(@RequestParam String titre, Model model) {

        Module mod = moduleService.moduleByTitre(titre);

        List<Etudiant> students = null;
        if (mod != null) {
            List<InscriptionAnnuelle> inscriptions = mod.getNiveau().getInscriptions();

            // recuperation des etudiants qui sont inscrits dans ce module
            students = new ArrayList<Etudiant>();
            for (InscriptionAnnuelle ia : inscriptions) {
                Boolean find = false;
                for(InscriptionModule im : ia.getInscriptionModules()){
                    if(im.getModule().getIdModule()== mod.getIdModule())
                        find=true;
                    if(find){
                        students.add(ia.getEtudiant());
                    }
                }

                }
           /* for (InscriptionAnnuelle i : inscriptions) {
                students.add(i.getEtudiant());
            }*/
        }

        model.addAttribute("studentList", students);

        // on recupere l id du module correspondant au titre
        idModule = mod.getIdModule();

        return "prof/userHomePage";
    }



    @GetMapping("/excel")
    public void exportExcelFile(HttpServletRequest request,HttpServletResponse response) throws IOException {

        Module module =moduleService.trouverModule(idModule);
        List<InscriptionAnnuelle> ia =  module.getNiveau().getInscriptions();
        List<Element> matieres = module.getElements();
        System.out.println(idModule);

        // preparation et remplissage des donnees  du fichier

        // entete de header
        String[] HeaderNames = new String[]{"Prof", "Module", "Annee", "Classe","Session"};
        String [][] data;

        // entete du body
        List<String> columnList = new ArrayList<>(){{add("ID");
            add("Cne");
            add("Nom");
            add("Prenom");

        }};

        // Recuperer la session choisie(normale ou rattrapage)
        session = request.getParameter("session");

       // System.out.println(session);

        String enseignant="";
        int nbrElt= 0;

        coeffList = new ArrayList<>();

        for(Element elt : matieres) {
            columnList.add(elt.getNom());

            coeffList.add(elt.getCurrentCoefficient());

            nbrElt++;
            if (matieres.size() > 1 ) {
                if (enseignant.equals(elt.getEnseignant().getNom() + " " + elt.getEnseignant().getPrenom()+"&")) {

                    enseignant = elt.getEnseignant().getNom() + " " + elt.getEnseignant().getPrenom();

                } else {
                    enseignant += elt.getEnseignant().getNom() + " " + elt.getEnseignant().getPrenom();

                    enseignant =enseignant+"&";
                }
            }
            else
                enseignant += elt.getEnseignant().getNom() + " " + elt.getEnseignant().getPrenom();

        }
        nbreElement=nbrElt;
        columnList.add("Moyenne");
        columnList.add("Validation");
        String[] columnNames = new String[columnList.size()];
        columnList.toArray(columnNames);

       enseignant = enseignant.substring(0,enseignant.length()-2);



        String classe = module.getNiveau().getAlias();
        String nom_module= module.getTitre();
 // recuperation de l annee actuelle
        LocalDate current_date = LocalDate.now();
        int current_Year = current_date.getYear();
        String annee = current_Year-1+"/"+current_Year;

        // les donnees de l en-tete
        data= new String[][]{{enseignant,nom_module,annee,classe,session}};



//  remplir le corps du fichier avec la liste des etudiants et leurs infos
       String [][] etd = new String[30][10];
        int  j=0;
        nbrEtudiants=0;

        for (InscriptionAnnuelle ins : ia) {
            //System.out.println(ins.getInscriptionModules());
            Boolean find = false;
             for(InscriptionModule im : ins.getInscriptionModules()){
                 if(im.getModule().getIdModule()== module.getIdModule())
                     find=true;


             }


            if(ins.getAnnee()==current_Year  && find ) {
                nbrEtudiants++;

                Etudiant e = ins.getEtudiant();
                etd[j][0] = String.valueOf(e.getIdUtilisateur());
                etd[j][1] = e.getCne();
                etd[j][2] = e.getNom();
                etd[j][3] = e.getPrenom();
                j++;


            }

        }




        fileInfo(response);
        //On genere et exporte le fichier
        ExcelExporter ee = new ExcelExporter(HeaderNames, columnNames, data, etd, "feuiile1");
        ee.exportFile(response);



        }




        public void fileInfo (HttpServletResponse response)
        {
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);

        }

    }



