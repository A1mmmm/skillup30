package com.skillup30.controller;

import com.skillup30.dto.*;
import com.skillup30.service.CheckInService;
import com.skillup30.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для работы с профилем пользователя")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final UserService userService;
    private final CheckInService checkInService;

    @GetMapping("/me")
    @Operation(summary = "Получить профиль", description = "Возвращает профиль текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль получен", 
                    content = @Content(schema = @Schema(implementation = UserProfileDto.class)))
    })
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getProfile(userDetails.getUsername()));
    }

    @GetMapping("/me/challenges")
    @Operation(summary = "Получить челленджи пользователя", description = "Возвращает все челленджи пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Челленджи получены", 
                    content = @Content(schema = @Schema(implementation = UserChallengeDto.class)))
    })
    public ResponseEntity<List<UserChallengeDto>> getUserChallenges(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserChallenges(userDetails.getUsername()));
    }

    @GetMapping("/me/today-challenges")
    @Operation(summary = "Получить челленджи на сегодня", description = "Возвращает активные челленджи для отметки на сегодня")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Челленджи на сегодня получены", 
                    content = @Content(schema = @Schema(implementation = UserChallengeDto.class)))
    })
    public ResponseEntity<List<UserChallengeDto>> getTodayChallenges(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getTodayChallenges(userDetails.getUsername()));
    }

    @GetMapping("/progress")
    @Operation(summary = "Получить прогресс", description = "Возвращает общий прогресс пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Прогресс получен", 
                    content = @Content(schema = @Schema(implementation = UserProgressDto.class)))
    })
    public ResponseEntity<List<UserProgressDto>> getProgress(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getProgress(userDetails.getUsername()));
    }

    @PostMapping("/check-in/{challengeId}")
    @Operation(summary = "Отметить день", description = "Отмечает выполнение задания на сегодня")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "День отмечен", 
                    content = @Content(schema = @Schema(implementation = CheckInResponse.class))),
            @ApiResponse(responseCode = "409", description = "День уже отмечен")
    })
    public ResponseEntity<CheckInResponse> checkIn(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long challengeId) {
        return ResponseEntity.ok(checkInService.checkIn(userDetails.getUsername(), challengeId));
    }
}