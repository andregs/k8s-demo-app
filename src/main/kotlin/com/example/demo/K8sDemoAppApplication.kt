package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class K8sDemoAppApplication

fun main(args: Array<String>) {
	runApplication<K8sDemoAppApplication>(*args)
}
