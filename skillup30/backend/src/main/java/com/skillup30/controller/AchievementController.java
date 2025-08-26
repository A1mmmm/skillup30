package com.skillup30.controller;

import com.skillup30.dto.AchievementDto;
import com.skillup30.service.AchievementService;
import com.skillup30.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
@Tag(name = "Достижения", description = "API для работы с достижениями пользователей")
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    @Operation(summary = "Получить достижения пользователя", description = "Возвращает достижения текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Достижения получены", 
                    content = @Content(schema = @Schema(implementation = AchievementDto.class)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<AchievementDto>> getUserAchievements(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(achievementService.getUserAchievements(userDetails.getUsername()));
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все достижения", description = "Возвращает список всех доступных достижений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Все достижения получены", 
                    content = @Content(schema = @Schema(implementation = AchievementDto.class)))
    })
    public ResponseEntity<List<AchievementDto>> getAllAchievements() {
        return ResponseEntity.ok(achievementService.getAllAchievements());
    }

    // Обработчики исключений
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}