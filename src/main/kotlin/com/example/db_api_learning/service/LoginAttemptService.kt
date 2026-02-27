package com.example.db_api_learning.service

import com.example.db_api_learning.model.LoginAttempt
import com.example.db_api_learning.repository.LoginAttemptRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LoginAttemptService(
    private val loginAttemptRepository: LoginAttemptRepository
) {
//    ==================================No expired time for restart attempts=======================================
//    companion object {
//        private const val MAX_ATTEMPTS = 3
//        private const val LOCK_TIME_MINUTES = 15L
//    }
//
//    @Transactional
//    fun loginSucceeded(email: String) {
//        loginAttemptRepository.deleteByEmail(email)
//    }
//
//    @Transactional
//    fun loginFailed(email: String) {
//        val attempt = loginAttemptRepository.findByEmail(email)
//
//        if (attempt == null) {
//            loginAttemptRepository.save(
//                LoginAttempt(
//                    email = email,
//                    attempts = 1,
//                    lastAttempt = LocalDateTime.now()
//                )
//            )
//        } else {
//            val newAttempts = attempt.attempts + 1
//            val lockedUntil = if (newAttempts >= MAX_ATTEMPTS) {
//                LocalDateTime.now().plusMinutes(LOCK_TIME_MINUTES)
//            } else null
//
//            loginAttemptRepository.save(
//                attempt.copy(
//                    attempts = newAttempts,
//                    lockedUntil = lockedUntil,
//                    lastAttempt = LocalDateTime.now()
//                )
//            )
//        }
//    }

//    ==================================Reset attempts after 1 hour=============================================
    companion object {
        private const val MAX_ATTEMPTS = 3
        private const val LOCK_TIME_MINUTES = 15L
        private const val ATTEMPT_EXPIRY_HOURS = 1L  // Reset attempts after 1 hour
    }

    @Transactional
    fun loginSucceeded(email: String) {
        loginAttemptRepository.deleteByEmail(email)
    }

    @Transactional
    fun loginFailed(email: String) {
        val attempt = loginAttemptRepository.findByEmail(email)

        if (attempt == null) {
            // First failed attempt
            loginAttemptRepository.save(
                LoginAttempt(
                    email = email,
                    attempts = 1,
                    lastAttempt = LocalDateTime.now()
                )
            )
        } else {
            val hoursSinceLastAttempt = java.time.Duration.between(
                attempt.lastAttempt,
                LocalDateTime.now()
            ).toHours()
            val found = loginAttemptRepository.findByEmail(email)
            if (hoursSinceLastAttempt > ATTEMPT_EXPIRY_HOURS && found != null) {
                // Reset attempts - it's been too long
                found.email = email
                found.attempts = 1
                found.lastAttempt = LocalDateTime.now()
                loginAttemptRepository.save(found)
            } else {
                // Increment attempts
                val newAttempts = attempt.attempts + 1
                val lockedUntil = if (newAttempts >= MAX_ATTEMPTS) {
                    LocalDateTime.now().plusMinutes(LOCK_TIME_MINUTES)
                } else null

                loginAttemptRepository.save(
                    attempt.copy(
                        attempts = newAttempts,
                        lockedUntil = lockedUntil,
                        lastAttempt = LocalDateTime.now()
                    )
                )
            }
        }
    }

    fun isBlocked(email: String): Boolean {
        val attempt = loginAttemptRepository.findByEmail(email) ?: return false
        val lockedUntil = attempt.lockedUntil ?: return false

        // Check if lock has expired
        return LocalDateTime.now().isBefore(lockedUntil)
    }

    // NEW METHOD - Call this separately to clear expired locks after 15 minutes
    @Transactional
    fun clearExpiredLock(email: String) {
        val attempt = loginAttemptRepository.findByEmail(email) ?: return
        val lockedUntil = attempt.lockedUntil ?: return

        if (LocalDateTime.now().isAfter(lockedUntil)) {
            loginAttemptRepository.deleteByEmail(email)
        }
    }

    fun getRemainingAttempts(email: String): Int {
        val attempt = loginAttemptRepository.findByEmail(email) ?: return MAX_ATTEMPTS
        return MAX_ATTEMPTS - attempt.attempts
    }

    fun getBlockedUntil(email: String): LocalDateTime? {
        return loginAttemptRepository.findByEmail(email)?.lockedUntil
    }
}