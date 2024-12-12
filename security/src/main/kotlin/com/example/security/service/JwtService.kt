package com.example.security.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    private val SECRET = "9f5e2c4a1bcb4c7f8e239dff5f53d5e213d4f4c8b1a6a3f7e8d5b4c8f5b4f5e4"
    private val SECRET_FOR_DATABASE = "9k213d4f4c8b1a6aRHGj6NNKEB5pj03WTZlCwW9R7Owjc1213d4f4c8b1a6a"

    fun generateToken(claims: Map<String, Any>, userDetails: UserDetails): String {
        return Jwts.builder()
            .claims(claims)
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 10))
            .signWith(getSecretKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token: String, details: UserDetails): Boolean {
        val username: String = extractUsername(token)

        return username == details.username && !isTokenEpired(token)
    }

    fun isTokenEpired(token: String): Boolean {
        return extractClaims(token, Claims::getExpiration).before(Date(System.currentTimeMillis()))
    }

    fun extractUsername(token: String): String {
        return extractClaims(token, Claims::getSubject)
    }

    fun <T> extractClaims(token: String, claimsResolver: (Claims) -> T): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private fun getSecretKey(): SecretKey {
        val keyBytes: ByteArray = Decoders.BASE64.decode(SECRET)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun getDbSecretKey(): SecretKey {
        val keyBytes: ByteArray = Decoders.BASE64.decode(SECRET_FOR_DATABASE)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun encodePassword(password: String): String {
        val claims: Map<String, Any> = mapOf()

        return Jwts.builder()
            .claims(claims)
            .subject(password)
            .signWith(SignatureAlgorithm.HS256, getDbSecretKey())
            .compact()
    }

    fun decodePassword(encodedPassword: String): String {
        try {
            val claims = Jwts
                .parser()
                .verifyWith(getDbSecretKey())
                .build()
                .parseSignedClaims(encodedPassword)
                .payload

            return claims.subject
        } catch (e: Exception) {
            throw RuntimeException("cannot decode password")
        }
    }
}