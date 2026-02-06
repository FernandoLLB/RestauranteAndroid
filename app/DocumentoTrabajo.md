PRÁCTICA FINAL – DESARROLLO DE APLICACIONES
MULTIPLATAFORMA (2º DAM)
Desarrollo de una aplicación Android moderna con Kotlin y Jetpack
Compose
1. Introducción
En esta práctica final el alumno/a deberá desarrollar una aplicación Android completa,
aplicando los conocimientos adquiridos a lo largo del curso sobre Kotlin, arquitectura
de aplicaciones, persistencia de datos, consumo de APIs REST, gestión de estados,
interfaz moderna con Jetpack Compose y experiencia de usuario.
El objetivo principal es simular un proyecto real de empresa, donde se combine el uso
de servicios remotos con almacenamiento local, control de roles de usuario, gestión de
estados de carga y error, uso de preferencias, notificaciones del sistema y
funcionalidades propias del sistema operativo Android como el uso de cámara y galería
mediante intents.
La aplicación deberá estar correctamente estructurada, ser estable, mantener una
buena experiencia de usuario y cumplir todos los requisitos indicados a continuación.
2. Tecnologías y requisitos técnicos
La aplicación deberá desarrollarse obligatoriamente en Kotlin, utilizando Jetpack
Compose como sistema de UI. Se deberá emplear Retrofit para la comunicación con la
API REST, Room para la persistencia local de datos, DataStore para el almacenamiento
de preferencias y token de sesión, y una arquitectura basada en MVVM, haciendo uso de
ViewModel y gestión los estados.
Será obligatorio el uso de Material Design 3, una Bottom Navigation Bar como sistema
principal de navegación y una correcta separación de responsabilidades entre capas.
3. Autenticación y gestión de usuarios
La aplicación contará con un sistema de login y registro de usuarios, conectándose a
una API REST mediante Retrofit. Tras un login correcto, la API devolverá un token de
autenticación, el cual deberá almacenarse de forma persistente usando DataStore.
Mientras el token exista y sea válido, el usuario no deberá volver a la pantalla de login al
abrir la aplicación. Deberá existir una opción de cerrar sesión, que elimine el token
almacenado y devuelva al usuario a la pantalla de autenticación.
Se trabajará con dos tipos de usuario: usuario normal y usuario administrador. El rol
vendrá determinado por la respuesta de la API.
4. Funcionalidades exclusivas de administrador
Cuando el usuario autenticado tenga rol de administrador, la aplicación deberá mostrar
una pantalla adicional desde la cual se pueda visualizar un listado completo de usuarios
registrados.
Desde esta pantalla, el administrador podrá:
• Modificar el nombre de un usuario.
• Eliminar usuarios existentes.
No estará permitido que un administrador se elimine a sí mismo. El acceso a esta
pantalla deberá estar protegido, de modo que un usuario sin rol administrador no pueda
acceder a ella en ninguna circunstancia.
5. Gestión de ítems con persistencia local (ROOM)
La aplicación deberá incluir un sistema de gestión de ítems cuya temática será libre,
elegida por cada alumno (por ejemplo: tareas, ítems de videojuegos, libros, películas,
equipos deportivos, etc.). La elección de una temática original y bien desarrollada será
valorada positivamente.
Cada ítem deberá contener, como mínimo, un título, una descripción amplia, una
imagen, la fecha de creación y el usuario que lo ha creado. Estos ítems se almacenarán
localmente utilizando Room, implementando un CRUD completo: crear, leer, editar y
eliminar.
6. Listados, expansión y filtrado de ítems
La aplicación deberá mostrar un listado de ítems que permita alternar entre:
• Ver únicamente los ítems creados por el usuario autenticado.
• Ver todos los ítems creados por todos los usuarios.
Cuando se muestren ítems de todos los usuarios, deberá indicarse claramente qué
usuario ha publicado cada uno.
Al pulsar sobre un ítem del listado, este deberá expandirse, mostrando información
detallada adicional y ofreciendo opciones para editar o eliminar el ítem.
Además, será obligatorio implementar un sistema de búsqueda y filtrado, que permita
al usuario localizar ítems por texto.
7. Uso de cámara y galería mediante Intents
En la creación y edición de ítems será obligatorio permitir al usuario añadir una imagen.
Esta imagen podrá seleccionarse:
• Desde la galería del dispositivo.
• Mediante la cámara, haciendo uso de intents.
Se deberán gestionar correctamente los permisos necesarios y mostrar una
previsualización de la imagen seleccionada antes de guardar el ítem.
8. Ajustes de la aplicación
La aplicación contará con una pantalla de ajustes desde la cual el usuario podrá activar
o desactivar el modo oscuro. Esta preferencia deberá guardarse utilizando DataStore y
aplicarse de forma global en toda la aplicación.
9. Manejo de estados con sealed classes
Tanto las operaciones realizadas contra la API REST como las operaciones de Room
deberán manejarse mediante sealed classes de estado, siendo obligatorio contemplar,
como mínimo, los siguientes estados:
• Idle
• Loading
• Success
• Error
La interfaz deberá reaccionar a estos estados mostrando indicadores de carga,
mensajes de éxito o error y evitando acciones inconsistentes. No se aceptarán
implementaciones que realicen operaciones sin control de estado.
10. Control de estado offline
La aplicación deberá ser capaz de detectar la falta de conexión a internet y reaccionar
correctamente. Cuando no exista conexión:
• No se deberán realizar llamadas a la API.
• Se informará visualmente al usuario del estado offline.
• Las funcionalidades locales con Room deberán seguir funcionando.
Este control deberá integrarse dentro de la lógica de estados de la aplicación.
11. Notificaciones del sistema
Cada vez que se cree, edite o elimine un ítem, la aplicación deberá lanzar una
notificación del sistema, informando de la acción realizada. Se deberá configurar
correctamente el canal de notificaciones siguiendo las buenas prácticas de Android.
12. Formularios y validaciones
Todos los formularios de la aplicación deberán incluir validaciones adecuadas. En el
login y registro se validarán campos como el email y la contraseña. En los formularios de
creación y edición de ítems se validará que los campos obligatorios estén completos y
cumplan los requisitos establecidos.
Los formularios asociados a Room deberán ser amplios, bien estructurados y claros
para el usuario.
13. Navegación y diseño
La aplicación deberá utilizar una Bottom Navigation Bar obligatoria como elemento
principal de navegación. El diseño general será evaluado, teniendo en cuenta el uso
correcto de Material Design, la coherencia visual, el espaciado, la tipografía, las
animaciones y la experiencia de usuario.
14. Entrega y evaluación
La práctica se entregará mediante un repositorio GitHub que incluya el código completo.
Se valorará especialmente la calidad del código, la correcta arquitectura, el uso
adecuado de estados, la estabilidad de la aplicación y el cumplimiento de todos los
requisitos indicados.
15. Extras
• Tests: pruebas unitarias en ViewModels y lógica de negocio para verificar
inserción, modificación, eliminación de datos, manejo de errores y estados de
carga.
• Experiencia de usuario (UX): confirmaciones antes de borrar, opción de deshacer
acciones, animaciones, skeleton loading y mejoras visuales en listados y
formularios.
• Mejoras técnicas: interceptor de Retrofit para gestionar el token
automáticamente, mejor control de errores de red, paginación en listas largas y
optimización del acceso a datos locales.
• Funcionalidades extra: marcar ítems como favoritos, compartir ítems mediante
intents, perfil de usuario editable e internacionalización (varios idiomas).
• Calidad del código: todas las funcionalidades deben respetar la arquitectura,
usar estados correctamente, evitar código duplicado y mantener buena
organización y legibilidad.
16. Estructrura del proyecto
app/
└─ src/main/
 ├─ AndroidManifest.xml
 ├─ java/com/example/pracfinal/
 │ ├─ MainActivity.kt
 │ ├─ navigation/
 │ ├─ data/
 │ │ ├─ repository/
 │ │ ├─ local/
 │ │ ├─ remote/
 │ │ └─ datastore/
 │ ├─ ui/
 │ │ ├─ screens/
 │ │ ├─ components/
 │ │ └─ theme/
 │ │ ├─ Color.kt
 │ │ ├─ Theme.kt
 │ │ └─ Type.kt
 │ ├─ uiState/
 │ ├─ viewmodel/
 │ └─ util/
 └─ res/
 └─ drawable/...