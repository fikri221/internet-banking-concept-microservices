[![CI For Concept Microservice](https://github.com/JavatoDev-com/internet-banking-concept-microservices/actions/workflows/gradle.yml/badge.svg)](https://github.com/JavatoDev-com/internet-banking-concept-microservices/actions/workflows/gradle.yml)

# Internet Banking Concept With Java Spring Boot Microservices

This source code was developed for Java based microservices tutorial series from [javatodev.com](https://javatodev.com).

In this article series I’m going to explain using internet banking API concept with spring boot based microserices architecture. Initially I’ll develop the core API which will evolve as a full fledged REST API collection until deployments.

### Releases

[1.0.0](https://github.com/JavatoDev-com/internet-banking-concept-microservices/releases/tag/v.1.0.0) - Initial release with Java 11 and Spring Boot 2.

### Layanan (Services) Dalam Proyek Ini

Proyek ini menggunakan arsitektur microservices yang terdiri dari beberapa layanan inti dan pendukung:

#### Layanan Inti (Core Services):
- **User Service (`internet-banking-user-service`)**: Mengelola data pengguna, pendaftaran, dan profil. Layanan ini terintegrasi dengan Keycloak untuk manajemen identitas dan menggunakan basis data untuk menyimpan informasi tambahan pengguna.
- **Core Banking Service (`core-banking-service`)**: Jantung dari sistem perbankan yang mensimulasikan operasional bank, termasuk manajemen akun dan pemrosesan transaksi dasar.
- **Fund Transfer Service (`internet-banking-fund-transfer-service`)**: Menangani proses transfer dana antar rekening dan mengirimkan pesan ke antrean (RabbitMQ) untuk notifikasi.
- **Utility Payment Service (`internet-banking-utility-payment-service`)**: Layanan khusus untuk memproses pembayaran tagihan (listrik, air, dll).

#### Infrastruktur & Pendukung (Infrastructure & Support):
- **Service Registry (`internet-banking-service-registry`)**: Menggunakan Netflix Eureka agar setiap microservice dapat saling menemukan satu sama lain secara dinamis.
- **Config Server (`internet-banking-config-server`)**: Menyediakan konfigurasi terpusat untuk semua microservice, memudahkan manajemen konfigurasi di berbagai lingkungan.
- **API Gateway (`internet-banking-api-gateway`)**: Menjadi gerbang tunggal untuk akses API, menangani routing permintaan ke layanan yang sesuai, serta keamanan.
- **Keycloak (`keycloak_web`)**: Server manajemen identitas dan akses (IAM) untuk menangani autentikasi dan otorisasi pengguna secara aman.
- **MySQL & PostgreSQL (`mysql_core_db` & `keycloakdb`)**: Basis data relasional yang digunakan oleh layanan inti dan Keycloak untuk penyimpanan data yang persisten.

### Base Project Architecture

<a href="#" target="blank">
    <img align="center" src="https://javatodev.com/content/images/wordpress/2021/05/Microservices-Article-Banking-Core-Concept-1024x870.png" 
alt="Spring Boot Microservices Project Architecture By Javatodev.com"/></a>

### Technology Stack

1. Java 21
2. Spring Boot 3.2.4
3. Spring Cloud 2023.0.0 
4. Netflix Eureka Service Registry
5. Netflix Eureka Service Client
6. Spring Cloud API Gateway
7. Spring Cloud Config Server
8. Zipkin
9. Spring Cloud Sleuth
10. Open Feign
11. RabbitMQ
12. Prometheus 
13. MySQL 
14. Keycloak 
15. Docker / Docker Compose 
16. Kubernetes 
17. Keycloak

Article series 

[1. Building Microservices With Spring Boot – Free Course With Practical Project](https://javatodev.com/building-microservices-with-spring-boot-free-course-with-practical-project/)

[2. Microservices – Service Registration and Discovery With Spring Cloud Netflix Eureka](https://javatodev.com/microservices-service-registration-and-discovery-with-spring-cloud-netflix-eureka/)

[3. Microservices – Setup API Gateway Using Spring Cloud Gateway](https://javatodev.com/microservices-setup-api-gateway-using-spring-cloud-gateway/)

[4. Microservices – Authentication, and Authorization With Keycloak](https://javatodev.com/microservices-authentication-and-authorization-with-keycloak/)

[5. Microservices – Core Banking Service Implementation](https://javatodev.com/microservices-core-banking-service-implementation/)

[6. Microservices – User Service Implementation](https://javatodev.com/microservices-user-service-implementation/)

[7. Microservices – Fund Transfer Service Implementation](https://javatodev.com/microservices-fund-transfer-service-implementation/)

[8. Microservices – Utility Payment Service Implementation](https://javatodev.com/microservices-utility-payment-service-implementation/)

[9. Microservices – Communication With Spring Cloud OpenFeign](https://javatodev.com/microservices-communication-with-spring-cloud-openfeign/)

[10. Microservices – Exception Handling](https://javatodev.com/microservices-exception-handling/)

[11. Microservices – Centralized Configurations With Spring Cloud Config](https://javatodev.com/microservices-centralized-configurations-with-spring-cloud-config/)

#### Author

<h1 align="center">Hi 👋, I'm Chinthaka Dinadasa</h1>
<h3 align="center">A Passionate Java Fullstack Developer from Sri Lanka and Author of JavatoDev.com</h3>

<p align="left"> <a href="https://twitter.com/spbootdeveloper" target="blank"><img src="https://img.shields.io/twitter/follow/spbootdeveloper?logo=twitter&style=for-the-badge" alt="spbootdeveloper" /></a> </p>
