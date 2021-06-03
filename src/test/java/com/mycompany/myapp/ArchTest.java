package com.mycompany.myapp;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

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
