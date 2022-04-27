package com.example.demo;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class demoController {
    private static final String URL = "http://94.198.50.185:7081/api/users";
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        //создаем заголовок, устанавливает тип возвращаемого значение
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //объект запроса
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        //получение sessionId, получаем единожды из getAllUsers, далее все операции происходят в одной сессии
        ResponseEntity<List> responseEntity = getAllUsers(requestEntity);
        ;
       /*Когда вы получите ответ на свой первый запрос, вы должны сохранить свой session id ,
       полученный с помощью файла cookie. Вы получите его в заголовке ответа set-cookie,
       затем в каждом последующем запросе вы должны установить заголовок запроса Cookie со значениями*/
        headers.set("Cookie", responseEntity.getHeaders().get("Set-Cookie").stream().collect(Collectors.joining(";")));


        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge((byte) 27);
        HttpEntity<User> entity = new HttpEntity<>(newUser, headers);
        createUser(entity);

        newUser.setName("Thomas");
        newUser.setLastName("Shelby");
        entity = new HttpEntity<>(newUser, headers);
        updateUser(entity);

        deleteUser(entity);

    }

    //RestTemplate специальный класс для работы с запросами,
    public static ResponseEntity<List> getAllUsers(HttpEntity<Object> requestEntity) {
        ResponseEntity<List> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, requestEntity, List.class);
        return responseEntity;
    }

    public static void createUser(HttpEntity<User> entity) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        System.out.println(responseEntity.getBody());
    }

    public static void updateUser(HttpEntity<User> entity) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);
        System.out.println(responseEntity.getBody());
    }

    public static void deleteUser(HttpEntity<User> entity) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, entity, String.class);
        System.out.println(responseEntity.getBody());
    }
}
