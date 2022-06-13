# Host-Alive-Api
호스트 Alive 상태 체크 및 모니터링 API 서버 개발
### Project 구조

• Spring Boot   
• MARIADB (RDMBS)  
• JPA (ORM)   

>controller  - Host컨트롤러
> 
>domain - Host엔티티
>>base - Host엔티티가 상속받는 jpa @MappedSuperclass(날짜,수정날짜)
>>
>>
>dto - reqeustDto
>>
>repository - jpa 리포지토리
>
>response - 응답Dto
>
>service - Host서비스
>
>status - 응답 코드 Status

### URI
@GetMapping("/hosts/{id}") :: 한 호스트만 조회   
@GetMapping("/hosts") :: 전체조회     
@GetMapping("/hosts/{name}/name-exists") 이름 중복체크    
@GetMapping("/hosts/{ip}/ip-exists") 아이피 중복체크   
@PostMapping("/hosts") 호스트 입력     
@PutMapping("/hosts/{id}") 호스트 수정    
@DeleteMapping("/hosts/{id}") 호스트 삭제       


### 시험 결과 리스트

![샘플 데이터1](https://user-images.githubusercontent.com/97586086/173371300-00d791fc-d3cb-45de-94c4-141dd8c12041.png)
![샘플 데이터2](https://user-images.githubusercontent.com/97586086/173371312-15675ae7-8ce7-4f9d-a7ac-eedef579d38f.png)
