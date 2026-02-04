package com.example.db_api_learning.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class TokenBlacklistService {
    // A thread-safe set to store blacklisted tokens
    private val blacklistedTokens = ConcurrentHashMap.newKeySet<String>()

    fun blacklistToken(token: String) {
        blacklistedTokens.add(token)
    }

    fun isBlacklisted(token: String): Boolean {
        return blacklistedTokens.contains(token)
    }
}