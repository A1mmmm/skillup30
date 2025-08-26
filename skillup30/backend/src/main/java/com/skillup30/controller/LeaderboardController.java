package com.skillup30.controller;

import com.skillup30.dto.LeaderboardEntryDto;
import com.skillup30.service.LeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
@Tag(name = "Лидерборд", description = "API для работы с рейтингом пользователей")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @GetMapping
    @Operation(summary = "Получить лидерборд", description = "Возвращает топ пользователей по уровню и активности")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Лидерборд получен", 
                    content = @Content(schema = @Schema(implementation = LeaderboardEntryDto.class)))
    })
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getLeaderboard());
    }
}