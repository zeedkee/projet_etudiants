package simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class EtudiantSimulation extends Simulation {

    // Protocole HTTP — pointe vers l'API Gateway
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    // Scénario 1 : Lecture (GET) — 50 utilisateurs en 30 secondes
    ScenarioBuilder readScenario = scenario("Lecture des étudiants")
            .exec(
                http("GET /api/etudiants")
                    .get("/api/etudiants")
                    .check(status().is(200))
            )
            .pause(1);

    // Scénario 2 : Écriture (POST) — 20 utilisateurs en 30 secondes
    ScenarioBuilder writeScenario = scenario("Création d'étudiants")
            .exec(
                http("POST /api/etudiants")
                    .post("/api/etudiants")
                    .body(StringBody(
                        "{\"cin\":\"GATL#{randomInt()}\",\"nom\":\"Gatling User\"," +
                        "\"dateNaissance\":\"2000-01-01\",\"email\":\"gatling@test.tn\"," +
                        "\"anneePremiereInscription\":2023,\"departementId\":1}"
                    ))
                    .check(status().is(201))
            )
            .pause(1);

    // Configuration : injection des utilisateurs
    {
        setUp(
            readScenario.injectOpen(
                rampUsers(50).during(Duration.ofSeconds(30))
            ),
            writeScenario.injectOpen(
                rampUsers(20).during(Duration.ofSeconds(30))
            )
        ).protocols(httpProtocol);
    }
}