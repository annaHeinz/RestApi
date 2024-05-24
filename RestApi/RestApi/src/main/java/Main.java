import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class Main {
    static RestTemplate restTemplate = new RestTemplate();
    static HttpHeaders headers = new HttpHeaders();
    static String sessionId;
    public static void main(String[] args) {
        // Получение списка всех пользователей и сохранение session id
        getAndSaveSessionId();
        // Сохранение нового пользователя
        saveNewUser();
        updateUser();
        deleteUser();
    }

    public static void getAndSaveSessionId() {
        String apiUrl = "http://94.198.50.185:7081/api/users";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        // Получение session id из заголовка set-cookie
       sessionId = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        headers.set(HttpHeaders.COOKIE, sessionId);
        System.out.println("Сессия: " + sessionId);
    }

    public static void saveNewUser() {
        String apiUrl = "http://94.198.50.185:7081/api/users";

        // Подготовка данных пользователя
        String newUserJson = "{\"id\":3,\"name\":\"James\",\"lastName\":\"Brown\",\"age\":30}";

        // Устанавливаем заголовок Content-Type как application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, sessionId);

        // Создание HttpEntity с данными пользователя и заголовками
        HttpEntity<String> requestEntity = new HttpEntity<>(newUserJson, headers);

        // Выполнение POST-запроса для сохранения пользователя
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // Проверка успешности операции
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Пользователь успешно сохранен.");
            // Получение обновленных данных с добавленным пользователем
            getData();
        } else {
            System.out.println("Ошибка при сохранении пользователя.");
        }
    }
//
    public static void getData() {
        String apiUrl = "http://94.198.50.185:7081/api/users";
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        // Вывод списка всех пользователей
        System.out.println("Список всех пользователей:");
        System.out.println(responseEntity.getBody());
    }
    public static void updateUser() {
        String apiUrl = "http://94.198.50.185:7081/api/users"; // Указываем id пользователя в пути URL

        // Подготовка данных пользователя для обновления
        String updatedUserJson = "{\"id\":3,\"name\":\"Thomas\",\"lastName\":\"Shelby\",\"age\":30}";

        // Устанавливаем заголовок Content-Type как application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, sessionId);

        // Создание HttpEntity с данными пользователя и заголовками
        HttpEntity<String> requestEntity = new HttpEntity<>(updatedUserJson, headers);

        // Выполнение PUT-запроса для обновления пользователя
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, String.class);

        // Проверка успешности операции
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Пользователь успешно изменен.");
            // Получение обновленных данных с измененным пользователем
            getData();
        } else {
            System.out.println("Ошибка при изменении пользователя.");
        }
    }
    public static void deleteUser() {
        String apiUrl = "http://94.198.50.185:7081/api/users/3"; // Указываем id пользователя в пути URL

        // Устанавливаем заголовок Content-Type как application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, sessionId);

        // Создание HttpEntity с пустым телом запроса и заголовками
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Выполнение DELETE-запроса для удаления пользователя
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.DELETE, requestEntity, String.class);

        // Проверка успешности операции
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Пользователь успешно удален.");
            // Получение обновленных данных после удаления пользователя
            getData();
        } else {
            System.out.println("Ошибка при удалении пользователя.");
        }
    }

}
