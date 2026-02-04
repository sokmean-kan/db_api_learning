package com.example.db_api_learning.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class SecurityAppConfig {

//    @Bean
//    fun userDetailsService(userRepository: UserRepository): UserDetailsService =
//        CustomUserDetailService(userRepository)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(userRepository: UserDetailsService): AuthenticationProvider =
        // 2. Pass the service directly into the Constructor
        DaoAuthenticationProvider(userRepository)
        // 3. Set the encoder using the setter
            .also {
                it.setPasswordEncoder(passwordEncoder())
            }
//    @Bean
//    fun authenticationProvider(sokmeanUserDetailsService: UserDetailsService): AuthenticationProvider {
//        // Inject the UserDetailsService bean we defined above
//        val authProvider = DaoAuthenticationProvider(sokmeanUserDetailsService)
//        authProvider.setPasswordEncoder(passwordEncoder())
//        return authProvider
//    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}