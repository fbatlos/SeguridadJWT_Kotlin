package com.es.jwtSecurityKotlin.controller

import com.es.jwtSecurityKotlin.model.Usuario
import com.es.jwtSecurityKotlin.security.TokenServices
import com.es.jwtSecurityKotlin.service.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    @Autowired
    private lateinit var usuarioService: UsuarioService

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var tokenServices: TokenServices

    /*
    MÉTODO PARA INSERTAR UN USUARIO
     */
    @PostMapping("/register")
    fun register(
        @RequestBody newUsuario: Usuario
    ) : ResponseEntity<Usuario?>? {

        // Comprobación mínima
        // -> La obviamos por ahora
        // Llamar al UsuarioService para insertar un usuario
        val usuario = usuarioService.registerUsuario(newUsuario)
        // Devolver el usuario insertado
        return ResponseEntity(usuario, HttpStatus.CREATED) // Cambiar null por el usuario insertado

    }

    @PostMapping("/login")
    fun login (
        @RequestBody usuario: Usuario
    ): ResponseEntity<Any?>? {
        val authentication: Authentication
        try {
             authentication= authenticationManager.authenticate(UsernamePasswordAuthenticationToken(usuario.username, usuario.password))
            println(authentication)
        }catch (e: AuthenticationException){
            return ResponseEntity(mapOf("mensaje" to "Credenciales incorrectas dude"), HttpStatus.BAD_REQUEST)
        }
        //Si pasamos la autenticación significa que estamos bien autenticados
        //Generamos token
        var token = ""
        //poner try
        token = tokenServices.generarToken(authentication)

        return ResponseEntity(mapOf("token" to token), HttpStatus.CREATED)
    }

}