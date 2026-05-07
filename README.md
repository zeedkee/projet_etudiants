# Projet Gestion des Étudiants

Projet complet composé d'une API REST Spring Boot avec PostgreSQL, Redis, Swagger, Kubernetes, et deux applications mobiles.

## Structure du projet
/projet-etudiants/
├── api-spring-boot/              # API REST Spring Boot
│   ├── src/
│   │   ├── main/java/.../
│   │   │   ├── controller/       # Contrôleurs REST
│   │   │   ├── service/          # Logique métier
│   │   │   ├── repository/       # Accès données (JPA)
│   │   │   ├── entity/           # Entités JPA
│   │   │   ├── dto/              # Objets de transfert
│   │   │   ├── mapper/           # Conversion DTO <-> Entity
│   │   │   ├── config/           # Configuration (Cache, DataLoader)
│   │   │   └── exception/        # Gestion des erreurs
│   │   └── resources/
│   │       ├── static/index.html # Page web
│   │       └── application.properties
│   ├── Dockerfile
│   └── pom.xml
├── k8s/                          # Manifests Kubernetes
│   ├── etudiant-deployment.yaml
│   └── postgres-deployment.yaml
├── mobile-app-flutter/           # Application Flutter
├── mobile-app-reactnative/       # Application React Native
├── docker-compose.yml            # Orchestration Docker
└── README.md

## Technologies

- **Backend** : Spring Boot 3.4 (Java 21), Spring Web, Spring Data JPA, Lombok
- **Base de données** : PostgreSQL 16 (Docker)
- **Cache** : Redis 7 (Docker)
- **Documentation** : Swagger / OpenAPI 3 (springdoc)
- **Tests** : Cucumber (BDD), JUnit 5
- **Conteneurisation** : Docker, Docker Compose
- **Orchestration** : Kubernetes (K3S)
- **Mobile** : Flutter, React Native

## Lancer le Backend (Docker Compose)

```bash
docker compose up --build
```

- API : http://localhost:8080/api/etudiants
- Page web : http://localhost:8080
- Swagger UI : http://localhost:8080/swagger-ui.html

## Lancer sur Kubernetes (K3S)

```bash
# Installer K3S si pas encore fait
curl -sfL https://get.k3s.io | sh -

# Configurer kubectl
mkdir -p ~/.kube
sudo cp /etc/rancher/k3s/k3s.yaml ~/.kube/config
sudo chown $(id -u):$(id -g) ~/.kube/config
export KUBECONFIG=~/.kube/config

# Déployer
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/etudiant-deployment.yaml

# Accéder à l'API
curl http://localhost:30080/api/etudiants
```

## API Endpoints

### Étudiants

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | /api/etudiants | Liste tous les étudiants |
| GET | /api/etudiants?annee=2022 | Filtre par année d'inscription |
| GET | /api/etudiants/{id} | Récupère un étudiant |
| POST | /api/etudiants | Crée un étudiant |
| PUT | /api/etudiants/{id} | Met à jour un étudiant |
| DELETE | /api/etudiants/{id} | Supprime un étudiant |

### Départements

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | /api/departements | Liste tous les départements |
| GET | /api/departements/{id} | Récupère un département |
| POST | /api/departements | Crée un département |
| PUT | /api/departements/{id} | Met à jour un département |
| DELETE | /api/departements/{id} | Supprime un département |

## Lancer l'application Flutter

```bash
cd mobile-app-flutter
flutter pub get
flutter run
```

## Lancer l'application React Native

```bash
cd mobile-app-reactnative
npm install
npx react-native start
# Dans un autre terminal : npx react-native run-android
```

## Tests BDD

```bash
cd api-spring-boot
mvn test
```

## Gestion de projet — Jira

Le projet est organisé en méthodologie Scrum avec deux sprints.
- Sprint 1 : API REST de base + Docker + Mobile
- Sprint 2 : Enrichissement (age, BDD, Swagger, Redis, K8S)



## Branches Git

- `main` : Code stable de la Partie 1
- `version-2` : Enrichissements de la Partie 1
- `version-3` : Enrichissements de la Partie 2

## Conventions de Review

- Toute Pull Request doit être relue dans un délai de **48 heures**
- Au minimum **1 approbation** est requise avant le merge
- Les commentaires **bloquants** (request changes) doivent être résolus avant le merge
- Chaque PR doit référencer un ticket Jira dans la description
- Les tests doivent passer et le `docker-compose up --build` doit fonctionner
- Le reviewer vérifie : qualité du code, tests, documentation Swagger, et structure des packages

## Auteur

AWIDID MOHAMED — Projet universitaire