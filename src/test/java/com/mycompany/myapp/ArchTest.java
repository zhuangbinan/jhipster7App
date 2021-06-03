package com.mycompany.myapp;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.mycompany.myapp.domain.CompanyDept;
import com.mycompany.myapp.domain.CompanyDeptExtends;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ArchTest {
    @Test
    void t2(){
        String oldStr = "{1,2,3}";
        String newStr = "4";
        String substring = oldStr.substring(0 , oldStr.length() - 1);
        String result = substring + newStr + "}";
        System.out.println(result);

    }

    @Test
    void t1(){
        String str = "0,100,101,102";
        String oldStr = "0,100,101,102";
        String newStr = "0,100,202,102";
        String replace = str.replace(oldStr, newStr);
        System.out.println(replace);

    }

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.mycompany.myapp");

        noClasses()
            .that()
            .resideInAnyPackage("com.mycompany.myapp.service..")
            .or()
            .resideInAnyPackage("com.mycompany.myapp.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.mycompany.myapp.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
