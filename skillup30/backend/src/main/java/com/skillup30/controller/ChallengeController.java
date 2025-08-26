package com.skillup30.controller;

import com.skillup30.dto.ChallengeDto;
import com.skillup30.dto.ChallengeDayDto;
import com.skillup30.dto.UserChallengeDto;
import com.skillup30.service.ChallengeService;
import com.skillup30.exception.ChallengeNotFoundException;
import com.skillup30.exception.UserNotFoundException;
import com.skillup30.exception.AlreadyJoinedChallengeException;
import com.skillup30.exception.AlreadyCheckedInException;
import com.skillup30.exception.UserNotJoinedChallengeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
@Tag(name = "Челленджи", description = "API для работы с челленджами")
public class ChallengeController {
        private final ChallengeService challengeService;

        @GetMapping
        @Operation(summary = "Получить все челленджи", description = "Возвращает список всех доступных челленджей")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Список челленджей получен", content = @Content(schema = @Schema(implementation = ChallengeDto.class)))
        })
        public ResponseEntity<List<ChallengeDto>> getAllChallenges() {
                return ResponseEntity.ok(challengeService.getAllChallenges());
        }

        @GetMapping("/{id}")
        @Operation(summary = "Получить челлендж по ID", description = "Возвращает детальную информацию о челлендже")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Челлендж найден", content = @Content(schema = @Schema(implementation = ChallengeDto.class))),
                        @ApiResponse(responseCode = "404", description = "Челлендж не найден")
        })
        public ResponseEntity<ChallengeDto> getChallenge(
                        @Parameter(description = "ID челленджа", example = "1") @PathVariable Long id) {
                return ResponseEntity.ok(challengeService.getChallenge(id));
        }

        @PostMapping("/{id}/join")
        @Operation(summary = "Присоединиться к челленджу", description = "Пользователь присоединяется к челленджу")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Успешно присоединились к челленджу"),
                        @ApiResponse(responseCode = "404", description = "Челлендж не найден"),
                        @ApiResponse(responseCode = "409", description = "Уже участвуете в этом челлендже")
        })
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<Void> joinChallenge(
                        @AuthenticationPrincipal UserDetails userDetails,
                        @Parameter(description = "ID челленджа", example = "1") @PathVariable Long id) {
                challengeService.joinChallenge(userDetails.getUsername(), id);
                return ResponseEntity.ok().build();
        }

        @GetMapping("/active")
        @Operation(summary = "Получить активные челленджи", description = "Возвращает челленджи, в которых участвует пользователь")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Активные челленджи получены", content = @Content(schema = @Schema(implementation = ChallengeDto.class)))
        })
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<List<ChallengeDto>> getActiveChallenges(
                        @AuthenticationPrincipal UserDetails userDetails) {
                return ResponseEntity.ok(challengeService.getActiveChallenges(userDetails.getUsername()));
        }

        @GetMapping("/{id}/plan")
        @Operation(summary = "Получить план челленджа", description = "Возвращает 30-дневный план челленджа")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "План челленджа получен", content = @Content(schema = @Schema(implementation = ChallengeDayDto.class))),
                        @ApiResponse(responseCode = "404", description = "Челлендж не найден")
        })
        public ResponseEntity<List<ChallengeDayDto>> getChallengePlan(
                        @Parameter(description = "ID челленджа", example = "1") @PathVariable Long id) {
                return ResponseEntity.ok(challengeService.getChallengePlan(id));
        }

        @PostMapping("/{id}/checkin")
        @Operation(summary = "Отметить день", description = "Отмечает выполнение задания на сегодня")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "День успешно отмечен"),
                        @ApiResponse(responseCode = "404", description = "Челлендж не найден"),
                        @ApiResponse(responseCode = "409", description = "День уже отмечен")
        })
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<Void> checkIn(
                        @AuthenticationPrincipal UserDetails userDetails,
                        @Parameter(description = "ID челленджа", example = "1") @PathVariable Long id) {
                challengeService.checkIn(userDetails.getUsername(), id);
                return ResponseEntity.ok().build();
        }

        @GetMapping("/{id}/user-progress")
        @Operation(summary = "Получить прогресс пользователя", description = "Возвращает прогресс пользователя в челлендже")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Прогресс получен"),
                        @ApiResponse(responseCode = "404", description = "Челлендж не найден"),
                        @ApiResponse(responseCode = "400", description = "Пользователь не участвует в челлендже")
        })
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<UserChallengeDto> getUserProgress(
                        @AuthenticationPrincipal UserDetails userDetails,
                        @Parameter(description = "ID челленджа", example = "1") @PathVariable Long id) {
                return ResponseEntity.ok(challengeService.getUserProgress(userDetails.getUsername(), id));
        }

        // Обработчики исключений
        @ExceptionHandler(ChallengeNotFoundException.class)
        public ResponseEntity<String> handleChallengeNotFound(ChallengeNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(e.getMessage());
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<String> handleUserNotFound(UserNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(e.getMessage());
        }

        @ExceptionHandler(UserNotJoinedChallengeException.class)
        public ResponseEntity<String> handleUserNotJoined(UserNotJoinedChallengeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(e.getMessage());
        }

        @ExceptionHandler(AlreadyJoinedChallengeException.class)
        public ResponseEntity<String> handleAlreadyJoined(AlreadyJoinedChallengeException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(e.getMessage());
        }

        @ExceptionHandler(AlreadyCheckedInException.class)
        public ResponseEntity<String> handleAlreadyCheckedIn(AlreadyCheckedInException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(e.getMessage());
        }
}