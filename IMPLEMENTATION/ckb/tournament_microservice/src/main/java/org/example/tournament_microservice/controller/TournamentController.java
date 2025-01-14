package org.example.tournament_microservice.controller;

import org.example.tournament_microservice.model.TournamentModel;
import org.example.tournament_microservice.service.TournamentRankingService;
import org.example.tournament_microservice.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class
TournamentController {
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private TournamentRankingService tournamentRankingService;
    @PostMapping("/createNewTournament")
    public ResponseEntity<String> createNewTournament(@RequestParam String name, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date subscriptionDate, @RequestParam String creator) {
            tournamentService.saveTournament(new TournamentModel(name, subscriptionDate, creator));
            return ResponseEntity.ok("Tournament created successfully");
    }
    @GetMapping("/getAllTournaments")
    public ResponseEntity<List<String>> getAllTournaments(@RequestParam String name) {
        System.out.println("Getting the tournaments");
        System.out.println(name);
        List<String> tournamentNames = tournamentService.getTournamentNamesByCreator(name);
        if (tournamentNames != null && !tournamentNames.isEmpty()) {
            return ResponseEntity.ok(tournamentNames);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/isTournamentEnded")
    public Boolean isTournamentEnded(@RequestParam String tournamentName) {
        // Retrieve the tournament using Optional
        Optional<TournamentModel> optionalTournament = tournamentService.getTournamentByName(tournamentName);

        // Check if the tournament is present and has ended
        return optionalTournament.map(TournamentModel::getEnded).orElse(false);
    }

    @GetMapping("/getAllTournamentsAbs")
    public ResponseEntity<List<String>> getAllTournamentsAbs() {
        System.out.println("Getting all the tournaments for students");
        List<String> tournamentNames = tournamentService.getTournaments();
        if (tournamentNames != null && !tournamentNames.isEmpty()) {
            return ResponseEntity.ok(tournamentNames);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/addStudent")
    public ResponseEntity<String> addStudent(@RequestParam String tourId, @RequestParam String studId) {
        return tournamentRankingService.addStudent(tourId, studId);
    }

    @GetMapping("/getAllSubscription")
    public ResponseEntity<List<String>> getTourIdsByStudId(@RequestParam String name) {
        List<String> tourIds = tournamentRankingService.findTourIdsByStudId(name);
        return ResponseEntity.ok(tourIds);
    }


    @GetMapping("/studScores")
    public List<Object[]> getStudAndScoreByTour(@RequestParam String tour) {
        return tournamentRankingService.getStudAndScoreByTour(tour);
    }

    @PostMapping("/endTournament")
    public ResponseEntity<String> endTournament(@RequestParam String tournament) {
        tournamentService.endTournament(tournament);
        return ResponseEntity.ok(tournament);
    }

    @PostMapping("/updateResultsBattle")
    public ResponseEntity<String> updateValues(
            @RequestParam String tour,
            @RequestBody List<Object[]> stud_scores) {


        System.out.println("tournament: " + tour);
        System.out.println("stud_scores: " + stud_scores);

        tournamentRankingService.updateScoreByTourAndStudent( stud_scores, tour);
        return ResponseEntity.ok("Values updated successfully");
    }
}
