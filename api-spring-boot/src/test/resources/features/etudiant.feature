Feature: Calcul de l'âge d'un étudiant

  Scenario: Étudiant né il y a 23 ans
    Given un étudiant avec la date de naissance "2002-04-07"
    When on calcule son âge
    Then l'âge retourné doit être cohérent avec la date de naissance "2002-04-07"