# plan-everything-server #
## Настройка и запуск ##
1. Создать PostgreSQL базу данных и убедиться, что сервер СУБД запущен; 
1. Создать файл *src/main/resources/application.yml* на основе [примера](src/main/resources/application.yml.sample);
1. Указать значения настроек (см. раздел **"Описание настроек приложения"**);
1. Выполнить сборку с помощью maven `mvn clean package` (проект включает в себя maven wrapper);
1. Запустить приложение `java -jar .\target\server-0.0.1-SNAPSHOT.jar`.

### Описание настроек приложения ###
Название | Описание
:---:|:---:
spring.datasource.url | JDBC URL подключения к базе данных
spring.datasource.user | Имя пользователя базы данных
spring.datasource.password | Пароль пользователя базы данных 
cors.origin | URL клиентского приложения для настройки CORS
security.admin.fullName | Имя и фамилия учетной записи администратора
security.admin.email | Электронная почта учетной записи администратора
oauth.resourceId | Идентификатор ресурса - произвольная строка
oauth.client.web.id | Идентификатор клиентского прилоложения для доступа к ресурсу
oauth.client.web.secret | Пароль клиентского прилоложения для доступа к ресурсу
