# Инструкция

У вас должна быть установлена Java не ниже версии 17<br/>
У вас должен быть установлен Maven последней версии

1. Разверните сервер с PostgreSQL<br/>

2. Создайте базу данных`<br/>
   
3. Создайте в этой базе данных таблицу<br/>
   Для этого можете использовать скрипт ```init.sql``` в папке ***\src\main\resources***<br/>

4. Настройте файл характеристик<br/>
   В файле ***\src\main\resources\application.properties*** замените имя и пароль от базы данных на ваши<br/>
   Пример:<br/>
   ```
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
     
3. Соберите проект<br/>
   Откройте терминал в корневой директории проекта (где находится ***pom.xml***) и выполните команду:<br/>
   ```mvn clean install -DskipTests```

4. После успешной сборки проекта можете запустить приложение с необходимыми параметрами<br/>
   ```mvn spring-boot:run``` (через Maven)<br/>
   ```java -jar target/enployee-0.0.1-SNAPSHOT.jar``` (как исполняемый JAR-файл)
   
5. Можете развернуть приложение через ```docker-compose.yml``` (опционально)<br/>
   
6. Для этого замените в файле ```docker-compose.yml``` параметры на ваши из файла ***\src\main\resources\application.properties*** на ваши собственные<br/>
   ```
   SPRING_DATASOURCE_USERNAME: your_username
   SPRING_DATASOURCE_PASSWORD: your_password
   ```
7. Можете использовать JSON в корневой директории для тестирования REST-API
