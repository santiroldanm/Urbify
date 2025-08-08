# 🌆 Urbify | Gestión Residencial Inteligente

**Urbify** es una aplicación web integral para la administración eficiente de conjuntos residenciales. Permite gestionar residentes, parqueaderos, correspondencia, objetos perdidos y más, todo desde una interfaz centralizada y moderna. Su arquitectura modular con Spring Boot garantiza robustez, escalabilidad y seguridad.

---

## 🚀 Características

### 🔹 Administrador (Admin)
- ✔️ Gestión de vigilantes (crear, editar, eliminar)
- ✔️ Control de accesos y permisos
- ✔️ Administración de residentes
- ✔️ Control de pagos
- ✔️ Gestión de parqueaderos
- ✔️ Manejo de correspondencias

### 🔹 Vigilante (Vigilant)
- ✔️ Registro de entradas y salidas de visitantes y vehículos
- ✔️ Edición y eliminación de registros desde una interfaz intuitiva
- ✔️ Reporte de objetos perdidos y correspondencia

---

## 🛠️ Tecnologías Utilizadas

### 🔹 Backend
- Spring Boot
- Spring Security
- JPA / Hibernate (ORM)
- SQL
- Maven

### 🔹 Frontend
- Thymeleaf
- HTML
- Tailwind CSS
- JavaScript

---

## 🧱 Arquitectura

El sistema está dividido en capas bien estructuradas:

- **Controllers:** Gestionan las rutas HTTP y comunican el frontend con los servicios.
- **Services:** Contienen la lógica de negocio principal.
- **Repositories:** Usan `JpaRepository` para operaciones CRUD.
- **Models:** Representan las entidades de la base de datos con anotaciones como `@Entity`.

---

## 🔒 Seguridad

Se implementa **Spring Security** para:

- Autenticación de usuarios
- Gestión de sesiones
- Autorización por roles

---

## 🖥️ Interfaz de Usuario

La interfaz fue desarrollada con **Thymeleaf**, combinando HTML, Tailwind CSS y JavaScript. Esto permite una experiencia dinámica y responsiva, con animaciones suaves y visuales agradables para los usuarios.

---

## 📖 Más Información

📄 Presentación del proyecto:  
[https://docs.google.com/presentation/d/1rEaDLFOfRKt7kQszlyHYTI9_eph87QwAjM4NK58VYGY/edit?usp=sharing](https://docs.google.com/presentation/d/1rEaDLFOfRKt7kQszlyHYTI9_eph87QwAjM4NK58VYGY/edit?usp=sharing)

---

## 🙌 Créditos

Desarrollado por:
- Emmanuel Pérez Vivas
- Santiago Roldán Muñoz
- Juan Pablo Sánchez Jaramillo
- Juan Pablo Restrepo Muñoz
- Juan Manuel López Vélez
